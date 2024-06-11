import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class JsoupTest {

    private Document document;
    private String html = "<html><head></head><body><p>Hello, World!</p></body></html>";

    @Before
    public void setUp() {
        document = Jsoup.parse(html);
    }

    @Test
    public void testParseStringString() {
        Document parsedDoc = Jsoup.parse(html, "http://example.com");
        assertNotNull(parsedDoc);
    }

    @Test
    public void testParseStringStringParser() {
        Document parsedDoc = Jsoup.parse(html, "http://example.com", Parser.xmlParser());
        assertNotNull(parsedDoc);
    }

    @Test
    public void testParseString() {
        Document parsedDoc = Jsoup.parse(html);
        assertNotNull(parsedDoc);
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
    }

    @Test
    public void testParseFileStringString() throws IOException {
        File file = new File("test.html");
        Document parsedDoc = Jsoup.parse(file, "UTF-8", "http://example.com");
        assertNotNull(parsedDoc);
    }

    @Test
    public void testParseFileString() throws IOException {
        File file = new File("test.html");
        Document parsedDoc = Jsoup.parse(file, "UTF-8");
        assertNotNull(parsedDoc);
    }

    @Test
    public void testParseInputStreamStringString() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document parsedDoc = Jsoup.parse(inputStream, "UTF-8", "http://example.com");
        assertNotNull(parsedDoc);
    }

    @Test
    public void testParseInputStreamStringStringParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document parsedDoc = Jsoup.parse(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(parsedDoc);
    }

    @Test
    public void testParseBodyFragmentString() {
        Document parsedDoc = Jsoup.parseBodyFragment("<p>Hello, World!</p>", "http://example.com");
        assertNotNull(parsedDoc);
    }

    @Test
    public void testParseBodyFragment() {
        Document parsedDoc = Jsoup.parseBodyFragment("<p>Hello, World!</p>");
        assertNotNull(parsedDoc);
    }

    @Test
    public void testParseURLInt() throws IOException {
        URL url = new URL("http://example.com");
        Document parsedDoc = Jsoup.parse(url, 5000);
        assertNotNull(parsedDoc);
    }

    @Test
    public void testCleanStringStringWhitelist() {
        String cleanHtml = Jsoup.clean(html, "http://example.com", Whitelist.basic());
        assertNotNull(cleanHtml);
    }

    @Test
    public void testCleanStringWhitelist() {
        String cleanHtml = Jsoup.clean(html, Whitelist.basic());
        assertNotNull(cleanHtml);
    }

    @Test
    public void testCleanStringStringWhitelistOutputSettings() {
        String cleanHtml = Jsoup.clean(html, "http://example.com", Whitelist.basic(), new Document.OutputSettings());
        assertNotNull(cleanHtml);
    }

    @Test
    public void testIsValid() {
        boolean valid = Jsoup.isValid(html, Whitelist.basic());
        assertTrue(valid);
    }
}