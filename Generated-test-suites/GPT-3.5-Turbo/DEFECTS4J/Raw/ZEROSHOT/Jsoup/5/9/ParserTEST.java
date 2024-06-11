package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.head().title());
        assertEquals("Test Page", doc.head().title().text());
        assertNotNull(doc.body());
        assertNotNull(doc.body().getElementsByTag("h1"));
        assertEquals("Hello, World!", doc.body().getElementsByTag("h1").first().text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertNotNull(doc.body().getElementsByTag("h1"));
        assertEquals("Hello, World!", doc.body().getElementsByTag("h1").first().text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertNotNull(doc.body().getElementsByTag("h1"));
        assertEquals("Hello, World!", doc.body().getElementsByTag("h1").first().text());
    }
    
    // Add more test methods for other methods in the Parser class
    // ...
}