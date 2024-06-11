import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        // Assert statements for testing document
        Assert.assertNotNull(document);
        // ...
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        // Assert statements for testing document
        Assert.assertNotNull(document);
        // ...
    }
    
    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.xmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        // Assert statements for testing document
        Assert.assertNotNull(document);
        // ...
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream inStream = getClass().getResourceAsStream("input.txt");
        OutputStream outStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inStream, outStream);
        
        // Assert statements for testing output stream
        // ...
    }
    
    // Additional test cases for other methods
    
    // ...
    
}