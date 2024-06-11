import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsoupTest {
    
    @org.junit.Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Test</p></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        
        assertEquals("<html><head><title>Test</title></head><body><p>Test</p></body></html>", document.html());
    }
    
    @org.junit.Test
    public void testParseWithParser() {
        String html = "<html><head><title>Test</title></head><body><p>Test</p></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri, Parser.htmlParser());
        
        assertEquals("<html><head><title>Test</title></head><body><p>Test</p></body></html>", document.html());
    }
    
    @org.junit.Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head><title>Test</title></head><body><p>Test</p></body></html>";
        Document document = Jsoup.parse(html);
        
        assertEquals("<html><head><title>Test</title></head><body><p>Test</p></body></html>", document.html());
    }
    
    @org.junit.Test
    public void testConnect() {
        Connection connection = Jsoup.connect("http://example.com");
        
        assertTrue(connection instanceof HttpConnection);
    }
    
    @org.junit.Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        
        assertEquals("Test HTML", document.text());
        
        file.delete();
    }
    
    @org.junit.Test
    public void testParseFileWithoutBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(file, charsetName);
        
        assertEquals("Test HTML", document.text());
        
        file.delete();
    }
    
    @org.junit.Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = DataUtil.load("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        
        assertEquals("Test HTML", document.text());
        
        inputStream.close();
    }
    
    @org.junit.Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream inputStream = DataUtil.load("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri, Parser.htmlParser());
        
        assertEquals("Test HTML", document.text());
        
        inputStream.close();
    }
    
    @org.junit.Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("<body><p>Test</p></body>", document.html());
    }
    
    @org.junit.Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<p>Test</p>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        
        assertEquals("<body><p>Test</p></body>", document.html());
    }
    
    @org.junit.Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;
        Document document = Jsoup.parse(url, timeoutMillis);
        
        assertEquals("Test HTML", document.text());
    }
    
    @org.junit.Test
    public void testClean() {
        String bodyHtml = "<script>alert('XSS')</script>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String safeHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        assertEquals("", safeHtml);
    }
    
    @org.junit.Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<script>alert('XSS')</script>";
        Whitelist whitelist = Whitelist.basic();
        String safeHtml = Jsoup.clean(bodyHtml, whitelist);
        
        assertEquals("", safeHtml);
    }
    
    @org.junit.Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<p>Hello, <b>world</b>!</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String safeHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        
        assertEquals("<body>\n <p>\n  Hello, <b>\n   world\n  </b>!\n </p>\n</body>", safeHtml);
    }
    
    @org.junit.Test
    public void testIsValid() {
        String bodyHtml = "<p>Test</p>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertTrue(isValid);
    }
    
    @org.junit.Test
    public void testIsValidWithInvalidHTML() {
        String bodyHtml = "<p><script>alert('XSS')</script></p>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertFalse(isValid);
    }
}