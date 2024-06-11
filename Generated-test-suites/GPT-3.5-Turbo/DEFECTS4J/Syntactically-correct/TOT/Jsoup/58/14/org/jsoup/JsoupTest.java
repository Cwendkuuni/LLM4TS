import static org.junit.Assert.*;
import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);

        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);

        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        Document document = Jsoup.parse(html);

        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals("", document.baseUri());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);

        assertNotNull(connection);
        assertEquals(url, connection.url());
        assertEquals(HttpConnection.DEFAULT_TIMEOUT, connection.timeout());
    }

    @Test
    public void testParseFromFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseFromFileWithCharsetName() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(file, charsetName);

        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(file.getAbsolutePath(), document.baseUri());
    }

    @Test
    public void testParseFromInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());

        inputStream.close();
    }

    @Test
    public void testParseFromInputStreamWithCharsetName() throws IOException {
        InputStream inputStream = new FileInputStream("example.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(inputStream, charsetName);

        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals("", document.baseUri());

        inputStream.close();
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("<body><h1>Hello World!</h1></body>", document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);

        assertNotNull(document);
        assertEquals("<body><h1>Hello World!</h1></body>", document.html());
        assertEquals("", document.baseUri());
    }

    @Test
    public void testParseUrlWithTimeout() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;
        Document document = Jsoup.parse(url, timeoutMillis);

        assertNotNull(document);
        assertEquals(url.toString(), document.baseUri());
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<p>This is a <b>bold</b> and <em>italic</em> text.</p>";
        Whitelist whitelist = Whitelist.basic();
        String safeHtml = Jsoup.clean(bodyHtml, whitelist);

        assertEquals("This is a <b>bold</b> and <i>italic</i> text.", safeHtml);
    }

    @Test
    public void testCleanWithWhitelistAndBaseUri() {
        String bodyHtml = "<p>This is a <b>bold</b> and <em>italic</em> text.</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String safeHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);

        assertEquals("This is a <b>bold</b> and <i>italic</i> text.", safeHtml);
    }

    @Test
    public void testCleanWithWhitelistAndOutputSettings() {
        String bodyHtml = "<p>This is a <b>bold</b> and <em>italic</em> text.</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String safeHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);

        assertEquals("This is a <b>bold</b> and <i>italic</i> text.", safeHtml);
    }

    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<p>This is a <b>bold</b> and <em>italic</em> text.</p>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);

        assertTrue(isValid);
    }

}