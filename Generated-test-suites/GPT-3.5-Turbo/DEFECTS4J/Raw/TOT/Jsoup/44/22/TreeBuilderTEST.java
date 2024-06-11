package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TreeBuilderTest {
    
    private TreeBuilder treeBuilder;
    
    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) { return false; }
        };
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        Assert.assertNotNull(treeBuilder.reader);
        Assert.assertNotNull(treeBuilder.tokeniser);
        Assert.assertNotNull(treeBuilder.doc);
        Assert.assertNotNull(treeBuilder.stack);
        Assert.assertNotNull(treeBuilder.baseUri);
        Assert.assertEquals(errors, treeBuilder.errors);
    }
    
    @Test
    public void testParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        
        Document document = treeBuilder.parse(input, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(1, document.children().size());
        Assert.assertEquals("html", document.child(0).tagName());
        
        Element htmlElement = document.child(0);
        Assert.assertEquals(2, htmlElement.children().size());
        Assert.assertEquals("head", htmlElement.child(0).tagName());
        Assert.assertEquals("body", htmlElement.child(1).tagName());
    }
    
    @Test
    public void testProcess() {
        Token token = new Token();
        boolean result = treeBuilder.process(token);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testProcessStartTag() {
        String tagName = "div";
        
        boolean result = treeBuilder.processStartTag(tagName);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testProcessStartTagWithAttributes() {
        String tagName = "div";
        Attributes attributes = new Attributes();
        attributes.put("class", "container");
        attributes.put("id", "main");
        
        boolean result = treeBuilder.processStartTag(tagName, attributes);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testProcessEndTag() {
        String tagName = "div";
        
        boolean result = treeBuilder.processEndTag(tagName);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testCurrentElement() {
        Element element1 = new Element("div");
        Element element2 = new Element("span");
        
        treeBuilder.stack = new ArrayList<>();
        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);
        
        Element currentElement = treeBuilder.currentElement();
        
        Assert.assertEquals(element2, currentElement);
    }
}