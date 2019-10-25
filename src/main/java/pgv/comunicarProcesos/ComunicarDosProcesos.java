package pgv.comunicarProcesos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ComunicarDosProcesos {

	public static void main(String[] args) throws IOException {
				
		
		//Se ejecutan dos procesos, uno padre y uno hijo. El padre lista/lee y el hijo escribe los resultados del proceso padre
		//Realiza un pipeline (salida de proceso padre e a entrada del proceso hijo)
		
		String bash = "bash.exe"; // Almacena la consola o programa a usar
		
		String comando_a = "ls -la"; //Comando a ejecutar en el proceso a
		String comando_b =  "tr d D"; //Comando a ejecutar en el proceso b
				
		try {
			
			
			//PROCESO A
			
			//Crear y empezar			
			ProcessBuilder pb_a = new ProcessBuilder(bash, "-c", comando_a);			
			Process proceso_a = pb_a.start();		
			
			//Conjunto de Streams que entrada - leen resultado del proceso A
			
			InputStream is1 = proceso_a.getInputStream();
			InputStreamReader isr1 = new InputStreamReader(is1, StandardCharsets.UTF_8);
			BufferedReader br1 = new BufferedReader(isr1); 
			
			//PROCESO B			
			
			//Crear y empezar proceso b
			
			ProcessBuilder pb_b = new ProcessBuilder(bash, "-c",comando_b ); 
			Process proceso_b = pb_b.start();	
			
			//Conjunto de Streams de salida que escribiran lo recogido en el proceso anterior			
			OutputStream out = proceso_b.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(out);
			BufferedWriter bw = new BufferedWriter(osw);
			
			//Escribir el buffered almacenado del proceso A en el proceso B
			String line;			
			while( (line = br1.readLine()) != null ) {
				System.out.println("ENTRADA:" + line); //Muestra por pantalla el contendido del proceso A
				bw.write(line + "\n"); //Escribir el buffered almacenado del proceso A en el proceso B
			}			
			
			bw.close();
			
			
			InputStream is2 = proceso_b.getInputStream();
			InputStreamReader isr2 = new InputStreamReader(is2, StandardCharsets.UTF_8);
			BufferedReader br2 = new BufferedReader(isr2); 
			
			
			//Conjunto de Streams que entrada - leen resultado del proceso B
			
			
				
			while( (line =br2.readLine()) != null ) {
				System.out.println("SALIDA: " + line); //Escribir por pantalla buffered almacenado del proceso B	
			}
			
			br2.close();
			bw.close();
			
			
		} catch( IOException e ) {
			
			System.err.println("No se ha ejecutado correctamente");
			System.exit(-1);
			
		} 
		
	}
}
