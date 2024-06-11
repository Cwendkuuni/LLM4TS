import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Test typical use case
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
            // Add more assertions as needed
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        // Test typical use case
        // Set up input stream
        
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
            // Add more assertions as needed
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadCustomParser() {
        // Test typical use case
        // Set up input stream
        
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
            assertNotNull(doc);
            // Add more assertions as needed
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        // Test typical use case
        // Set up input stream and output stream
        
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            // Add more assertions as needed
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Add more test methods for other public methods in DataUtil class

}