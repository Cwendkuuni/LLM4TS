package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.ArrayList;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "<html><body><div></div></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);

        // assert statements
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.currentToken);
        assertNotNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "<html><body><div></div></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = treeBuilder.parse(input, baseUri);

        // assert statements
        assertNotNull(doc);
    }

    @Test
    public void testProcess() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Token token = new Token();
        boolean result = treeBuilder.process(token);

        // assert statements
        assertEquals(false, result);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String tagName = "div";
        boolean result = treeBuilder.processStartTag(tagName);

        // assert statements
        assertEquals(false, result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String tagName = "div";
        Attributes attrs = new Attributes();
        boolean result = treeBuilder.processStartTag(tagName, attrs);

        // assert statements
        assertEquals(false, result);
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String tagName = "div";
        boolean result = treeBuilder.processEndTag(tagName);

        // assert statements
        assertEquals(false, result);
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Element element = treeBuilder.currentElement();

        // assert statements
        assertNull(element);
    }
}