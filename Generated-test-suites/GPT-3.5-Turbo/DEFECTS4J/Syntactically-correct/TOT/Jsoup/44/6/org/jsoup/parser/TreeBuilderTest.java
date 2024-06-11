package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TreeBuilderTest {
    
    private TreeBuilder treeBuilder;
    
    @Before
    public void setUp() {
        treeBuilder = new MockTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        Assert.assertNotNull(treeBuilder.doc);
        Assert.assertEquals(0, treeBuilder.stack.size());
        Assert.assertEquals(baseUri, treeBuilder.baseUri);
        Assert.assertEquals(errors, treeBuilder.errors);
    }
    
    @Test
    public void testParse_noErrors() {
        String input = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        
        Document document = treeBuilder.parse(input, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(1, document.body().children().size());
        Assert.assertEquals("div", document.body().child(0).tagName());
        Assert.assertEquals("Hello World", document.body().child(0).text());
    }
    
    @Test
    public void testParse_withErrors() {
        String input = "<div><p>Hello <span>World</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        Document document = treeBuilder.parse(input, baseUri, errors);
        
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(1, document.body().children().size());
        Assert.assertEquals("div", document.body().child(0).tagName());
        Assert.assertEquals(1, errors.size());
    }
    
    @Test
    public void testProcess_startTag() {
        String name = "div";
        
        boolean result = treeBuilder.processStartTag(name);
        
        Assert.assertTrue(result);
        Assert.assertEquals(1, treeBuilder.stack.size());
        Assert.assertEquals("div", treeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testProcess_startTag_withAttributes() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "myDiv");
        attrs.put("class", "myClass");
        
        boolean result = treeBuilder.processStartTag(name, attrs);
        
        Assert.assertTrue(result);
        Assert.assertEquals(1, treeBuilder.stack.size());
        Assert.assertEquals("div", treeBuilder.currentElement().tagName());
        Assert.assertEquals(attrs, treeBuilder.currentElement().attributes());
    }
    
    @Test
    public void testProcess_endTag() {
        String name = "div";
        
        boolean result = treeBuilder.processEndTag(name);
        
        Assert.assertTrue(result);
        Assert.assertEquals(0, treeBuilder.stack.size());
    }
    
    private class MockTreeBuilder extends TreeBuilder {
        @Override
        protected boolean process(Token token) {
            return false;
        }
    }
}