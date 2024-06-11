package org.jsoup;
import org.junit.Test;
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

import static org.junit.Assert.*;

public class JsoupTest {

    // Test for parse(String html, String baseUri) method

    @Test
    public void testParse_withHtmlAndBaseUri_shouldReturnDocument() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals(1, document.getElementsByTag("h1").size());
    }

    // Test for parse(String html, String baseUri, Parser parser) method

    @Test
    public void testParse_withHtmlBaseUriAndParser_shouldReturnDocument() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = Jsoup.parse(html, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(1, document.getElementsByTag("h1").size());
    }
    
    // Test for parse(String html) method
    
    @Test
    public void testParse_withHtml_shouldReturnDocument() {
        String html = "<html><body><h1>Test</h1></body></html>";
        
        Document document = Jsoup.parse(html);
        
        assertNotNull(document);
        assertEquals(1, document.getElementsByTag("h1").size());
    }

    // Test for connect(String url) method

    @Test
    public void testConnect_withValidUrl_shouldReturnConnection() {
        String url = "http://example.com";
        
        Connection connection = Jsoup.connect(url);
        
        assertNotNull(connection);
    }

    // Test for parse(File in, String charsetName, String baseUri) method
    
    @Test
    public void testParse_withFileCharsetNameAndBaseUri_shouldReturnDocument() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parse(file, charsetName, baseUri);
        
        assertNotNull(document);
    }

    // Test for parse(File in, String charsetName) method
    
    @Test
    public void testParse_withFileAndCharsetName_shouldReturnDocument() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        
        Document document = Jsoup.parse(file, charsetName);
        
        assertNotNull(document);
    }

    // Test for parse(InputStream in, String charsetName, String baseUri) method
    
    @Test
    public void testParse_withInputStreamCharsetNameAndBaseUri_shouldReturnDocument() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
    }

    // Test for parse(InputStream in, String charsetName, String baseUri, Parser parser) method
    
    @Test
    public void testParse_withInputStreamCharsetNameBaseUriAndParser_shouldReturnDocument() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = Jsoup.parse(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
    }

    // Test for parseBodyFragment(String bodyHtml, String baseUri) method
    
    @Test
    public void testParseBodyFragment_withBodyHtmlAndBaseUri_shouldReturnDocument() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals(1, document.getElementsByTag("h1").size());
    }

    // Test for parseBodyFragment(String bodyHtml) method
    
    @Test
    public void testParseBodyFragment_withBodyHtml_shouldReturnDocument() {
        String bodyHtml = "<h1>Test</h1>";
        
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        
        assertNotNull(document);
        assertEquals(1, document.getElementsByTag("h1").size());
    }

    // Test for parse(URL url, int timeoutMillis) method
    
    @Test
    public void testParse_withValidUrlAndTimeoutMillis_shouldReturnDocument() throws IOException {
        String url = "http://example.com";
        int timeoutMillis = 5000;
        
        Document document = Jsoup.parse(new URL(url), timeoutMillis);
        
        assertNotNull(document);
    }

    // Test for clean(String bodyHtml, String baseUri, Whitelist whitelist) method
    
    @Test
    public void testClean_withBodyHtmlBaseUriAndWhitelist_shouldReturnCleanedHtml() {
        String bodyHtml = "<h1 onclick=\"alert('xss')\">Test</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        assertEquals("<h1>Test</h1>", cleanHtml);
    }

    // Test for clean(String bodyHtml, Whitelist whitelist) method
    
    @Test
    public void testClean_withBodyHtmlAndWhitelist_shouldReturnCleanedHtml() {
        String bodyHtml = "<h1 onclick=\"alert('xss')\">Test</h1>";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        
        assertEquals("<h1>Test</h1>", cleanHtml);
    }

    // Test for clean(String bodyHtml, String baseUri, Whitelist whitelist, Document.OutputSettings outputSettings) method
    
    @Test
    public void testClean_withBodyHtmlBaseUriWhitelistAndOutputSettings_shouldReturnCleanedHtml() {
        String bodyHtml = "<h1 onclick=\"alert('xss')\">Test</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        
        assertEquals("<h1>Test</h1>", cleanHtml);
    }

    // Test for isValid(String bodyHtml, Whitelist whitelist) method
    
    @Test
    public void testIsValid_withBodyHtmlAndWhitelist_shouldReturnTrue() {
        String bodyHtml = "<h1>Test</h1>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertTrue(isValid);
    }

}