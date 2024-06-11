package org.jsoup;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {
    @Test
    public void testParseStringString() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(html, doc.html());
    }
    
    @Test
    public void testParseStringStringParser() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertNotNull(doc);
        assertEquals(html, doc.html());
    }
    
    @Test
    public void testParseString() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals(html, doc.html());
    }
    
    @Test
    public void testConnect() {
        String url = "http://example.com";
        
        Connection con = Jsoup.connect(url);
        assertNotNull(con);
        assertEquals(url, con.request().url().toString());
    }
    
    @Test
    public void testParseFileStringString() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("", doc.html());
    }
    
    @Test
    public void testParseFileString() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        
        Document doc = Jsoup.parse(file, charsetName);
        assertNotNull(doc);
        assertEquals("", doc.html());
    }
    
    @Test
    public void testParseInputStreamStringString() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("", doc.html());
    }
    
    @Test
    public void testParseInputStreamStringStringParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = Jsoup.parse(in, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("", doc.html());
    }
    
    @Test
    public void testParseBodyFragmentStringString() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals(bodyHtml, doc.body().html());
    }
    
    @Test
    public void testParseBodyFragmentString() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals(bodyHtml, doc.body().html());
    }
    
    @Test
    public void testCleanStringStringWhitelist() {
        String bodyHtml = "<b><i>Hello, World!</i></b>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals("<b><i>Hello, World!</i></b>", cleanHtml);
    }
    
    @Test
    public void testCleanStringWhitelist() {
        String bodyHtml = "<b><i>Hello, World!</i></b>";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("<b><i>Hello, World!</i></b>", cleanHtml);
    }
    
    @Test
    public void testCleanStringStringWhitelistOutputSettings() {
        String bodyHtml = "<b><i>Hello, World!</i></b>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("<b><i>Hello, World!</i></b>", cleanHtml);
    }
    
    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }
}