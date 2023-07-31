import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Gson gson = new Gson();
        List<Cep> list = new ArrayList<Cep>();

        while (true) {
            try {

                System.out.println("Digite um CEP para buscar, " + (list.size() > 0 ? "'salvar' para salvar a lista de cidades buscadas" : "") + "ou um digite 'sair' para sair");
                String cep = sc.nextLine();

                // Nunca comparar strings com ==
                if (cep.equalsIgnoreCase("sair")) {
                    break;
                } else if (cep.equalsIgnoreCase("salvar")){
                    if(list.size() == 0)
                        throw new Exception("Não há nenhum cep para ser salvo!");


                    FileWriter fileWriter = new FileWriter("enderecos.json");
                    fileWriter.write(gson.toJson(list));
                    fileWriter.close();
                    break;
                }

                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://viacep.com.br/ws/" + cep + "/json/"))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

                Cep retorno = gson.fromJson(response.body(), Cep.class);
                System.out.println(retorno);

                list.add(retorno);
            } catch (Exception e) {
                System.out.println("Erro ao buscar CEP! " + e.getMessage());
            }
        }


    }
}