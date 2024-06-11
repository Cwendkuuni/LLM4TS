package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>", doc.toString());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test Heading</h1><p>Test Paragraph</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("<h1>Test Heading</h1><p>Test Paragraph</p>", doc.body().html());
        assertEquals("http://example.com", doc.baseUri());
    }
}