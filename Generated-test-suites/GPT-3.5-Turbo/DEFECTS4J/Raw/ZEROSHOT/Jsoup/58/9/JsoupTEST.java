import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
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
        Parser parser = Parser.xmlParser();
        
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
    public void testConnect() throws IOException {
        String url = "http://example.com";
        
        Connection connection = Jsoup.connect(url);
        
        assertNotNull(connection);
        assertEquals(url, connection.url());
    }
    
    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parse(file, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        
        Document document = Jsoup.parse(file, charsetName);
        
        assertNotNull(document);
        assertEquals(file.getAbsolutePath(), document.baseUri());
    }
    
    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        
        inputStream.close();
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testParseInputStreamWithCharsetName() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        
        Document document = Jsoup.parse(inputStream, charsetName);
        
        inputStream.close();
        
        assertNotNull(document);
        assertEquals("", document.baseUri());
    }
    
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals(bodyHtml, document.body().html());
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        
        assertNotNull(document);
        assertEquals(bodyHtml, document.body().html());
        assertEquals("", document.baseUri());
    }
    
    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<h1>Hello World!</h1><script>alert('XSS')</script>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        assertEquals("<h1>Hello World!</h1>", cleanedHtml);
    }
    
    @Test
    public void testCleanWithWhitelistAndOutputSettings() {
        String bodyHtml = "<h1>Hello World!</h1><script>alert('XSS')</script>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        
        assertEquals("<h1>Hello World!</h1>", cleanedHtml);
    }
    
    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<h1>Hello World!</h1><script>alert('XSS')</script>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean valid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertFalse(valid);
    }
    
}