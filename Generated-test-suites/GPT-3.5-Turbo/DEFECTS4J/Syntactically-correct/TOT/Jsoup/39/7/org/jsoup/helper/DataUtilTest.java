import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoad_fileWithCorrectCharsetName_successfullyLoaded() throws IOException {
        // Arrange
        File file = new File("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        // Assert
        assertNotNull(document);
        // Add additional assertions based on typical use cases, edge cases, and error scenarios.
        // For example: 
        //assertEquals("Expected Title", document.title());
    }
    
    @Test
    public void testLoad_fileNotFound_throwsIOException() {
        // Arrange
        File file = new File("path/to/nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(file, charsetName, baseUri);
        });
    }
    
    @Test
    public void testLoad_inputStreamWithCorrectCharsetName_successfullyParsed() throws IOException {
        // Arrange
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        // Assert
        assertNotNull(document);
        // Add additional assertions based on typical use cases, edge cases, and error scenarios.
    }
    
    // Add more test cases for each method in DataUtil class
    
}