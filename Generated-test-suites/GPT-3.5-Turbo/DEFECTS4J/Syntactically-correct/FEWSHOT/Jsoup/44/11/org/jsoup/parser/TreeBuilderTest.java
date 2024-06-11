package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import org.junit.Test;

public class TreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        TreeBuilder treeBuilder = new TreeBuilderTest();
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.doc);
        assertEquals(0, treeBuilder.stack.size());
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertEquals(errors, treeBuilder.errors);
    }
    
    @Test
    public void testProcessStartTag() {
        String name = "div";
        
        TreeBuilder treeBuilder = new TreeBuilderTest();
        boolean result = treeBuilder.processStartTag(name);
        
        assertTrue(result);
        assertNotNull(treeBuilder.start);
        assertEquals(name, treeBuilder.start.name());
    }
    
    @Test
    public void testProcessStartTagWithAttributes() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("class", "my-div");
        
        TreeBuilder treeBuilder = new TreeBuilderTest();
        boolean result = treeBuilder.processStartTag(name, attrs);
        
        assertTrue(result);
        assertNotNull(treeBuilder.start);
        assertEquals(name, treeBuilder.start.name());
        assertEquals(attrs, treeBuilder.start.getAttributes());
    }
    
    @Test
    public void testProcessEndTag() {
        String name = "div";
        
        TreeBuilder treeBuilder = new TreeBuilderTest();
        boolean result = treeBuilder.processEndTag(name);
        
        assertTrue(result);
        assertNotNull(treeBuilder.end);
        assertEquals(name, treeBuilder.end.name());
    }
    
    @Test
    public void testCurrentElement() {
        Element element = new Element("div");
        
        TreeBuilder treeBuilder = new TreeBuilderTest();
        treeBuilder.stack.add(element);
        
        assertEquals(element, treeBuilder.currentElement());
    }
}