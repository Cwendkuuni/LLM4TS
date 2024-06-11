package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        TreeBuilder builder = new TreeBuilder();

        builder.initialiseParse(input, baseUri, null);

        assertNotNull(builder.doc);
        assertNotNull(builder.reader);
        assertNotNull(builder.tokeniser);
        assertNotNull(builder.stack);
        assertEquals(baseUri, builder.baseUri);
    }

    @Test
    public void testParse() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        TreeBuilder builder = new TreeBuilder();

        Document doc = builder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals("h1", doc.body().child(0).tagName());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testProcess() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        TreeBuilder builder = new TreeBuilder();

        builder.initialiseParse(input, baseUri, null);
        
        Token token = new Token.StartTag();
        token.reset().name("h1");

        boolean result = builder.process(token);
        
        assertTrue(result);
        assertNotNull(builder.currentElement());
        assertEquals("h1", builder.currentElement().tagName());
        assertEquals("Hello World", builder.currentElement().text());
    }

    @Test
    public void testProcessStartTag() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        TreeBuilder builder = new TreeBuilder();

        builder.initialiseParse(input, baseUri, null);

        boolean result = builder.processStartTag("h1");

        assertTrue(result);
        assertNotNull(builder.currentElement());
        assertEquals("h1", builder.currentElement().tagName());
        assertEquals("Hello World", builder.currentElement().text());
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        String input = "<html><body><a href='http://example.com'>Link</a></body></html>";
        String baseUri = "http://example.com";
        TreeBuilder builder = new TreeBuilder();

        builder.initialiseParse(input, baseUri, null);

        boolean result = builder.processStartTag("a", new Attributes());
        
        assertTrue(result);
        assertNotNull(builder.currentElement());
        assertEquals("a", builder.currentElement().tagName());
        assertEquals("http://example.com", builder.currentElement().attr("href"));
        assertEquals("Link", builder.currentElement().text());
    }

    @Test
    public void testProcessEndTag() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        TreeBuilder builder = new TreeBuilder();

        builder.initialiseParse(input, baseUri, null);

        boolean result = builder.processEndTag("h1");

        assertTrue(result);
        assertNull(builder.currentElement());
    }

    @Test
    public void testCurrentElement() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        TreeBuilder builder = new TreeBuilder();

        builder.initialiseParse(input, baseUri, null);

        assertNull(builder.currentElement());
        
        builder.processStartTag("html");
        
        assertNotNull(builder.currentElement());
        assertEquals("html", builder.currentElement().tagName());
        
        builder.processStartTag("body");
        
        assertNotNull(builder.currentElement());
        assertEquals("body", builder.currentElement().tagName());
    }
}