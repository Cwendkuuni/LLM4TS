package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // Stub implementation for testing purposes
                return false;
            }
        };
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = null;

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNotNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";

        Document doc = treeBuilder.parse(input, baseUri);

        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", doc.outerHtml());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html><";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        Document doc = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", doc.outerHtml());
        assertEquals(1, errors.size());
    }

    @Test
    public void testProcessStartTag() {
        String name = "div";

        boolean result = treeBuilder.processStartTag(name);

        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        String name = "div";
        Attributes attrs = new Attributes();

        boolean result = treeBuilder.processStartTag(name, attrs);

        assertFalse(result);
    }

    @Test
    public void testProcessEndTag() {
        String name = "div";

        boolean result = treeBuilder.processEndTag(name);

        assertFalse(result);
    }

    @Test
    public void testCurrentElement() {
        Element element = treeBuilder.currentElement();

        assertNull(element);

        // Add an element to the stack
        treeBuilder.stack.add(new Element("div"));

        element = treeBuilder.currentElement();

        assertNotNull(element);
        assertEquals("div", element.tagName());
    }
}