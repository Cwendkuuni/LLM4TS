import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class JsoupTest {
    
    @Before
    public void setup() {
        // setup code
    }
    
    @Test
    public void testParseWithValidHtmlAndBaseUri() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }
    
    @Test
    public void testParseWithValidHtmlAndBaseUriAndParser() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }
    
    @Test
    public void testParseWithValidHtmlWithoutBaseUri() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Document doc = Jsoup.parse(html);
        
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }
    
    @Test
    public void testConnectWithValidUrl() {
        String url = "http://example.com";
        Connection con = Jsoup.connect(url);
        
        assertNotNull(con);
        assertEquals(url, con.request().url());
    }
    
    @Test(expected = IOException.class)
    public void testParseFileWithInvalidFile() throws IOException {
        File file = new File("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(file, charsetName, baseUri);
    }
    
    @Test
    public void testParseFileWithValidFileAndCharsetName() throws IOException {
        File file = new File("valid.html");
        String charsetName = "windows-1252";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }
    
    @Test
    public void testParseInputStreamWithValidInputStreamAndCharsetName() throws IOException {
        InputStream in = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }
    
    @Test
    public void testParseInputStreamWithValidInputStreamAndCharsetNameAndParser() throws IOException {
        InputStream in = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(in, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }
    
    @Test
    public void testParseBodyFragmentWithValidHtmlAndBaseUri() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }
    
    @Test
    public void testParseBodyFragmentWithValidHtmlWithoutBaseUri() {
        String bodyHtml = "<h1>Test</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }
    
    @Test
    public void testParseUrlWithValidUrlAndTimeoutMillis() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;
        Document doc = Jsoup.parse(url, timeoutMillis);
        
        assertNotNull(doc);
        assertEquals(url.toString(), doc.location());
    }
    
    @Test
    public void testCleanWithValidBodyHtmlAndBaseUriAndWhitelist() {
        String bodyHtml = "<script>alert('test')</script><p>Test</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);

        assertFalse(cleanHtml.contains("<script>"));
        assertTrue(cleanHtml.contains("<p>"));
    }
    
    @Test
    public void testCleanWithValidBodyHtmlAndWhitelist() {
        String bodyHtml = "<script>alert('test')</script><p>Test</p>";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);

        assertFalse(cleanHtml.contains("<script>"));
        assertTrue(cleanHtml.contains("<p>"));
    }
    
    @Test
    public void testCleanWithValidBodyHtmlAndBaseUriAndWhitelistAndOutputSettings() {
        String bodyHtml = "<script>alert('test')</script><p>Test</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);

        assertFalse(cleanHtml.contains("<script>"));
        assertTrue(cleanHtml.contains("<p>"));
    }
    
    @Test
    public void testIsValidWithValidBodyHtmlAndWhitelist() {
        String bodyHtml = "<p>Test</p>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertTrue(isValid);
    }
    
    @Test
    public void testIsValidWithInvalidBodyHtmlAndWhitelist() {
        String bodyHtml = "<script>alert('test')</script>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertFalse(isValid);
    }
}