package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.asssrtNotEquals;

import org.junit.Test;

public class TreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("input", "baseUri", null);
        
        assertNull(treeBuilder.reader);
        assertNull(treeBuilder.tokeniser);
        assertNull(treeBuilder.doc);
        assertNull(treeBuilder.stack);
        assertNull(treeBuilder.baseUri);
        assertNull(treeBuilder.currentToken);
        assertNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Document doc = treeBuilder.parse("input", "baseUri");
        
        assertEquals("baseUri", doc.baseUri());
    }

    @Test
    public void testParseWithErrorList() {
        TreeBuilder treeBuilder = new TreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse("input", "baseUri", errors);
        
        assertEquals("baseUri", doc.baseUri());
        assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testRunParser() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.reader = new CharacterReader("input");
        treeBuilder.tokeniser = new Tokeniser(treeBuilder.reader, null);
        treeBuilder.stack = new ArrayList<Element>();
        
        treeBuilder.runParser();
        
        // Verify the parser ran without error
        assertEquals(0, treeBuilder.errors.size());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        boolean result = treeBuilder.processStartTag("tag");
        
        // Verify the result is true
        assertEquals(true, result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Attributes attrs = new Attributes();
        boolean result = treeBuilder.processStartTag("tag", attrs);
        
        // Verify the result is true
        assertEquals(true, result);
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        boolean result = treeBuilder.processEndTag("tag");
        
        // Verify the result is true
        assertEquals(true, result);
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Element element = treeBuilder.currentElement();
        
        // Verify the element is null
        assertNull(element);
    }
}