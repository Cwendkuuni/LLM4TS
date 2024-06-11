package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.Test;

public class TreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        TreeBuilder treeBuilder = new TreeBuilderMock();
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertSame(errors, treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>";
        String baseUri = "https://example.com";
        
        TreeBuilder treeBuilder = new TreeBuilderMock();
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertEquals(baseUri, doc.baseUri());
        assertEquals(1, doc.children().size());
        assertEquals("html", doc.child(0).tagName());
        
        Element html = doc.child(0);
        assertEquals(2, html.children().size());
        assertEquals("head", html.child(0).tagName());
        assertEquals("body", html.child(1).tagName());
        
        Element head = html.child(0);
        assertEquals(1, head.children().size());
        assertEquals("title", head.child(0).tagName());
        assertEquals("Title", head.child(0).ownText());
        
        Element body = html.child(1);
        assertEquals(1, body.children().size());
        assertEquals("h1", body.child(0).tagName());
        assertEquals("Heading", body.child(0).ownText());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilderMock();
        boolean result = treeBuilder.processStartTag("div");
        
        assertTrue(result);
        assertNotNull(treeBuilder.currentElement());
        assertEquals("div", treeBuilder.currentElement().tagName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilderMock();
        Attributes attrs = new Attributes();
        attrs.put("id", "mydiv");
        
        boolean result = treeBuilder.processStartTag("div", attrs);
        
        assertTrue(result);
        assertNotNull(treeBuilder.currentElement());
        assertEquals("div", treeBuilder.currentElement().tagName());
        assertEquals("mydiv", treeBuilder.currentElement().attr("id"));
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilderMock();
        boolean result = treeBuilder.processEndTag("div");
        
        assertTrue(result);
        assertNull(treeBuilder.currentElement());
    }

    private static class TreeBuilderMock extends TreeBuilder {
        protected boolean process(Token token) {
            // Mock implementation
            return false;
        }
    }
}