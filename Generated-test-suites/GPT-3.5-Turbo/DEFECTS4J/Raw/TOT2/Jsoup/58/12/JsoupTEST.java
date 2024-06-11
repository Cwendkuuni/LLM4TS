import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseWithParser() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseNoBaseUri() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test
    public void testConnect() {
        Connection conn = Jsoup.connect("http://example.com");
        assertNotNull(conn);
    }

    @Test
    public void testParseFileWithCharsetName() {
        File file = new File("test.html");
        try {
            Document doc = Jsoup.parse(file, "UTF-8", "");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseInputStreamWithCharsetName() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = Jsoup.parse(in, "UTF-8", "");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        Document doc = Jsoup.parseBodyFragment("<div>Test</div>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testClean() {
        String html = "<div><script>alert('XSS')</script></div>";
        Whitelist whitelist = Whitelist.basic();
        String cleanedHtml = Jsoup.clean(html, whitelist);
        assertEquals("<div></div>", cleanedHtml);
    }

    @Test
    public void testIsValid() {
        String html = "<div>Valid HTML</div>";
        Whitelist whitelist = Whitelist.basic();
        assertTrue(Jsoup.isValid(html, whitelist));
    }

}