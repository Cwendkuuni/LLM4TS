import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertNotNull(doc);
    }
    
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
    }
    
    @Test
    public void testConnect() {
        String url = "http://example.com";
        
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
    }
    
    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testParseFileWithoutCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = null;
        
        Document doc = Jsoup.parse(file, charsetName);
        assertNotNull(doc);
    }
    
    @Test
    public void testParseInputStreamWithCharsetName() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testParseInputStreamWithoutCharsetName() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = null;
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<div>Hello, World!</div>";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<div>Hello, World!</div>";
        
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
    }
    
    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 1000;
        
        Document doc = Jsoup.parse(url, timeoutMillis);
        assertNotNull(doc);
    }
    
    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<div>Hello, <script>alert('World!');</script></div>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals("<div>Hello, </div>", cleanHtml);
    }
    
    @Test
    public void testCleanWithWhitelistAndOutputSettings() {
        String bodyHtml = "<div>Hello, <script>alert('World!');</script></div>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("<div>Hello, </div>", cleanHtml);
    }
    
    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<div>Hello, <script>alert('World!');</script></div>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }
    
}