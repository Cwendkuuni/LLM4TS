package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertNotNull(doc);
        assertTrue(doc.body().childrenSize() > 0);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<head><title>Test</title></head><body><h1>Hello World!</h1></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        assertNotNull(doc);
        assertTrue(doc.head().childrenSize() > 0);
        assertTrue(doc.body().childrenSize() > 0);
    }

    // Additional test methods for edge cases and exception handling scenarios can be added here

}