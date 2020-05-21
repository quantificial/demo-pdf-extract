package demo.demopdfextract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.github.jonathanlink.PDFLayoutTextStripper;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class DemoPdfExtractApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoPdfExtractApplication.class, args);
	}
	
	
	@Bean
	public ApplicationRunner applicationRunner() {
		
		return args -> {
			
			final List<String> nonOptionArgs = args.getNonOptionArgs();
			
			nonOptionArgs.forEach(nonOption -> log.info("## Non Option Args : "+nonOption));
			
//			String string = null;
//	        try {
//	            PDFParser pdfParser = new PDFParser(new RandomAccessFile(new File("bus.pdf"), "r"));
//	            pdfParser.parse();
//	            PDDocument pdDocument = new PDDocument(pdfParser.getDocument());
//	            PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
//	            string = pdfTextStripper.getText(pdDocument);
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        };
//	        System.out.println(string);
			
			Stream<Path> paths = Files.walk(Paths.get(nonOptionArgs.get(0)));
			
			paths.filter(Files::isRegularFile)
				//.filter(e-> e.getFileName().endsWith("zip"))
				.filter(e->e.getFileName().toString().endsWith(".pdf"))
				.forEach(e-> {
					
					log.info("converting pdf... {}", e.getFileName());
					
					log.info(e.getParent().toString());
					
				String result = "";
			    try {
		            PDFParser pdfParser = new PDFParser(new RandomAccessFile(new File(e.toString()), "r"));
		            pdfParser.parse();
		            PDDocument pdDocument = new PDDocument(pdfParser.getDocument());
		            PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
		            result = pdfTextStripper.getText(pdDocument);
		            
		            FileOutputStream outputStream = new FileOutputStream(e.toString()+".txt");
		            
		            outputStream.write(result.getBytes());
		            
		            outputStream.close();
		            
		            
		        } catch (FileNotFoundException ex) {
		            ex.printStackTrace();
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        };
					
					
				});
			
			
			System.exit(0);
			
			
		};
	}

}
