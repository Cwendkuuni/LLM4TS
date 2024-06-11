
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Test file loading functionality
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        // Test input stream loading functionality
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Test input stream loading functionality with parser
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Add more test cases for edge cases and exceptions handling

}
