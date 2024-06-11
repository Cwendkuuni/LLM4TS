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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsoupTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertEquals(html, doc.html());
    }
    
    @Test
    public void testParseWithParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertEquals(html, doc.html());
    }
    
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals(html, doc.html());
    }
    
    @Test
    public void testConnect() {
        String url = "https://www.example.com";
        Connection con = Jsoup.connect(url);
        assertEquals(url, con.request().url().toString());
    }
    
    @Test
    public void testParseFileWithCharsetAndBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        assertTrue(file.exists());
        assertEquals("", doc.html());
    }
    
    @Test
    public void testParseFileWithCharsetNoBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document doc = Jsoup.parse(file, charsetName);
        assertTrue(file.exists());
        assertEquals("", doc.html());
    }
    
    @Test
    public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parse(inputStream, charsetName, baseUri);
        assertTrue(inputStream != null);
        assertEquals("", doc.html());
    }
    
    @Test
    public void testParseInputStreamWithCharsetNoBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        Document doc = Jsoup.parse(inputStream, charsetName);
        assertTrue(inputStream != null);
        assertEquals("", doc.html());
    }
    
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(bodyHtml, doc.body().html());
    }
    
    @Test
    public void testParseBodyFragmentNoBaseUri() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals(bodyHtml, doc.body().html());
    }
    
    @Test
    public void testParseUrlWithTimeout() throws IOException {
        URL url = new URL("https://www.example.com");
        int timeoutMillis = 5000;
        Document doc = Jsoup.parse(url, timeoutMillis);
        assertEquals("", doc.html());
    }
    
    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals(bodyHtml, cleanHtml);
    }
    
    @Test
    public void testCleanNoBaseUriAndWhitelist() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals(bodyHtml, cleanHtml);
    }
    
    @Test
    public void testCleanWithBaseUriWhitelistAndOutputSettings() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals(bodyHtml, cleanHtml);
    }
    
    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }
}