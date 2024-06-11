import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.charset.IllegalCharsetNameException;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.xmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inputStream, outputStream);
        
        // Add assertions to check the output stream
    }

    // Add more test methods for other public methods in DataUtil class
    
    @Test(expected = IOException.class)
    public void testLoadFileWithIOException() throws IOException {
        File file = new File("non_existing_file.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testGetCharsetFromInvalidContentType() {
        String contentType = "text/html; charset=invalid_charset";
        
        DataUtil.getCharsetFromContentType(contentType);
    }

    // Add more test methods for edge cases and exception handling scenarios

}