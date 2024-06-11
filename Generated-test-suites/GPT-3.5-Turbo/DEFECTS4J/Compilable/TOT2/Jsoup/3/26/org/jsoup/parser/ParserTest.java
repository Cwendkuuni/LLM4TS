package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Add more assertions as needed
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Add more assertions as needed
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<p>Hello, World!</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Add more assertions as needed
    }
}