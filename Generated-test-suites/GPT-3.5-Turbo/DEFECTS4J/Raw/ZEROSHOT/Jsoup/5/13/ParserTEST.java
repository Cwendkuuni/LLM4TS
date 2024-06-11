package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;
    private String html;
    private String baseUri;

    @Before
    public void setup() {
        html = "<html><body><h1>Hello World!</h1></body></html>";
        baseUri = "http://example.com";
        parser = new Parser(html, baseUri, false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("Hello World!", doc.select("h1").text());
    }
}