package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class TreeBuilderTest {
    
    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // Dummy implementation for testing purposes
                return true;
            }
        };
        
        String input = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals(1, doc.getElementsByTag("html").size());
        assertEquals(1, doc.getElementsByTag("body").size());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello, World!", doc.getElementsByTag("h1").get(0).text());
    }
    
    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // Dummy implementation for testing purposes
                return true;
            }
        };
        
        String name = "div";
        boolean result = treeBuilder.processStartTag(name);
        
        assertTrue(result);
    }
    
    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // Dummy implementation for testing purposes
                return true;
            }
        };
        
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "myDiv");
        attrs.put("class", "container");
        
        boolean result = treeBuilder.processStartTag(name, attrs);
        
        assertTrue(result);
    }
    
    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // Dummy implementation for testing purposes
                return true;
            }
        };
        
        String name = "div";
        boolean result = treeBuilder.processEndTag(name);
        
        assertTrue(result);
    }
    
    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // Dummy implementation for testing purposes
                return true;
            }
        };
        
        Element element = new Element("div");
        treeBuilder.stack.add(element);
        
        Element currentElement = treeBuilder.currentElement();
        
        assertEquals(element, currentElement);
    }
}