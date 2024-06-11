import org.jsoup.helper.DataUtil;
import org.junit.Test;
import org.junit.Assert;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        // Typical use case
        String basePath = "src/test/resources/";
        String fileName = "test.html";
        String charset = "UTF-8";
        String baseUri = "https://example.com";
        
        try {
            DataUtil.load(new File(basePath + fileName), charset, baseUri);
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        // Edge case: file not found
        try {
            DataUtil.load(new File("nonexistent.html"), charset, baseUri);
            Assert.fail("Expected IOException not thrown");
        } catch (IOException e) {
            // Expected IOException
        }
        
        // Error scenario: invalid charset
        try {
            DataUtil.load(new File(basePath + fileName), "invalidCharset", baseUri);
            Assert.fail("Expected UnsupportedEncodingException not thrown");
        } catch (UnsupportedEncodingException e) {
            // Expected UnsupportedEncodingException
        } catch (IOException e) {
            Assert.fail("Unexpected Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadInputStream() {
        // Typical use case
        String charset = "UTF-8";
        String baseUri = "https://example.com";
        
        try (InputStream in = new FileInputStream("src/test/resources/test.html")) {
            DataUtil.load(in, charset, baseUri);
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        // Edge case: null input stream
        try {
            DataUtil.load(null, charset, baseUri);
            Assert.fail("Expected IOException not thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }
    
    // Add more test cases for other methods
    
}