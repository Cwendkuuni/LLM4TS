package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        
        Element head = doc.head();
        assertNotNull(head);
        
        Element body = doc.body();
        assertNotNull(body);
        
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Test", h1.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.head());
        
        Element body = doc.body();
        assertNotNull(body);
        
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Test", h1.text());
    }
    
    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<div>Test</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.head());
        
        Element body = doc.body();
        assertNotNull(body);
        
        Element div = body.getElementsByTag("div").first();
        assertNotNull(div);
        assertEquals("Test", div.text());
    }
    
    @Test
    public void testParseWithInvalidHtml() {
        String html = "<html><head></head><body><h1>Test</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        
        Element head = doc.head();
        assertNotNull(head);
        
        Element body = doc.body();
        assertNotNull(body);
        
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Test", h1.text());
    }

}