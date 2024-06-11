package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {
    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // Custom process implementation for testing
                return false;
            }
        };
    }

    @Test
    public void testInitialiseParse() {
        // Test Valid input
        String input = "<html></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNotNull(treeBuilder.currentToken);
        assertNotNull(treeBuilder.errors);
        assertEquals(baseUri, treeBuilder.baseUri);

        // Test Null input
        try {
            treeBuilder.initialiseParse(null, baseUri, errors);
            fail("NullPointerException should be thrown for null input");
        } catch (NullPointerException e) {
            assertEquals("String input must not be null", e.getMessage());
        }

        // Test Null baseUri
        try {
            treeBuilder.initialiseParse(input, null, errors);
            fail("NullPointerException should be thrown for null baseUri");
        } catch (NullPointerException e) {
            assertEquals("BaseURI must not be null", e.getMessage());
        }
    }

    @Test
    public void testParse() {
        // Test Valid input
        String input = "<html></html>";
        String baseUri = "http://example.com";
        Document parsedDoc = treeBuilder.parse(input, baseUri);
        assertNotNull(parsedDoc);
        assertEquals(baseUri, parsedDoc.baseUri());
        assertTrue(parsedDoc.childNodes().size() > 0);

        // Test Invalid input
        String invalidInput = "<html>";
        Document invalidParsedDoc = treeBuilder.parse(invalidInput, baseUri);
        assertNull(invalidParsedDoc);
    }

    @Test
    public void testProcess() {
        Token token = new Token();

        // Test process with valid token
        boolean result = treeBuilder.process(token);
        assertFalse(result);

        // Test process with invalid token
        Token invalidToken = new Token();
        invalidToken.type = Token.TokenType.START_TAG;
        invalidToken.name = "invalid";
        boolean invalidResult = treeBuilder.process(invalidToken);
        assertFalse(invalidResult);
    }

    @Test
    public void testProcessStartTag() {
        String tagName = "div";

        // Test processStartTag with valid name
        boolean result = treeBuilder.processStartTag(tagName);
        assertFalse(result);

        // Test processStartTag with invalid name
        String invalidTagName = "<>";
        boolean invalidResult = treeBuilder.processStartTag(invalidTagName);
        assertFalse(invalidResult);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String tagName = "div";
        Attributes attrs = new Attributes();

        // Test processStartTag with valid name and attributes
        boolean result = treeBuilder.processStartTag(tagName, attrs);
        assertFalse(result);

        // Test processStartTag with invalid name and valid attributes
        String invalidTagName = "<>";
        boolean invalidResult = treeBuilder.processStartTag(invalidTagName, attrs);
        assertFalse(invalidResult);
    }

    @Test
    public void testProcessEndTag() {
        String tagName = "div";

        // Test processEndTag with valid name
        boolean result = treeBuilder.processEndTag(tagName);
        assertFalse(result);

        // Test processEndTag with invalid name
        String invalidTagName = "<>";
        boolean invalidResult = treeBuilder.processEndTag(invalidTagName);
        assertFalse(invalidResult);
    }

    @Test
    public void testCurrentElement() {
        // Test currentElement when stack is empty
        Element element = treeBuilder.currentElement();
        assertNull(element);

        // Test currentElement with elements in stack
        String tagName = "div";
        Element addChildElement = new Element(tagName);
        treeBuilder.stack.add(addChildElement);

        Element currentElement = treeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals(tagName, currentElement.tagName());
    }
}