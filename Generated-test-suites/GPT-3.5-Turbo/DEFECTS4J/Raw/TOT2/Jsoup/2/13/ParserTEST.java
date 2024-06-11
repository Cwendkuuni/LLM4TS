package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><head><title>Hello</title></head><body><p>Paragraph</p></body></html>", "https://example.com");
        assertNotNull(doc);
        assertEquals("Hello", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Paragraph</p>", "https://example.com");
        assertNotNull(doc);
        assertEquals(1, doc.body().childNodeSize());
    }

    @Test
    public void testParseWithEmptyInput() {
        Document doc = Parser.parse("", "https://example.com");
        assertNotNull(doc);
        assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testParseWithInvalidBaseUri() {
        try {
            Parser.parse("<html><body></body></html>", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseBodyFragmentWithNullHtml() {
        try {
            Parser.parseBodyFragment(null, "https://example.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseWithInvalidInput() {
        try {
            Parser.parse("<ht%%ml></bod2y></html>", "https://example.com");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseBodyFragmentWithInvalidHtml() {
        try {
            Parser.parseBodyFragment("<p>Paragraph", "https://example.com");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }
}