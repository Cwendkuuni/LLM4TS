package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

public class ParserTest {
    @Test
    public void testParseBasicFunctionality() {
        // Basic functionality of parsing HTML into a Document
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element root = doc.body();
        Element title = doc.head().title();
        Element h1 = root.getElementsByTag("h1").first();

        assertEquals("Test", title.text());
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseEndTag() {
        // Basic functionality of parsing and closing end tags
        String html = "<p><i>Test</i></p>";
        Document doc = Parser.parse(html, "http://test.com");
        Element p = doc.getElementsByTag("p").first();
        Element i = p.getElementsByTag("i").first();

        assertEquals("Test", i.text());

        Parser parser = new Parser(html, "http://test.com", false);
        parser.parseEndTag();
        Element last = parser.stack.getLast();

        assertEquals(Tag.valueOf("i"), last.tag());
    }

    @Test
    public void testParseStartTag() {
        // Basic functionality of parsing start tags and handling attributes
        String html = "<p id='p1'><b class='bold' data-foo='bar'>Test</b></p>";
        Document doc = Parser.parse(html, "http://test.com");
        Element p = doc.getElementsByTag("p").first();
        Element b = p.getElementsByTag("b").first();

        assertEquals("p1", p.id());
        assertEquals("bold", b.className());
        assertEquals("bar", b.attr("data-foo"));
        assertEquals("Test", b.text());

        Parser parser = new Parser(html, "http://test.com", false);
        parser.parseStartTag();
        Element last = parser.stack.getLast();

        assertEquals(Tag.valueOf("b"), last.tag());
    }

    @Test
    public void testParseEdgeCases() {
        // Edge cases testing with invalid input, empty HTML, missing baseUri, and invalid baseUri
        String invalidHtml = "<html><body><div><p>Test";
        Document doc = Parser.parse(invalidHtml, "http://test.com");
        assertTrue(doc.getElementsByTag("html").isEmpty());

        String emptyHtml = "";
        doc = Parser.parse(emptyHtml, "http://test.com");
        assertTrue(doc.getElementsByTag("html").isEmpty());

        Document docNullBaseUri = Parser.parse("<html></html>", null);
        assertEquals(null, docNullBaseUri.baseUri());

        Document docInvalidBaseUri = Parser.parse("<html></html>", "http://test.com/test");
        assertEquals("http://test.com/test", docInvalidBaseUri.baseUri());
    }

    @Test
    public void testParseExceptions() {
        // Exception cases testing with malformed HTML, invalid tag, and missing opening tag
        String malformedHtml = "<html><body><div><p>Test";
        assertThrows(IllegalArgumentException.class, () -> Parser.parse(malformedHtml, "http://test.com"));

        String invalidTag = "<html><body><div><foo></foo>";
        assertThrows(IllegalArgumentException.class, () -> Parser.parse(invalidTag, "http://test.com"));

        String missingOpeningTag = "</p>";
        assertThrows(IllegalArgumentException.class, () -> Parser.parse(missingOpeningTag, "http://test.com"));
    }
}