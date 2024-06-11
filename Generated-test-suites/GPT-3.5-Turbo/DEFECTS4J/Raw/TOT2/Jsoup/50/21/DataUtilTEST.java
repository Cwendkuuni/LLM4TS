import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Typical Use Case
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        // Edge Cases
        // File does not exist
        file = new File("non_existing_file.html");
        try {
            DataUtil.load(file, charsetName, baseUri);
            fail("IOException should be thrown for non-existing file");
        } catch (IOException e) {
            // Exception expected
        }
        
        // Exception Handling
        // Invalid charsetName
        charsetName = "invalid_charset";
        try {
            DataUtil.load(file, charsetName, baseUri);
            fail("IllegalCharsetNameException should be thrown for invalid charset");
        } catch (IllegalCharsetNameException e) {
            // Exception expected
        } catch (IOException e) {
            fail("Wrong exception thrown for invalid charset");
        }
    }

    @Test
    public void testLoadInputStream() {
        // Typical Use Case
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        // Edge Cases
        // Null InputStream
        inputStream = null;
        try {
            DataUtil.load(inputStream, charsetName, baseUri);
            fail("IOException should be thrown for null input stream");
        } catch (IOException e) {
            // Exception expected
        }
        
        // Exception Handling
        // Invalid charsetName
        charsetName = "invalid_charset";
        try {
            DataUtil.load(inputStream, charsetName, baseUri);
            fail("IllegalCharsetNameException should be thrown for invalid charset");
        } catch (IllegalCharsetNameException e) {
            // Exception expected
        } catch (IOException e) {
            fail("Wrong exception thrown for invalid charset");
        }
    }

    // Additional test cases for other methods can be added here

}