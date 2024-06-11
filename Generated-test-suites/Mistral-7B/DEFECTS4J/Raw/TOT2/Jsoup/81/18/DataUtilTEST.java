import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("example.html"); // Replace with a valid file path
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body></body></html>"; // Replace with valid HTML string
        InputStream input = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(input, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><body></body></html>"; // Replace with valid HTML string
        InputStream input = new ByteArrayInputStream(html.getBytes("ISO-8859-1"));
        try {
            Document doc = DataUtil.load(input, "ISO-8859-1", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body></body></html>"; // Replace with valid HTML string
        InputStream input = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(input, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><body></body></html>"; // Replace with valid HTML string
        InputStream input = new ByteArrayInputStream(html.getBytes("ISO-8859-1"));
        try {
            Document doc = DataUtil.parseInputStream(input, "ISO-8859-1", null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

}