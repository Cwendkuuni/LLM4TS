import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "GBK"; // invalid charset
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        InputStream input = this.getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(input, charsetName, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "GBK"; // invalid charset
        InputStream input = this.getClass().getResourceAsStream("/test.html");
        try {
            Document doc = DataUtil.load(input, charsetName, null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        InputStream input = this.getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(input, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        InputStream input = this.getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        Document doc = DataUtil.parseInputStream(input, charsetName, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream input = this.getClass().getResourceAsStream("/test.html");
        String charsetName = "GBK"; // invalid charset
        try {
            Document doc = DataUtil.parseInputStream(input, charsetName, null, Parser.htmlParser());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseInputStreamWithNullCharset() throws IOException {
        InputStream input = this.getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }
}