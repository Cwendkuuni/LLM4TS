package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @ org.junit.Before
    public void setUp() {
        // Initialize any test data here
    }

    /**
     * Test parse method with a valid HTML string and base URI.
     */
    @ org.junit.Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assert that the parsed Document is as expected
        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());

        Element headNode = htmlNode.child(0);
        assertEquals("head", headNode.tagName());

        Element titleNode = headNode.child(0);
        assertEquals("title", titleNode.tagName());
        assertEquals("Test Title", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        assertEquals("body", bodyNode.tagName());

        Element headingNode = bodyNode.child(0);
        assertEquals("h1", headingNode.tagName());
        assertEquals("Test Heading", headingNode.text());
    }

    /**
     * Test parse method with an empty HTML string and base URI.
     */
    @ org.junit.Test
    public void testParseEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assert that the parsed Document is empty
        assertTrue(doc.isEmpty());
    }

    /**
     * Test parseBodyFragment method with a valid HTML fragment and base URI.
     */
    @ org.junit.Test
    public void testParseBodyFragmentValidHtml() {
        String bodyHtml = "<body><h1>Test Heading</h1></body>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Assert that the parsed Document has an empty head and the body is as expected
        assertTrue(doc.head().isEmpty());
        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());

        Element headingNode = bodyNode.child(0);
        assertEquals("h1", headingNode.tagName());
        assertEquals("Test Heading", headingNode.text());
    }

    /**
     * Test parseBodyFragment method with an empty HTML fragment and base URI.
     */
    @ org.junit.Test
    public void testParseBodyFragmentEmptyHtml() {
        String bodyHtml = "";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Assert that the parsed Document is empty
        assertTrue(doc.isEmpty());

        // Ensure that there are no exceptions thrown when parsing an empty HTML fragment
    }

    /**
     * Test parse method with an invalid HTML string and base URI.
     */
    @ org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String html = "<html><head><title>Test Title"; // missing closing tag
        String baseUri = "http://example.com";
        Parser.parse(html, baseUri);
    }

    /**
     * Test parse method with null HTML string and base URI.
     */
    @ org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        String html = null;
        String baseUri = "http://example.com";
        Parser.parse(html, baseUri);
    }

    /**
     * Test parse method with null base URI.
     */
    @ org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1></body></html>";
        String baseUri = null;
        try {
            Parser.parse(html, baseUri);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}