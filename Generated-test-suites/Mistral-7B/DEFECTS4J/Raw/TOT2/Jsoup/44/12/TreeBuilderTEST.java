package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        // Test valid input, baseUri, and no errors
        TreeBuilder treeBuilder = new TreeBuilder();
        Document doc = treeBuilder.parse("input.html", "baseUri", ParseErrorList.noTracking());
        assertNotNull(doc);

        // Test null input
        assertThrows(NullPointerException.class, () -> treeBuilder.parse(null, "baseUri", ParseErrorList.noTracking()));

        // Test empty input
        assertThrows(IllegalArgumentException.class, () -> treeBuilder.parse("", "baseUri", ParseErrorList.noTracking()));

        // Test null baseUri
        assertThrows(NullPointerException.class, () -> treeBuilder.parse("input.html", null, ParseErrorList.noTracking()));

        // Test null errors
        assertThrows(NullPointerException.class, () -> treeBuilder.parse("input.html", "baseUri", null));
    }

    @Test
    public void testParse() {
        // Test valid input and baseUri
        TreeBuilder treeBuilder = new TreeBuilder();
        Document doc = treeBuilder.parse("input.html");
        assertNotNull(doc);

        // Test null input
        assertThrows(NullPointerException.class, () -> treeBuilder.parse(null));

        // Test empty input
        assertThrows(IllegalArgumentException.class, () -> treeBuilder.parse(""));
    }

    @Test
    public void testParseWithErrors() {
        // Test valid input, baseUri, and errors
        TreeBuilder treeBuilder = new TreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse("input.html", "baseUri", errors);
        assertNotNull(doc);

        // Test null input
        assertThrows(NullPointerException.class, () -> treeBuilder.parse(null, "baseUri", errors));

        // Test empty input
        assertThrows(IllegalArgumentException.class, () -> treeBuilder.parse("", "baseUri", errors));

        // Test null errors
        assertThrows(NullPointerException.class, () -> treeBuilder.parse("input.html", "baseUri", null));

        // Test errors with a message
        treeBuilder = new TreeBuilder();
        errors = new ParseErrorList();
        errors.add(new ParseError(10, 20, "Error message"));
        doc = treeBuilder.parse("input.html", "baseUri", errors);
        assertNotNull(doc);
        assertEquals(1, errors.size());
        assertEquals("Error message", errors.get(0).getMessage());
    }

    @Test
    public void testRunParser() {
        // Test valid input and no errors
        TreeBuilder treeBuilder = new TreeBuilder();
        Document doc = treeBuilder.parse("input.html", "baseUri", ParseErrorList.noTracking());
        treeBuilder.runParser();

        // Test null input
        assertThrows(NullPointerException.class, () -> treeBuilder.runParser());

        // Test empty input
        treeBuilder = new TreeBuilder();
        treeBuilder.parse("", "baseUri", ParseErrorList.noTracking());
        assertFalse(treeBuilder.hasErrors());
        treeBuilder.runParser();
        assertTrue(treeBuilder.hasErrors());
    }

    // ... (test cases for other methods)

    // Test methods that use other methods
    @Test
    public void testParseWithErrorsAndStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse("input.html", "baseUri", errors);
        treeBuilder.parse("input.html", "baseUri", errors);
        treeBuilder.runParser();

        boolean processStartTag = treeBuilder.processStartTag("html");
        assertTrue(processStartTag);

        Element html = treeBuilder.currentElement();
        assertEquals("html", html.tagName());
        Element head = treeBuilder.processStartTag("head");
        assertEquals("head", head.tagName());
        assertEquals(html, head.parent());

        treeBuilder.processEndTag("head");
        assertEquals(1, treeBuilder.stack.size());
        assertEquals(html, treeBuilder.stack.get(0));

        treeBuilder.processEndTag("html");
        assertEquals(0, treeBuilder.stack.size());
        assertTrue(treeBuilder.hasNoErrors());
    }

    // ... (more test cases)
}