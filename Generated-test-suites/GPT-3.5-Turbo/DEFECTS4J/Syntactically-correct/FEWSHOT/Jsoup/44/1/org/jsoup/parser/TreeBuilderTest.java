package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.ParseErrorList;

public class TreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("input", "baseUri", null);
        
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNull(treeBuilder.errors);
    }
    
    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Document document = treeBuilder.parse("input", "baseUri");
        
        assertEquals("baseUri", document.baseUri());
    }
    
    @Test
    public void testProcess() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        assertFalse(treeBuilder.process(new Token()));
    }
    
    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        assertFalse(treeBuilder.processStartTag("name"));
    }
    
    @Test
    public void testProcessStartTagWithAttrs() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        assertFalse(treeBuilder.processStartTag("name", new Attributes()));
    }
    
    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        assertFalse(treeBuilder.processEndTag("name"));
    }
    
    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.stack.add(new Element("test"));
        
        assertEquals("test", treeBuilder.currentElement().tagName());
    }
}