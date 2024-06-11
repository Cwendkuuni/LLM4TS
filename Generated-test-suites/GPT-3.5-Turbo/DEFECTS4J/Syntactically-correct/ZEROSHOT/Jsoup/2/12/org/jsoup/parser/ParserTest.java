package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        // Check Document properties
        assertEquals(baseUri, doc.baseUri());
        assertEquals("Test", doc.title());
        
        // Check body content
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("Hello World", body.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        // Check Document properties
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.title());
        
        // Check body content
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("Hello World", body.text());
    }
    
    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals(1, doc.childNodes().size());
        
        Node child = doc.childNode(0);
        assertTrue(child instanceof Comment);
        
        Comment comment = (Comment) child;
        assertEquals(" This is a comment ", comment.getData());
        assertEquals(baseUri, comment.baseUri());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals(1, doc.childNodes().size());
        
        Node child = doc.childNode(0);
        assertTrue(child instanceof TextNode);
        
        TextNode textNode = (TextNode) child;
        assertEquals("This is CDATA", textNode.getWholeText());
        assertEquals(baseUri, textNode.baseUri());
    }
    
    // Add tests for other methods in the Parser class
    
}