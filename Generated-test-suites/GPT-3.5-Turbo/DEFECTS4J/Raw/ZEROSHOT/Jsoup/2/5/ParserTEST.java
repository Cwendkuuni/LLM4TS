package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";
        parser = new Parser(html, baseUri, false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello", doc.body().select("h1").text());
    }
}