import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class JsoupTest {

    // 

    @Test
    public void testParse() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "https://example.com";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Title</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals("", document.baseUri());
    }

    @Test
    public void testConnect() {
        String url = "https://example.com";
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
        assertEquals(url, connection.request().url().toString());
    }

    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        assertNotNull(document);
        assertTrue(document.html().contains("Test HTML"));
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(file, charsetName);
        assertNotNull(document);
        assertTrue(document.html().contains("Test HTML"));
        assertEquals(file.getAbsolutePath(), document.baseUri());
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = DataUtil.loadStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertTrue(document.html().contains("Test HTML"));
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseInputStreamWithCharsetName() throws IOException {
        InputStream inputStream = DataUtil.loadStream("test.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(inputStream, charsetName, "");
        assertNotNull(document);
        assertTrue(document.html().contains("Test HTML"));
        assertEquals("", document.baseUri());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Title</h1>";
        String baseUri = "https://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals(bodyHtml, document.body().html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Title</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
        assertEquals(bodyHtml, document.body().html());
        assertEquals("", document.baseUri());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("https://example.com");
        int timeoutMillis = 10000;
        Document document = Jsoup.parse(url, timeoutMillis);
        assertNotNull(document);
        assertTrue(document.html().contains("Example Domain"));
        assertEquals(url.toString(), document.baseUri());
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<div><h1>Title</h1><script>alert('XSS')</script></div>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertNotNull(cleanHtml);
        assertFalse(cleanHtml.contains("<script>"));
        assertEquals("<div><h1>Title</h1></div>", cleanHtml);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<div><h1>Title</h1><script>alert('XSS')</script></div>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNotNull(cleanHtml);
        assertFalse(cleanHtml.contains("<script>"));
        assertEquals("<div><h1>Title</h1></div>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<div><h1>Title</h1><script>alert('XSS')</script></div>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertNotNull(cleanHtml);
        assertFalse(cleanHtml.contains("<script>"));
        assertEquals("<div><h1>Title</h1></div>", cleanHtml);
    }

    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<div><h1>Title</h1><script>alert('XSS')</script></div>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }

    // 

}