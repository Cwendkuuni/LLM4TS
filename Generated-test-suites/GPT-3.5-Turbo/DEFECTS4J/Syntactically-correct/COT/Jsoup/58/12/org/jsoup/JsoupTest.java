import org.junit.Test;

import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
        // Test basic functionality
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testParseWithParser() {
        // Test basic functionality
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testParseWithoutBaseUri() {
        // Test basic functionality
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
        assertEquals(html, document.html());
    }


    @Test
    public void testConnect() {
        // Test basic functionality
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
    }

    @Test
    public void testParseFileWithCharsetAndBaseUri() throws IOException {
        // Test basic functionality
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseFileWithCharset() throws IOException {
        // Test basic functionality
        File file = new File("example.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(file, charsetName);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
        // Test basic functionality
        InputStream inputStream = new FileInputStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        // Test basic functionality
        InputStream inputStream = new FileInputStream("example.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(inputStream, charsetName);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        // Test basic functionality
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals(bodyHtml, document.body().html());
    }

    @Test
    public void testParseBodyFragment() {
        // Test basic functionality
        String bodyHtml = "<h1>Hello World!</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
        assertEquals(bodyHtml, document.body().html());
    }

    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        // Test basic functionality
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertNotNull(cleanHtml);
        assertTrue(cleanHtml.contains("Hello World!"));
    }

    @Test
    public void testCleanWithWhitelist() {
        // Test basic functionality
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNotNull(cleanHtml);
        assertTrue(cleanHtml.contains("Hello World!"));
    }

    @Test
    public void testCleanWithBaseUriAndWhitelistAndOutputSettings() {
        // Test basic functionality
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertNotNull(cleanHtml);
        assertTrue(cleanHtml.contains("<h1>Hello World!</h1>"));
    }

    @Test
    public void testIsValidWithWhitelist() {
        // Test basic functionality
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }
}