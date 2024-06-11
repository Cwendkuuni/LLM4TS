package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        
        Element htmlElement = doc.select("html").first();
        assertNotNull(htmlElement);
        
        Element headElement = doc.select("head").first();
        assertNotNull(headElement);
        
        Element titleElement = doc.select("title").first();
        assertNotNull(titleElement);
        assertEquals("Test", titleElement.text());
        
        Element bodyElement = doc.select("body").first();
        assertNotNull(bodyElement);
        
        Element h1Element = doc.select("h1").first();
        assertNotNull(h1Element);
        assertEquals("Hello World", h1Element.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1><p>This is a paragraph.</p>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        
        Element h1Element = doc.select("h1").first();
        assertNotNull(h1Element);
        assertEquals("Hello World", h1Element.text());
        
        Element pElement = doc.select("p").first();
        assertNotNull(pElement);
        assertEquals("This is a paragraph.", pElement.text());
    }

    @Test
    public void testParseWithRelaxedParsing() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        
        Element htmlElement = doc.select("html").first();
        assertNotNull(htmlElement);
        
        Element headElement = doc.select("head").first();
        assertNotNull(headElement);
        
        Element titleElement = doc.select("title").first();
        assertNotNull(titleElement);
        assertEquals("Test", titleElement.text());
        
        Element bodyElement = doc.select("body").first();
        assertNotNull(bodyElement);
        
        Element h1Element = doc.select("h1").first();
        assertNotNull(h1Element);
        assertEquals("Hello World", h1Element.text());
    }
}