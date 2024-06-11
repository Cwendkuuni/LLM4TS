import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, expectedCharset, baseUri);
        Assert.assertNotNull(doc);
        // Add assertions for valid document content
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, expectedCharset, baseUri);
        Assert.assertNotNull(doc);
        // Add assertions for valid document content
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String expectedCharset = "InvalidCharset";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        try {
            DataUtil.load(inputStream, expectedCharset, baseUri);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Add test cases for other methods in DataUtil class
}