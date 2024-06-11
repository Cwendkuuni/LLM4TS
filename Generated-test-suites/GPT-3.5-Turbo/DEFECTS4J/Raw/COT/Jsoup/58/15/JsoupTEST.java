import org.junit.*;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello, World!", doc.getElementsByTag("h1").first().text());
    }
    
    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello, World!", doc.getElementsByTag("h1").first().text());
    }
    
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Document doc = Jsoup.parse(html);
        
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello, World!", doc.getElementsByTag("h1").first().text());
    }
    
    @Test
    public void testConnect() {
        String url = "http://www.example.com";
        Connection connection = Jsoup.connect(url);
        
        assertNotNull(connection);
        assertEquals(url, connection.request().url().toString());
    }
    
    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello, World!", doc.getElementsByTag("h1").first().text());
    }
    
    @Test
    public void testParseFileWithoutBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document doc = Jsoup.parse(file, charsetName);
        
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello, World!", doc.getElementsByTag("h1").first().text());
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello, World!", doc.getElementsByTag("h1").first().text());
        
        in.close();
    }
    
    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(in, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello, World!", doc.getElementsByTag("h1").first().text());
        
        in.close();
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<span>Hello, World!</span>";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("span").size());
        assertEquals("Hello, World!", doc.getElementsByTag("span").first().text());
    }
    
    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<span>Hello, World!</span>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("span").size());
        assertEquals("Hello, World!", doc.getElementsByTag("span").first().text());
    }
    
    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://www.example.com");
        int timeoutMillis = 5000;
        Document doc = Jsoup.parse(url, timeoutMillis);
        
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello, World!", doc.getElementsByTag("h1").first().text());
    }
    
    @Test
    public void testClean() {
        String bodyHtml = "<script>alert('Hello, World!');</script>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        assertEquals("", cleanHtml);
    }
    
    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<script>alert('Hello, World!');</script>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        
        assertEquals("", cleanHtml);
    }
    
    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<span>Hello, World!</span>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        
        assertEquals("<span>Hello, World!</span>", cleanHtml);
    }
    
    @Test
    public void testIsValid() {
        String bodyHtml = "<span>Hello, World!</span>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertTrue(isValid);
    }
    
    // TODO: Add test cases for potential edge cases and exception handling scenarios
    
}