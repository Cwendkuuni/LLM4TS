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
import static org.junit.Assert.*;

public class JsoupTest {
    
    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Jsoup.parse(html, baseUri);
        
        assertEquals("Test", document.head().select("title").text());
        assertEquals("Hello World!", document.body().select("h1").text());
    }
    
    @Test
    public void testParseWithParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Jsoup.parse(html, baseUri, Parser.htmlParser());
        
        assertEquals("Test", document.head().select("title").text());
        assertEquals("Hello World!", document.body().select("h1").text());
    }
    
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document document = Jsoup.parse(html);
        
        assertEquals("Test", document.head().select("title").text());
        assertEquals("Hello World!", document.body().select("h1").text());
    }
    
    @Test
    public void testConnect() throws IOException {
        String url = "https://www.example.com";
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
        
        Document document = connection.get();
        assertNotNull(document);
        assertTrue(connection.response().statusCode() == 200);
    }
    
    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "UTF-8", "https://www.example.com");
        
        assertNotNull(document);
        assertEquals("Test", document.head().select("title").text());
    }
    
    @Test
    public void testParseFromFileWithoutCharset() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, null, "https://www.example.com");
        
        assertNotNull(document);
        assertEquals("Test", document.head().select("title").text());
    }
    
    @Test
    public void testParseFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "https://www.example.com");
        
        assertNotNull(document);
        assertEquals("Test", document.head().select("title").text());
    }
    
    @Test
    public void testParseFromInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "https://www.example.com", Parser.htmlParser());
        
        assertNotNull(document);
        assertEquals("Test", document.head().select("title").text());
    }
    
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("Hello World!", document.body().select("h1").text());
    }
    
    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        
        assertEquals("Hello World!", document.body().select("h1").text());
    }
    
    @Test
    public void testParseUrlWithTimeout() throws IOException {
        URL url = new URL("https://www.example.com");
        int timeoutMillis = 5000;
        Document document = Jsoup.parse(url, timeoutMillis);
        
        assertNotNull(document);
        assertEquals("Test", document.head().select("title").text());
    }
    
    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<p><a href='https://www.example.com'>Hello World!</a></p>";
        String baseUri = "https://www.example.com";
        String expectedOutput = "<p><a href=\"https://www.example.com\">Hello World!</a></p>";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        assertEquals(expectedOutput, cleanHtml);
    }
    
    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<p><a href='https://www.example.com'>Hello World!</a></p>";
        String expectedOutput = "<p><a href=\"https://www.example.com\">Hello World!</a></p>";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        
        assertEquals(expectedOutput, cleanHtml);
    }
    
    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<p><a href='https://www.example.com'>Hello World!</a></p>";
        String baseUri = "https://www.example.com";
        String expectedOutput = "<p><a href=\"https://www.example.com\">Hello World!</a></p>";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        
        assertEquals(expectedOutput, cleanHtml);
    }
    
    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<p><a href='https://www.example.com'>Hello World!</a></p>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertTrue(isValid);
    }
}