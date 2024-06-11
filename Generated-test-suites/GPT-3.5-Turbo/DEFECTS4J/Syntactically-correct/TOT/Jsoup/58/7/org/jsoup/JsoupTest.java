import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertEquals("Test", document.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, "", parser);
        assertEquals("Test", document.select("h1").text());
    }

    @Test
    public void testConnect() throws IOException {
        String url = "https://example.com";
        Connection connection = Jsoup.connect(url);
        assertEquals(url, connection.request().url().toString());
    }

    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        assertTrue(document.hasText());
    }

    @Test
    public void testParseFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        assertTrue(document.hasText());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "https://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("Test", document.select("h1").text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script>alert('XSS')</script>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertFalse(cleanHtml.contains("<script>"));
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<p><strong>Test</strong></p>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("<p><strong>Test</strong></p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p>Test</p>";
        Whitelist whitelist = Whitelist.basic();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }
}