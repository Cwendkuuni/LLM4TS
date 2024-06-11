import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null);
        assertEquals("Expected HTML content", "<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8");
        assertEquals("Expected HTML content", "<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test(expected = IOException.class)
    public void testLoadNullFile() {
        Document doc = DataUtil.load(null, "UTF-8");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.load(inputStream, null);
        assertEquals("Expected HTML content", "<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8");
        assertEquals("Expected HTML content", "<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInput() {
        Document doc = DataUtil.load(null, "UTF-8");
    }
}