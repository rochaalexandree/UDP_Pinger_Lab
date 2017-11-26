import java.io.*;
import java.net.*;
import java.util.*;

public class PingClient {

    private static final int CLIENT_PORT = 3030;
    private static final int PING_REQUEST = 10;
    public static void main(String[] args ) throws Exception{
        if(args.length != 2){
            System.out.println("Requerid Arguments: host port");
            return;
        }

        //captura o localhost e o numero da porta
        InetAddress ipAddress = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);

        // Create a datagram socket for receiving and sending UDP packets
         // through the port specified on the command line.
        DatagramSocket socket = new DatagramSocket(CLIENT_PORT);
        socket.setSoTimeout(1000);

        int sequence_number = 0;
        while(sequence_number < PING_REQUEST){
            // Cria Datagram de resposta do servidor
            DatagramPacket reply = new DatagramPacket(new byte[1024], 1024);

            Date date = new Date();
            long time = date.getTime();

            //Mensagem padrão que será enviada ao server
            String message = "PING " + sequence_number + " " + time + " \r\n";

            //Converte a mensagem para array de bytes
            byte[] buffer = new byte[1024];
            buffer = message.getBytes();

            // Envia datagram
            DatagramPacket pingRequest = new DatagramPacket(buffer, buffer.length, ipAddress, port);
            socket.send(pingRequest);



            // Recebe resposta do servidor
            try {
                // Tenta receber o pacote do servidor
                socket.receive(reply);
                printData(reply);
            }
            catch (SocketTimeoutException e) {
                System.out.println("Pacote perdido: " + message);
            }

            sequence_number++;
        }

    }

    /*
     * Print ping data to the standard output stream.
     */
     private static void printData(DatagramPacket request) throws Exception {
         // Obtain references to the packet's array of bytes.
         byte[] buf = request.getData();

         // Wrap the bytes in a byte array input stream,
         // so that you can read the data as a stream of bytes.
         ByteArrayInputStream bais = new ByteArrayInputStream(buf);

         // Wrap the byte array output stream in an input stream reader,
         // so you can read the data as a stream of characters.
         InputStreamReader isr = new InputStreamReader(bais);

         // Wrap the input stream reader in a bufferred reader,
         // so you can read the character data a line at a time.
         // (A line is a sequence of chars terminated by any combination of \r and \n.)
         BufferedReader br = new BufferedReader(isr);

         // The message data is contained in a single line, so read this line.
         String line = br.readLine();

         // Print host address and data received from it.
         System.out.println(
         "Received from " +
         request.getAddress().getHostAddress() +
         ": " +
         new String(line) );
     }
}
