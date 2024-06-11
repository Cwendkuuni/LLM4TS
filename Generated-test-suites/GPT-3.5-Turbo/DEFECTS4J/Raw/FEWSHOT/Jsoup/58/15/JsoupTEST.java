package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello Jsoup</h1></body></html>";
        String baseUri = "https://www.example.com/";
        Document doc = Jsoup.parse(html, baseUri);
        assertEquals("<h1>Hello Jsoup</h1>", doc.body().html());
        assertEquals("https://www.example.com/", doc.baseUri());
        
        Parser parser = Parser.xmlParser();
        doc = Jsoup.parse(html, baseUri, parser);
        assertEquals("<h1>Hello Jsoup</h1>", doc.body().html());
        assertEquals("https://www.example.com/", doc.baseUri());
    }
    
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><p>Hello Jsoup</p></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("<p>Hello Jsoup</p>", doc.body().html());
        
        doc = Jsoup.parseBodyFragment(html);
        assertEquals("<p>Hello Jsoup</p>", doc.body().html());
    }
    
    @Test
    public void testConnect() {
        try {
            Connection con = Jsoup.connect("http://example.com").userAgent("Mozilla").data("name", "jsoup");
            Document doc = con.get();
            assertEquals("http://example.com", doc.location());
            
            con = Jsoup.connect("http://example.com").cookie("auth", "token");
            doc = con.post();
            assertEquals("http://example.com", doc.location());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testParseFile() {
        File file = new File("example.html");
        try {
            Document doc = Jsoup.parse(file, "UTF-8", "https://www.example.com/");
            assertEquals("https://www.example.com/", doc.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
        
        try {
            Document doc = Jsoup.parse(file, "UTF-8");
            assertEquals(file.getAbsolutePath(), doc.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testParseInputStream() {
        InputStream in = DataUtil.load("example.html");
        try {
            Document doc = Jsoup.parse(in, "UTF-8", "https://www.example.com/");
            assertEquals("https://www.example.com/", doc.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
        
        in = DataUtil.load("example.html");
        try {
            Document doc = Jsoup.parse(in, "UTF-8", "https://www.example.com/", Parser.xmlParser());
            assertEquals("https://www.example.com/", doc.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello Jsoup</h1>";
        String baseUri = "https://www.example.com/";
        Document doc = Jsoup.parseBodyFragment(html, baseUri);
        assertEquals("<h1>Hello Jsoup</h1>", doc.body().html());
        assertEquals("https://www.example.com/", doc.baseUri());
    }
    
    @Test
    public void testClean() {
        String html = "<h1>Hello<world><script>alert('hello');</script> Jsoup</h1>";
        String baseUri = "https://www.example.com/";
        Whitelist whitelist = Whitelist.basicWithImages();
        String cleanHtml = Jsoup.clean(html, baseUri, whitelist);
        assertEquals("<h1>Hello Jsoup</h1>", cleanHtml);
        
        cleanHtml = Jsoup.clean(html, whitelist);
        assertEquals("<h1>Hello Jsoup</h1>", cleanHtml);
        
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.xhtml);
        
        cleanHtml = Jsoup.clean(html, baseUri, whitelist, outputSettings);
        assertEquals("<h1>Hello Jsoup</h1>", cleanHtml);
    }
    
    @Test
    public void testIsValid() {
        String html = "<h1>Hello Jsoup</h1>";
        Whitelist whitelist = Whitelist.basic();
        assertTrue(Jsoup.isValid(html, whitelist));
        
        html = "<script>alert('hello');</script>";
        assertFalse(Jsoup.isValid(html, whitelist));
    }
    
}