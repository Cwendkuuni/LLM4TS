import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with actual test file
        String charsetName = "UTF-8";
        String baseUri = null;
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertEquals("Expected HTML content", "<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testHtml = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = null;
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("Expected HTML content", "<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testHtml = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes("ISO-8859-1"));
        String charsetName = "ISO-8859-1";
        String baseUri = null;
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }
}