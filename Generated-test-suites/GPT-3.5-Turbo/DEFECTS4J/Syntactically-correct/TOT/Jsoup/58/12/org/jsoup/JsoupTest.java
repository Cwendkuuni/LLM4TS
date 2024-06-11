import org.junit.*;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parse(html, baseUri, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("Test", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Test", doc.body().getElementsByTag("h1").text());
    }
 
    @Test
    public void testConnect() {
        String url = "https://www.example.com";
        Connection conn = Jsoup.connect(url);
        assertNotNull(conn);
        conn.userAgent("Mozilla");
        Document doc = conn.get();
        assertNotNull(doc);
        assertEquals("Example Domain", doc.title());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", "http://www.example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseFileWithoutCharset() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, null);
        assertNotNull(doc);
        assertEquals("Test", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = Jsoup.parse(in, "UTF-8", "http://www.example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.body().getElementsByTag("h1").text());
        in.close();
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = Jsoup.parse(in, "UTF-8", "http://www.example.com", Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("Test", doc.body().getElementsByTag("h1").text());
        in.close();
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Test</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals("Test", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script>alert('hello');</script><p>Test</p>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals("<p>Test</p>", cleanHtml);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<script>alert('hello');</script><p>Test</p>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("<p>Test</p>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("<p>Test</p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p>Test</p>";
        Whitelist whitelist = Whitelist.basic();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testInvalidHtml() {
        String bodyHtml = "<script>alert('hello');</script><p>Test</p>";
        Whitelist whitelist = Whitelist.basic();
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }
}