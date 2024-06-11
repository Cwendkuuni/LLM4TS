import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

public class JsoupTest {

    private Document document;

    @Before
    public void setup() {
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        document = Jsoup.parse(html);
    }

    @Test
    public void testParse() {
        assertEquals("html", document.nodeName());
        assertEquals("Test HTML", document.title());
        assertEquals("Hello, World!", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String baseUri = "http://example.com";
        Document document = Jsoup.parse("<a href='test.html'>Test</a>", baseUri);

        assertEquals(baseUri + "/test.html", document.getElementsByTag("a").first().absUrl("href"));
    }

    @Test
    public void testParseWithParser() {
        String xml = "<root><element>Test</element></root>";
        Parser parser = Parser.xmlParser();

        Document document = Jsoup.parse(xml, "", parser);

        assertEquals("element", document.getElementsByTag("element").first().tagName());
        assertEquals("Test", document.getElementsByTag("element").first().text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url).userAgent("Mozilla").data("name", "jsoup").get();

        assertNotNull(connection);
        assertEquals(url, connection.request().url().toString());
        assertEquals("Mozilla", connection.request().header("User-Agent"));
        assertEquals("jsoup", connection.request().data().get("name"));
    }

    @Test(expected = IOException.class)
    public void testParseFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "invalidCharset";

        Jsoup.parse(file, charsetName);
    }

    @Test
    public void testParseFileWithValidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = file.getAbsolutePath();

        Document document = Jsoup.parse(file, charsetName, baseUri);

        assertNotNull(document);
        assertEquals("Test HTML", document.title());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "invalidCharset";

        Jsoup.parse(inputStream, charsetName, "");
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = Jsoup.parse(inputStream, charsetName, baseUri);

        assertNotNull(document);
        assertEquals("Test HTML", document.title());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<div><p>Test</p></div>";
        String baseUri = "http://example.com";

        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("body", document.getElementsByTag("body").first().tagName());
        assertEquals("div", document.getElementsByTag("div").first().tagName());
        assertEquals("p", document.getElementsByTag("p").first().tagName());
        assertEquals("Test", document.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test</p></div>";

        Document document = Jsoup.parseBodyFragment(bodyHtml);

        assertEquals("body", document.getElementsByTag("body").first().tagName());
        assertEquals("div", document.getElementsByTag("div").first().tagName());
        assertEquals("p", document.getElementsByTag("p").first().tagName());
        assertEquals("Test", document.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("http://example.com");

        Document document = Jsoup.parse(url, 3000);

        assertNotNull(document);
        assertEquals("Test HTML", document.title());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<div><p>Unsafe <script>alert('xss');</script> HTML</p></div>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();

        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);

        assertEquals("Unsafe HTML", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<div><p>Unsafe <script>alert('xss');</script> HTML</p></div>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);

        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);

        assertEquals("UnsafeHTML", cleanHtml);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<div><p>Unsafe <script>alert('xss');</script> HTML</p></div>";
        Whitelist whitelist = Whitelist.basic();

        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);

        assertEquals("Unsafe HTML", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<div><p>Safe HTML</p></div>";
        Whitelist whitelist = Whitelist.basic();

        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);

        assertTrue(isValid);
    }

    @Test
    public void testIsValidWithUnsafeHtml() {
        String bodyHtml = "<div><p>Unsafe <script>alert('xss');</script> HTML</p></div>";
        Whitelist whitelist = Whitelist.basic();

        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);

        assertFalse(isValid);
    }
}