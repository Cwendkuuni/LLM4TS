import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsoupTest {

    @Before
    public void setUp() throws Exception {
        // any necessary setup code
    }

    // Test case for parse(String html, String baseUri) method
    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        
        assertNotNull(document);
        assertTrue(document.title().equals("Test"));
        assertTrue(document.body().text().equals("Hello World!"));
    }

    // Test case for parse(String html, String baseUri, Parser parser) method
    @Test
    public void testParseWithParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        
        assertNotNull(document);
        assertTrue(document.title().equals("Test"));
        assertTrue(document.body().text().equals("Hello World!"));
    }

    // Test case for parse(String html) method
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document document = Jsoup.parse(html);
        
        assertNotNull(document);
        assertTrue(document.title().equals("Test"));
        assertTrue(document.body().text().equals("Hello World!"));
    }

    // Test case for connect(String url) method
    @Test
    public void testConnect() throws IOException {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        
        assertNotNull(connection);
        assertEquals(url, connection.request().url().toString());
    }

    // Test case for parse(File in, String charsetName, String baseUri) method
    @Test
    public void testParseFileWithBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertTrue(document.body().text().equals("Hello World!"));
    }

    // Test case for parse(File in, String charsetName) method
    @Test
    public void testParseFileWithoutBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(file, charsetName);
        
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertTrue(document.body().text().equals("Hello World!"));
    }

    // Test case for parse(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertTrue(document.body().text().equals("Hello World!"));
    }

    // Test case for parse(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertTrue(document.body().text().equals("Hello World!"));
    }

    // Test case for parseBodyFragment(String bodyHtml, String baseUri) method
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertTrue(document.body().text().equals("Hello World!"));
    }

    // Test case for parseBodyFragment(String bodyHtml) method
    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        
        assertNotNull(document);
        assertTrue(document.body().text().equals("Hello World!"));
    }

    // Test case for parse(URL url, int timeoutMillis) method
    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;
        Document document = Jsoup.parse(url, timeoutMillis);
        
        assertNotNull(document);
        assertEquals("Example Domain", document.title());
    }

    // Test case for clean(String bodyHtml, String baseUri, Whitelist whitelist) method
    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<h1><script>alert('XSS');</script></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        assertNotNull(cleanedHtml);
        assertFalse(cleanedHtml.contains("<script>"));
    }

    // Test case for clean(String bodyHtml, Whitelist whitelist) method
    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<h1><script>alert('XSS');</script></h1>";
        Whitelist whitelist = Whitelist.basic();
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
        
        assertNotNull(cleanedHtml);
        assertFalse(cleanedHtml.contains("<script>"));
    }

    // Test case for clean(String bodyHtml, String baseUri, Whitelist whitelist, Document.OutputSettings outputSettings) method
    @Test
    public void testCleanWithSettings() {
        String bodyHtml = "<h1><script>alert('XSS');</script></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        
        assertNotNull(cleanedHtml);
        assertFalse(cleanedHtml.contains("<script>"));
    }

    // Test case for isValid(String bodyHtml, Whitelist whitelist) method
    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertTrue(isValid);
    }
}