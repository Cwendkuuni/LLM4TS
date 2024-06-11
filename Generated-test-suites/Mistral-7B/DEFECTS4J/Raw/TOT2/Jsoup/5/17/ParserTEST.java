package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><script src=\"script.js\"></script><h1>Hello World</h1></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }
}