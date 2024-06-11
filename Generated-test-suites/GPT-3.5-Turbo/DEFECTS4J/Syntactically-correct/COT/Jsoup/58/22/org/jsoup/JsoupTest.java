import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class JsoupTest {

    // Parse(String, String) method test
    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
    }

    // Parse(String, String, Parser) method test
    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
    }

    // Parse(String) method test
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head></head><body></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
    }

    // Connect(String) method test
    @Test
    public void testConnect() {
        String url = "https://example.com";
        Jsoup.Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
        assertEquals(url, connection.url());
    }

    // Parse(File, String, String) method test
    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
    }

    // Parse(File, String) method test
    @Test
    public void testParseFileWithoutCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = null;
        Document doc = Jsoup.parse(file, charsetName);
        assertNotNull(doc);
        assertEquals("file:" + file.getAbsolutePath(), doc.baseUri());
    }

    // Parse(InputStream, String, String) method test
    @Test
    public void testParseInputStreamWithCharsetName() throws IOException {
        InputStream in = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
    }

    // Parse(InputStream, String, String, Parser) method test
    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(in, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
    }

    // ParseBodyFragment(String, String) method test
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<div>test</div>";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertTrue(doc.body().html().contains(bodyHtml));
    }

    // ParseBodyFragment(String) method test
    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<div>test</div>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertTrue(doc.body().html().contains(bodyHtml));
    }

    // Parse(URL, int) method test
    @Test
    public void testParseWithURL() throws IOException {
        URL url = new URL("https://example.com");
        int timeoutMillis = 10000;
        Document doc = Jsoup.parse(url, timeoutMillis);
        assertNotNull(doc);
        assertEquals(url.toString(), doc.baseUri());
    }

    // Clean(String, String, Whitelist) method test
    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<div><script>javascript:alert('test')</script></div>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertTrue(cleanHtml.contains("<div></div>"));
        assertFalse(cleanHtml.contains("<script>javascript:alert('test')</script>"));
    }

    // Clean(String, Whitelist) method test
    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<div><script>javascript:alert('test')</script></div>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertTrue(cleanHtml.contains("<div></div>"));
        assertFalse(cleanHtml.contains("<script>javascript:alert('test')</script>"));
    }

    // Clean(String, String, Whitelist, Document.OutputSettings) method test
    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<div><script>javascript:alert('test')</script></div>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertTrue(cleanHtml.contains("<div></div>"));
        assertFalse(cleanHtml.contains("<script>javascript:alert('test')</script>"));
    }

    // IsValid(String, Whitelist) method test
    @Test
    public void testIsValid() {
        String bodyHtml = "<div><script>javascript:alert('test')</script></div>";
        Whitelist whitelist = Whitelist.basic();
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }

}