package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        TreeBuilder treeBuilder = new TreeBuilderMock();
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.currentToken);
        assertNotNull(treeBuilder.errors);
    }
    
    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        TreeBuilder treeBuilder = new TreeBuilderMock();
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.title());
        assertNotNull(doc.body());
        assertNotNull(doc.body().child(0));
        assertNotNull(doc.body().child(0).tagName());
        assertEquals("h1", doc.body().child(0).tagName());
        assertNotNull(doc.body().child(0).text());
        assertEquals("Hello World!", doc.body().child(0).text());
    }
    
    @Test
    public void testProcessStartTag() {
        String name = "div";
        TreeBuilder treeBuilder = new TreeBuilderMock();
        
        boolean result = treeBuilder.processStartTag(name);
        
        assertTrue(result);
        assertNotNull(treeBuilder.currentElement());
        assertEquals(name, treeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testProcessStartTagWithAttrs() {
        String name = "img";
        Attributes attrs = new Attributes();
        attrs.put("src", "image.png");
        attrs.put("alt", "Image");
        
        TreeBuilder treeBuilder = new TreeBuilderMock();
        
        boolean result = treeBuilder.processStartTag(name, attrs);
        
        assertTrue(result);
        assertNotNull(treeBuilder.currentElement());
        assertEquals(name, treeBuilder.currentElement().tagName());
        assertNotNull(treeBuilder.currentElement().attributes());
        assertEquals("image.png", treeBuilder.currentElement().attr("src"));
        assertEquals("Image", treeBuilder.currentElement().attr("alt"));
    }
    
    @Test
    public void testProcessEndTag() {
        String name = "div";
        TreeBuilder treeBuilder = new TreeBuilderMock();
        
        boolean result = treeBuilder.processEndTag(name);
        
        assertTrue(result);
        assertNull(treeBuilder.currentElement());
    }
    
    private class TreeBuilderMock extends TreeBuilder {
        @Override
        protected boolean process(Token token) {
            // mock implementation
            return true;
        }
    }
}