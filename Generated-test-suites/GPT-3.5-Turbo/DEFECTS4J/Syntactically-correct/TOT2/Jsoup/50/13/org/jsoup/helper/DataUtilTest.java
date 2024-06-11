import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(doc);
        // Add more assertions here as needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(doc);
        // Add more assertions here as needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        
        assertNotNull(doc);
        // Add more assertions here as needed
    }

    // Add more test methods for other public methods in DataUtil class

}