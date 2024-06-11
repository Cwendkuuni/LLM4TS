import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;

public class JsoupTest {
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);

        assertEquals(1, document.select("h1").size());
        assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);

        assertEquals(1, document.select("h1").size());
        assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        Document document = Jsoup.parse(html);

        assertEquals(1, document.select("h1").size());
        assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);

        assertEquals(url, connection.url());
    }

    @Test
    public void testParseFileWithCharsetName() throws IOException {
        String filePath = "/path/to/file.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(new File(filePath), charsetName, baseUri);

        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseFileWithoutCharsetName() throws IOException {
        String filePath = "/path/to/file.html";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(new File(filePath), null, baseUri);

        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseInputStreamWithCharsetName() throws IOException {
        InputStream inputStream = new FileInputStream("/path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);

        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseInputStreamWithoutCharsetName() throws IOException {
        InputStream inputStream = new FileInputStream("/path/to/file.html");
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, null, baseUri);

        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);

        assertEquals(1, document.select("h1").size());
        assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);

        assertEquals(1, document.select("h1").size());
        assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<h1>Hello <script>alert('World!');</script></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);

        assertEquals("<h1>Hello </h1>", cleanHtml);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<h1>Hello <script>alert('World!');</script></h1>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);

        assertEquals("<h1>Hello </h1>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<h1>Hello <script>alert('World!');</script></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);

        assertEquals("<h1>Hello </h1>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello <script>alert('World!');</script></h1>";
        Whitelist whitelist = Whitelist.basic();

        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsValidWithValidHtml() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();

        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }
    
}