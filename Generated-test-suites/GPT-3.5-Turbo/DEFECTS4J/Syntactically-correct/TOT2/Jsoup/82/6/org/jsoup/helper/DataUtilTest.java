import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions as needed
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream input = getClass().getResourceAsStream("testFile.html");
        Document doc = DataUtil.load(input, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions as needed
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream input = getClass().getResourceAsStream("testFile.html");
        Document doc = DataUtil.load(input, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        // Add more assertions as needed
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        // Test case for crossStreams method
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test case for readToByteBuffer method
    }
    
    // Add more test methods for other public methods in DataUtil class
    
}