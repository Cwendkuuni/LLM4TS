package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        Document document = Parser.parse(html, "http://example.com");
        assertNotNull(document);
        assertEquals("Hello World!", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Fragment</h2>";
        Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(document);
        assertEquals("Fragment", document.body().getElementsByTag("h2").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><span>Relaxed</span></div>";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        assertNotNull(document);
        assertEquals("Relaxed", document.body().select("div > span").text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        Parser.parse("<p>Test</p>", null);
    }

    @Test
    public void testParseEmptyTagname() {
        String html = "<>";
        Document document = Parser.parse(html, "http://example.com");
        assertNull(document);
    }

    @Test
    public void testParseTextNodeInsideTag() {
        String html = "<p>Text before <!-- comment --> text after</p>";
        Document document = Parser.parse(html, "http://example.com");
        assertNotNull(document);
        assertEquals("Text before  text after", document.body().getElementsByTag("p").text());
    }

    // More test cases for edge cases, error scenarios, etc. can be added here

}