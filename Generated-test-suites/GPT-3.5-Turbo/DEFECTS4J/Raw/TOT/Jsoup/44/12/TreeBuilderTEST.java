package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {
    private TreeBuilder treeBuilder;
    private String input;
    private String baseUri;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // Dummy implementation for testing purposes
                return false;
            }
        };
        input = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        baseUri = "http://example.com";
        errors = ParseErrorList.noTracking();
    }

    @Test
    public void testInitialiseParseNotNullInput() {
        try {
            treeBuilder.initialiseParse(null, baseUri, errors);
            fail("NullPointerException should be thrown when input is null.");
        } catch (NullPointerException e) {
            assertEquals("String input must not be null", e.getMessage());
        }
    }

    @Test
    public void testInitialiseParseNotNullBaseUri() {
        try {
            treeBuilder.initialiseParse(input, null, errors);
            fail("NullPointerException should be thrown when baseUri is null.");
        } catch (NullPointerException e) {
            assertEquals("BaseURI must not be null", e.getMessage());
        }
    }

    @Test
    public void testParseNoErrors() {
        Document doc = treeBuilder.parse(input, baseUri, errors);
        assertNotNull("Parsed document should not be null.", doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseWithErrors() {
        ParseErrorList trackingErrors = new ParseErrorList();
        Document doc = treeBuilder.parse(input, baseUri, trackingErrors);
        assertNotNull("Parsed document should not be null.", doc);
        assertEquals("http://example.com", doc.baseUri());
        assertFalse("ParseErrorList should not be empty.", trackingErrors.isEmpty());
    }

    @Test
    public void testRunParser() {
        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();
        // TODO: Add assertions for the processed tokens and their expected results
    }

    @Test
    public void testProcessStartTag() {
        boolean result = treeBuilder.processStartTag("h1");
        assertTrue("Process start tag should return true.", result);
        // TODO: Add assertions for the expected results after processing the start tag
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        Attributes attrs = new Attributes();
        attrs.put("class", "title");
        boolean result = treeBuilder.processStartTag("h1", attrs);
        assertTrue("Process start tag should return true.", result);
        // TODO: Add assertions for the expected results after processing the start tag with attributes
    }

    @Test
    public void testProcessEndTag() {
        boolean result = treeBuilder.processEndTag("h1");
        assertTrue("Process end tag should return true.", result);
        // TODO: Add assertions for the expected results after processing the end tag
    }

    @Test
    public void testCurrentElementNotEmptyStack() {
        Element element = new Element("div");
        ArrayList<Element> stack = new ArrayList<>();
        stack.add(element);
        treeBuilder.stack = stack;

        Element currentElement = treeBuilder.currentElement();
        assertEquals("Current element should be the last element in the stack.", element, currentElement);
    }

    @Test
    public void testCurrentElementEmptyStack() {
        treeBuilder.stack = new ArrayList<>();

        Element currentElement = treeBuilder.currentElement();
        assertNull("Current element should be null when stack is empty.", currentElement);
    }
}