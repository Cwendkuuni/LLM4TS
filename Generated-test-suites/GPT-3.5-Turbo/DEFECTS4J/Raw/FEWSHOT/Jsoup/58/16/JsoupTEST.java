package org.jsoup;

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class JsoupTest {
    @Test
    public void testParse() {
        // Test HTML parsing with base URI
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());

        // Test HTML parsing with base URI and custom parser
        String baseUri1 = "http://www.example.com";
        Parser parser = Parser.xmlParser();
        Document document1 = Jsoup.parse(html, baseUri1, parser);
        assertEquals(html, document1.html());
        assertEquals(baseUri1, document1.baseUri());

        // Test HTML parsing without base URI
        String html2 = "<html><body><h1>Hello World!</h1></body></html>";
        Document document2 = Jsoup.parse(html2);
        assertEquals(html2, document2.html());
        assertEquals("", document2.baseUri());
    }
    
    @Test
    public void testConnect() {
        // Test connecting to a URL and getting the document
        String url = "https://www.example.com";
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
        assertTrue(document instanceof Document);
    }
    
    @Test
    public void testParseFile() throws IOException {
        // Test parsing HTML from a file with charsetName and baseUri
        File file = new File("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        assertEquals(file.getAbsolutePath(), document.baseUri());
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        // Test parsing HTML from an input stream with charsetName and baseUri
        InputStream inputStream = new FileInputStream("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testParseBodyFragment() {
        // Test parsing HTML fragment with baseUri
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://www.example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(bodyHtml, document.body().html());
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        // Test parsing HTML fragment without baseUri
        String bodyHtml = "<h1>Hello World!</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals(bodyHtml, document.body().html());
        assertEquals("", document.baseUri());
    }
    
    @Test
    public void testClean() {
        // Test cleaning untrusted input HTML with whitelist and baseUri
        String bodyHtml = "<script>alert('XSS')</script><p>Hello World!</p>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals("<p>Hello World!</p>", cleanHtml);
    }
    
    @Test
    public void testCleanWithoutBaseUri() {
        // Test cleaning untrusted input HTML with whitelist without baseUri
        String bodyHtml = "<script>alert('XSS')</script><p>Hello World!</p>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("<p>Hello World!</p>", cleanHtml);
    }
    
    @Test
    public void testCleanWithOutputSettings() {
        // Test cleaning untrusted input HTML with whitelist and baseUri and output settings
        String bodyHtml = "<script>alert('XSS')</script><p>Hello World!</p>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("<p>Hello World!</p>", cleanHtml);
    }
    
    @Test
    public void testIsValid() {
        // Test validation of HTML with whitelist
        String bodyHtml = "<p>Hello World!</p>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }
    
    @Test
    public void testIsValidWithInvalidHtml() {
        // Test validation of invalid HTML with whitelist
        String bodyHtml = "<script>alert('XSS')</script><p>Hello World!</p>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }
}