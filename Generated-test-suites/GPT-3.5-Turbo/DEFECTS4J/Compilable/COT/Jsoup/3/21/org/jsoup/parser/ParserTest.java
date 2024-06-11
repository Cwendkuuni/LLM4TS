package org.jsoup.parser;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Hello World", doc.body().text());
    }
    
    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Hello World", doc.body().text());
    }
    
    // Edge cases and exception handling scenarios
    
    @Test
    public void testParseWithEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("", doc.title());
        assertEquals("", doc.body().text());
    }
    
    @Test
    public void testParseWithInvalidBaseUri() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String baseUri = null;
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().text());
    }
    
    @Test
    public void testParseBodyFragmentWithEmptyHtml() {
        String bodyHtml = "";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("", doc.body().text());
    }
    
    @Test
    public void testParseBodyFragmentWithInvalidBaseUri() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = null;
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
        assertEquals("Hello World", doc.body().text());
    }
}