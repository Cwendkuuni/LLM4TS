package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class TreeBuilderTest {
    
    private TreeBuilder treeBuilder;
    private Document doc;
    private ArrayList<Element> stack;
    
    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            protected boolean process(Token token) {
                return false;
            }
        };
        
        doc = new Document("http://www.example.com");
        stack = new ArrayList<Element>();
        
        treeBuilder.doc = doc;
        treeBuilder.stack = stack;
    }
    
    @Test
    public void processStartTag_shouldReturnTrue() {
        String tagName = "div";
        
        boolean result = treeBuilder.processStartTag(tagName);
        
        assertTrue(result);
    }
    
    @Test
    public void processStartTag_withAttributes_shouldReturnTrue() {
        String tagName = "div";
        Attributes attrs = new Attributes();
        attrs.put("class", "container");
        attrs.put("id", "main");
        
        boolean result = treeBuilder.processStartTag(tagName, attrs);
        
        assertTrue(result);
    }
    
    @Test
    public void processEndTag_shouldReturnTrue() {
        String tagName = "div";
        
        boolean result = treeBuilder.processEndTag(tagName);
        
        assertTrue(result);
    }
    
    @Test
    public void currentElement_withEmptyStack_shouldReturnNull() {
        Element result = treeBuilder.currentElement();
        
        assertNull(result);
    }
    
    @Test
    public void currentElement_withNonEmptyStack_shouldReturnLastElement() {
        Element element1 = new Element("div");
        Element element2 = new Element("span");
        stack.add(element1);
        stack.add(element2);
        
        Element result = treeBuilder.currentElement();
        
        assertEquals(element2, result);
    }
}