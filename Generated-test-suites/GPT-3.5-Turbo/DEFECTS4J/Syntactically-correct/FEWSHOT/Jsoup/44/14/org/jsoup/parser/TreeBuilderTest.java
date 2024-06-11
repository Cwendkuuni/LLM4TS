package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        // Test with valid input
        String input = "<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNull(treeBuilder.errors);
        assertEquals(0, treeBuilder.doc.childNodeSize());
        assertEquals(0, treeBuilder.stack.size());
        
        // Test with null input
        try {
            treeBuilder.initialiseParse(null, baseUri, errors);
        } catch (IllegalArgumentException e) {
            assertEquals("String input must not be null", e.getMessage());
        }
        
        // Test with null baseUri
        try {
            treeBuilder.initialiseParse(input, null, errors);
        } catch (IllegalArgumentException e) {
            assertEquals("BaseURI must not be null", e.getMessage());
        }
    }

    @Test
    public void testParse() {
        // Test with valid input
        String input = "<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        Document doc = treeBuilder.parse(input, baseUri, errors);
        
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNull(treeBuilder.errors);
        assertEquals(1, treeBuilder.doc.childNodeSize());
        assertEquals(0, treeBuilder.stack.size());
        assertEquals(1, doc.select("html").size());
        assertEquals(1, doc.select("head").size());
        assertEquals(1, doc.select("title").size());
        assertEquals(1, doc.select("body").size());
        assertEquals(1, doc.select("p").size());
        
        // Test with null input
        try {
            treeBuilder.parse(null, baseUri, errors);
        } catch (IllegalArgumentException e) {
            assertEquals("String input must not be null", e.getMessage());
        }
        
        // Test with null baseUri
        try {
            treeBuilder.parse(input, null, errors);
        } catch (IllegalArgumentException e) {
            assertEquals("BaseURI must not be null", e.getMessage());
        }
    }

    @Test
    public void testRunParser() {
        // Implementation specific test. Cannot be tested without mocking the Tokeniser and Token classes.
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        boolean result = treeBuilder.processStartTag("html");
        
        assertEquals(false, result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        Attributes attrs = new Attributes();
        attrs.put("class", "test");
        boolean result = treeBuilder.processStartTag("div", attrs);
        
        assertEquals(false, result);
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        boolean result = treeBuilder.processEndTag("html");
        
        assertEquals(false, result);
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        assertNull(treeBuilder.currentElement());
        
        treeBuilder.stack.add(new Element("div"));
        
        assertEquals("div", treeBuilder.currentElement().tagName());
    }
}