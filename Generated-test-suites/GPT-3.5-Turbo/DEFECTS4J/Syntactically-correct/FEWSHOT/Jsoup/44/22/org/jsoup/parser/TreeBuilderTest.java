package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // Setup
        String input = "";
        String baseUri = "";
        ParseErrorList errors = null;

        // Exercise
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        treeBuilder.initialiseParse(input, baseUri, errors);

        // Verify
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNotNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        // Setup
        String input = "";
        String baseUri = "";

        // Exercise
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        Document document = treeBuilder.parse(input, baseUri);

        // Verify
        assertNotNull(document);
    }

    @Test
    public void testParseWithErrors() {
        // Setup
        String input = "";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();

        // Exercise
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        Document document = treeBuilder.parse(input, baseUri, errors);

        // Verify
        assertNotNull(document);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testProcess() {
        // Setup
        TreeBuilderImpl treeBuilder = new TreeBuilderImpl();
        Token token = new Token();

        // Exercise
        boolean result = treeBuilder.process(token);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testProcessStartTag() {
        // Setup
        TreeBuilderImpl treeBuilder = new TreeBuilderImpl();
        String name = "div";

        // Exercise
        boolean result = treeBuilder.processStartTag(name);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        // Setup
        TreeBuilderImpl treeBuilder = new TreeBuilderImpl();
        String name = "div";
        Attributes attrs = new Attributes();

        // Exercise
        boolean result = treeBuilder.processStartTag(name, attrs);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testProcessEndTag() {
        // Setup
        TreeBuilderImpl treeBuilder = new TreeBuilderImpl();
        String name = "div";

        // Exercise
        boolean result = treeBuilder.processEndTag(name);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testCurrentElement() {
        // Setup
        TreeBuilderImpl treeBuilder = new TreeBuilderImpl();
        Element element = new Element("div");

        // Exercise
        treeBuilder.stack.add(element);
        Element currentElement = treeBuilder.currentElement();

        // Verify
        assertEquals(element, currentElement);
    }
}