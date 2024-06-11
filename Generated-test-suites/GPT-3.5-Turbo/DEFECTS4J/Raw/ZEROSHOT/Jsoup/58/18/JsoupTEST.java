import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
        assertEquals(url, connection.request().url());
    }

    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("", document.html());
    }

    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(file, charsetName);
        assertNotNull(document);
        assertEquals("", document.html());
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("", document.html());
    }

    @Test
    public void testParseInputStreamWithCharsetName() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(inputStream, charsetName);
        assertNotNull(document);
        assertEquals("", document.html());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals(bodyHtml, document.body().html());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
        assertEquals(bodyHtml, document.body().html());
    }

    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertNotNull(cleanHtml);
        assertEquals(bodyHtml, cleanHtml);
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNotNull(cleanHtml);
        assertEquals(bodyHtml, cleanHtml);
    }

    @Test
    public void testCleanWithBaseUriWhitelistAndOutputSettings() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertNotNull(cleanHtml);
        assertEquals(bodyHtml, cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }

}