package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        String input = "test";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(input, treeBuilder.reader.getInput());
        assertEquals(errors, treeBuilder.errors);
        assertNull(treeBuilder.doc);
        assertNull(treeBuilder.tokeniser);
        assertNull(treeBuilder.stack);
        assertNull(treeBuilder.currentToken);
    }
    
    @Test
    public void testParseWithNoErrors() {
        String input = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        TreeBuilder treeBuilder = new TreeBuilder();
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertEquals(baseUri, doc.baseUri());
        assertEquals(1, doc.getElementsByTag("html").size());
        assertEquals(1, doc.getElementsByTag("body").size());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello World!", doc.getElementsByTag("h1").get(0).text());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><body><h1>Hello World!</h2></body></html>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        
        TreeBuilder treeBuilder = new TreeBuilder();
        Document doc = treeBuilder.parse(input, baseUri, errors);
        
        assertEquals(baseUri, doc.baseUri());
        assertEquals(1, doc.getElementsByTag("html").size());
        assertEquals(1, doc.getElementsByTag("body").size());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals(0, doc.getElementsByTag("h2").size());
        assertEquals(1, errors.size());
    }
    
    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        boolean result = treeBuilder.processStartTag("div");
        assertEquals(true, result);
        
        Element currentElement = treeBuilder.currentElement();
        assertEquals("div", currentElement.tagName());
    }
    
    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Attributes attrs = new Attributes();
        attrs.put("class", "container");
        boolean result = treeBuilder.processStartTag("div", attrs);
        assertEquals(true, result);
        
        Element currentElement = treeBuilder.currentElement();
        assertEquals("div", currentElement.tagName());
        assertEquals("container", currentElement.attr("class"));
    }
    
    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.processStartTag("div");
        boolean result = treeBuilder.processEndTag("div");
        assertEquals(true, result);
        
        Element currentElement = treeBuilder.currentElement();
        assertNull(currentElement);
    }
}