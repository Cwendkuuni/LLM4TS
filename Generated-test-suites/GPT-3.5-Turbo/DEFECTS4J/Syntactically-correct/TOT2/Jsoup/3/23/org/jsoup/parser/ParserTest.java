package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical use case
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Document parsedDoc = Parser.parse(html, "http://example.com");
        assertNotNull(parsedDoc);
        assertEquals("Test", parsedDoc.title());
        assertEquals("Hello World!", parsedDoc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case
        String bodyHtml = "<p>Hello World!</p>";
        Document parsedDoc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(parsedDoc);
        assertEquals("", parsedDoc.title());
        assertEquals("Hello World!", parsedDoc.body().text());
    }

    @Test
    public void testParseRelaxed() {
        // Typical use case
        String bodyHtml = "<div><p>Hello World!</p><script>alert('Test')</script></div>";
        Document parsedDoc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(parsedDoc);
        assertEquals("", parsedDoc.title());
        assertEquals("Hello World! alert('Test')", parsedDoc.body().text());
    }

    // Add more test cases for edge cases and exception handling scenarios

}