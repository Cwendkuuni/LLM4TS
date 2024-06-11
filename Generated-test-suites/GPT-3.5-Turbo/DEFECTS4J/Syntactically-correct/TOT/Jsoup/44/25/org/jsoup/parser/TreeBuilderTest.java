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
                return false;
            }
        };
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(errors, treeBuilder.errors);
    }
    
    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        
        assertNotNull(doc.head());
        assertNotNull(doc.head().title());
        assertEquals("Test", doc.head().title().text());
        
        assertNotNull(doc.body());
    }
    
    @Test
    public void testCurrentElement() {
        assertNull(treeBuilder.currentElement());
        
        Element element1 = new Element("div");
        treeBuilder.stack.add(element1);
        assertEquals(element1, treeBuilder.currentElement());
        
        Element element2 = new Element("span");
        treeBuilder.stack.add(element2);
        assertEquals(element2, treeBuilder.currentElement());
    }
}