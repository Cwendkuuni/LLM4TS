package org.jsoup.parser;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TreeBuilderTest {
    
    private TreeBuilder treeBuilder;
    
    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // Custom process for testing
                return true;
            }
        };
    }
    
    @After
    public void tearDown() {
        treeBuilder = null;
    }
    
    @Test
    public void testParse() {
        Document doc = treeBuilder.parse("<html><body><p>Hello World</p></body></html>", "example.com");
        Elements bodyElements = doc.select("body");
        Assert.assertEquals("Body element should exist", 1, bodyElements.size());
    }
    
    @Test
    public void testProcessStartTagWithName() {
        Assert.assertTrue("Start tag with name should be processed", treeBuilder.processStartTag("div"));
    }
    
    @Test
    public void testProcessStartTagWithNameAndAttributes() {
        Assert.assertTrue("Start tag with name and attributes should be processed", treeBuilder.processStartTag("a", new Attributes()));
    }
    
    @Test
    public void testProcessEndTag() {
        Assert.assertTrue("End tag with name should be processed", treeBuilder.processEndTag("span"));
    }
    
    @Test
    public void testCurrentElementWhenStackIsEmpty() {
        Assert.assertNull("Current element should be null when stack is empty", treeBuilder.currentElement());
    }
    
    @Test
    public void testCurrentElementWhenStackIsNotEmpty() {
        Element element = new Element("p");
        treeBuilder.stack.add(element);
        Assert.assertEquals("Current element should be the last element in stack", element, treeBuilder.currentElement());
    }
}