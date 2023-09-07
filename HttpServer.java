import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

public class HttpServer{
    public static void main (String[] args) throws Exception{
        //start Receiving messages
        try(ServerSocket serverSocket = new ServerSocket(2728)){
            System.out.println("Server Initiated");
            while(true){
                try(Socket client = serverSocket.accept()){
                    System.out.println("Debug : got a new message "+client.toString());
                    InputStreamReader inputStream = new InputStreamReader(client.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStream);
                    StringBuilder request = new StringBuilder();
                    String line;
                    line = bufferedReader.readLine();
                    while(!line.isBlank()){
                        request.append(line+"\r\n");
                        line = bufferedReader.readLine();
                    }
                    // System.out.println("-----------Request----------");
                    // System.out.println(request.toString());
                    String firtLineRequest = request.toString().split("\n")[0];
                    String resourceUrl = firtLineRequest.split(" ")[1];
                    System.out.println(resourceUrl);
                    if(resourceUrl.equals("/")){
                        //Getting a html file as response
                        FileInputStream homePage = new FileInputStream("./htdocs/index.php");
                        OutputStream clientOutput = client.getOutputStream();
                        clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                        clientOutput.write(("\r\n").getBytes());
                        clientOutput.write(homePage.readAllBytes());
                        clientOutput.flush();
                        homePage.close();
                    }else if(resourceUrl.equals("/batman")){
                        //Getting a image as response
                        FileInputStream image = new FileInputStream("./htdocs/bATMAN.jpeg");
                        OutputStream clientOutput = client.getOutputStream();
                        clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                        clientOutput.write(("\r\n").getBytes());
                        clientOutput.write(image.readAllBytes());
                        clientOutput.flush();
                        image.close();
                    }else{
                        FileInputStream errorPage = new FileInputStream("./htdocs/404.html");
                        OutputStream clientOutput = client.getOutputStream();
                        clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                        clientOutput.write(("\r\n").getBytes());
                        clientOutput.write(errorPage.readAllBytes());
                        clientOutput.flush();
                        errorPage.close();
                    }

                client.close();
                }
            }
        }
    }
}