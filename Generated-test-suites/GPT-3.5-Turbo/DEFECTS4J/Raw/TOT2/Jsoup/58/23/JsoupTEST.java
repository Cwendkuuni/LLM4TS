import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        Document doc = Jsoup.parse("<html></html>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseWithParser() {
        Document doc = Jsoup.parse("<html></html>", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseWithoutBaseUri() {
        Document doc = Jsoup.parse("<html></html>");
        assertNotNull(doc);
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("http://example.com");
        assertNotNull(con);
    }

    @Test
    public void testParseFile() {
        Document doc = Jsoup.parse(new File("test.html"), "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseFileWithCharsetName() {
        Document doc = Jsoup.parse(new File("test.html"), "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() {
        InputStream is = new FileInputStream("test.html");
        Document doc = Jsoup.parse(is, "UTF-8", "http://example.com");
        assertNotNull(doc);
        is.close();
    }

    @Test
    public void testParseInputStreamWithParser() {
        InputStream is = new FileInputStream("test.html");
        Document doc = Jsoup.parse(is, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        is.close();
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        Document doc = Jsoup.parseBodyFragment("<div></div>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        Document doc = Jsoup.parseBodyFragment("<div></div>");
        assertNotNull(doc);
    }

    @Test
    public void testParseUrlWithTimeout() {
        Document doc = Jsoup.parse(new URL("http://example.com"), 5000);
        assertNotNull(doc);
    }

    @Test
    public void testClean() {
        String cleaned = Jsoup.clean("<div></div>", Whitelist.basic());
        assertNotNull(cleaned);
    }

    @Test
    public void testCleanWithBaseUri() {
        String cleaned = Jsoup.clean("<div></div>", "http://example.com", Whitelist.basic());
        assertNotNull(cleaned);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String cleaned = Jsoup.clean("<div></div>", "http://example.com", Whitelist.basic(), new Document.OutputSettings());
        assertNotNull(cleaned);
    }

    @Test
    public void testIsValid() {
        boolean valid = Jsoup.isValid("<div></div>", Whitelist.basic());
        assertTrue(valid);
    }
}