package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.*;
import org.apache.commons.lang.Validate;

import java.util.*;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document result = Parser.parse(html, baseUri);
        
        assertNotNull(result);
        assertEquals(html, result.html());
        assertEquals(baseUri, result.baseUri());
        
        Element htmlElement = result.child(0);
        Element headElement = htmlElement.child(0);
        Element titleElement = headElement.child(0);
        Element bodyElement = htmlElement.child(1);
        Element h1Element = bodyElement.child(0);
        
        assertEquals("html", htmlElement.tagName());
        assertEquals("head", headElement.tagName());
        assertEquals("title", titleElement.tagName());
        assertEquals("body", bodyElement.tagName());
        assertEquals("h1", h1Element.tagName());
        
        assertEquals("Test Page", titleElement.text());
        assertEquals("Hello, World!", h1Element.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        Document result = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(result);
        assertEquals(bodyHtml, result.body().html());
        assertEquals(baseUri, result.baseUri());
        
        Element bodyElement = result.body();
        Element h1Element = bodyElement.child(0);
        
        assertEquals("h1", h1Element.tagName());
        assertEquals("Hello, World!", h1Element.text());
    }
    
    @Test
    public void testParseComment() {
        Parser parser = new Parser("", "", false);
        parser.parseComment();
        
        // TODO: Add assertions
    }
    
    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("", "", false);
        parser.parseXmlDecl();
        
        // TODO: Add assertions
    }
    
    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("", "", false);
        parser.parseEndTag();
        
        // TODO: Add assertions
    }
    
    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("", "", false);
        parser.parseStartTag();
        
        // TODO: Add assertions
    }
    
    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("", "", false);
        Attribute result = parser.parseAttribute();
        
        assertNull(result);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("", "", false);
        parser.parseTextNode();
        
        // TODO: Add assertions
    }
    
    @Test
    public void testParseCdata() {
        Parser parser = new Parser("", "", false);
        parser.parseCdata();
        
        // TODO: Add assertions
    }
    
    @Test
    public void testAddChildToParent() {
        Parser parser = new Parser("", "", false);
        Element result = parser.addChildToParent(null, false);
        
        assertNull(result);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "", false);
        boolean result = parser.stackHasValidParent(null);
        
        assertFalse(result);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "", false);
        Element result = parser.popStackToSuitableContainer(null);
        
        assertNull(result);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "", false);
        Element result = parser.popStackToClose(null);
        
        assertNull(result);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testLast() {
        Parser parser = new Parser("", "", false);
        Element result = parser.last();
        
        assertNull(result);
        
        // TODO: Add assertions
    }

}