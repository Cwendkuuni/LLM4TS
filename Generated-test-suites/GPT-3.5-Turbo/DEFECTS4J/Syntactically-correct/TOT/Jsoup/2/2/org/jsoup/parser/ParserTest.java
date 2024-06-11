import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParse() {
        // Typical use case: parsing HTML into a Document
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document result = Parser.parse(html, baseUri);
        
        assertNotNull(result);
        assertEquals("http://example.com", result.baseUri());
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", result.outerHtml());
    }
    
    @Test
    public void testParseEmptyHtml() {
        // Edge case: parsing empty HTML into a Document
        String html = "";
        String baseUri = "http://example.com";
        
        Document result = Parser.parse(html, baseUri);
        
        assertNotNull(result);
        assertEquals("http://example.com", result.baseUri());
        assertEquals("<html><head></head><body></body></html>", result.outerHtml());
    }
    
    @Test
    public void testParseNullHtml() {
        // Error scenario: parsing null HTML into a Document
        String html = null;
        String baseUri = "http://example.com";
        
        Document result = Parser.parse(html, baseUri);
        
        assertNull(result);
    }
    
    @Test
    public void testParseBodyFragment() {
        // Typical use case: parsing a fragment of HTML into the body of a Document
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        
        Document result = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(result);
        assertEquals("http://example.com", result.baseUri());
        assertEquals("<head></head><body><h1>Hello World</h1></body>", result.outerHtml());
    }
    
    @Test
    public void testParseBodyFragmentEmptyBodyHtml() {
        // Edge case: parsing an empty fragment of HTML into the body of a Document
        String bodyHtml = "";
        String baseUri = "http://example.com";
        
        Document result = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(result);
        assertEquals("http://example.com", result.baseUri());
        assertEquals("<head></head><body></body>", result.outerHtml());
    }
    
    @Test
    public void testParseBodyFragmentNullBodyHtml() {
        // Error scenario: parsing a null fragment of HTML into the body of a Document
        String bodyHtml = null;
        String baseUri = "http://example.com";
        
        Document result = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNull(result);
    }
}