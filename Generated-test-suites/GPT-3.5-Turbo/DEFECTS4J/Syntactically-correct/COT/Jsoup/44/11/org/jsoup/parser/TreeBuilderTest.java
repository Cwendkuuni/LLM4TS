
package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        // Test that initialiseParse method initializes all the required variables correctly
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        // Instantiate a TreeBuilder object and invoke the initialiseParse method
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        treeBuilder.initialiseParse(input, baseUri, null);
        
        // Check that the variables are initialized correctly
        assertEquals("http://example.com", treeBuilder.baseUri);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNull(treeBuilder.errors);
    }
    
    @Test
    public void testParse() {
        // Test that the parse method correctly parses the input and returns a Document object
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        // Instantiate a TreeBuilder object and invoke the parse method
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        Document document = treeBuilder.parse(input, baseUri, null);
        
        // Check that the parse method correctly parsed the input
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World</h1>\n </body>\n</html>", document.toString());
    }
    
    @Test
    public void testProcessStartTag() {
        // Test that the processStartTag method correctly processes a start tag
        String name = "p";
        
        // Instantiate a TreeBuilder object and invoke the processStartTag method
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        boolean result = treeBuilder.processStartTag(name);
        
        // Check that the processStartTag method returned the correct result
        assertTrue(result);
    }
    
    @Test
    public void testProcessStartTagWithAttributes() {
        // Test that the processStartTag method correctly processes a start tag with attributes
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("class", "container");
        attrs.put("id", "main");
        
        // Instantiate a TreeBuilder object and invoke the processStartTag method
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        boolean result = treeBuilder.processStartTag(name, attrs);
        
        // Check that the processStartTag method returned the correct result
        assertTrue(result);
    }
    
    @Test
    public void testProcessEndTag() {
        // Test that the processEndTag method correctly processes an end tag
        String name = "p";
        
        // Instantiate a TreeBuilder object and invoke the processEndTag method
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        boolean result = treeBuilder.processEndTag(name);
        
        // Check that the processEndTag method returned the correct result
        assertTrue(result);
    }
    
    @Test
    public void testCurrentElement() {
        // Test that the currentElement method returns the current element from the stack
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        // Instantiate a TreeBuilder object and invoke the parse method
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        treeBuilder.parse(input, baseUri, null);
        
        // Check that the currentElement method returned the correct result
        assertNotNull(treeBuilder.currentElement());
        assertEquals("h1", treeBuilder.currentElement().tagName());
    }
    
}
