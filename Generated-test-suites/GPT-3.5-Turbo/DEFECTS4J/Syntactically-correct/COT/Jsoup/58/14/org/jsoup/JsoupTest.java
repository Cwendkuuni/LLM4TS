import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class JsoupTest {
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Document document = Jsoup.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals("Test", document.select("h1").text());
    }
    
    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Document document = Jsoup.parse(html, baseUri, Parser.htmlParser());
        
        assertNotNull(document);
        assertEquals("Test", document.select("h1").text());
    }
    
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Test</h1></body></html>";
        
        Document document = Jsoup.parse(html);
        
        assertNotNull(document);
        assertEquals("Test", document.select("h1").text());
    }
    
    @Test
    public void testConnect() {
        String url = "http://www.example.com";
        
        Connection connection = Jsoup.connect(url);
        
        assertNotNull(connection);
    }
    
    @Test
    public void testParseFileWithCharsetAndBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        Document document = Jsoup.parse(file, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("Test", document.select("h1").text());
    }
    
    @Test
    public void testParseFileWithCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        
        Document document = Jsoup.parse(file, charsetName);
        
        assertNotNull(document);
        assertEquals("Test", document.select("h1").text());
    }
    
    @Test
    public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        Document document = Jsoup.parse(in, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("Test", document.select("h1").text());
    }
    
    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        
        Document document = Jsoup.parse(in, charsetName);
        
        assertNotNull(document);
        assertEquals("Test", document.select("h1").text());
    }
    
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://www.example.com";
        
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("Test", document.select("h1").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test</h1>";
        
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        
        assertNotNull(document);
        assertEquals("Test", document.select("h1").text());
    }
    
    @Test
    public void testParseURLWithTimeout() throws IOException {
        URL url = new URL("http://www.example.com");
        int timeoutMillis = 5000;
        
        Document document = Jsoup.parse(url, timeoutMillis);
        
        assertNotNull(document);
        assertEquals("Test", document.select("h1").text());
    }
    
    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<h1>Test</h1><script>alert('XSS')</script>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        assertNotNull(cleanHtml);
        assertEquals("<h1>Test</h1>", cleanHtml);
    }
    
    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<h1>Test</h1><script>alert('XSS')</script>";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        
        assertNotNull(cleanHtml);
        assertEquals("<h1>Test</h1>", cleanHtml);
    }
    
    @Test
    public void testCleanWithBaseUriWhitelistAndOutputSettings() {
        String bodyHtml = "<h1>Test</h1><script>alert('XSS')</script>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        
        assertNotNull(cleanHtml);
        assertEquals("<h1>Test</h1>", cleanHtml);
    }
    
    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<h1>Test</h1>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertTrue(isValid);
    }
    
    @Test
    public void testIsValidWithInvalidHtml() {
        String bodyHtml = "<h1>Test</h1><script>alert('XSS')</script>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertFalse(isValid);
    }
}