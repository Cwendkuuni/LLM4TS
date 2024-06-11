package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    private Document doc;

    @Before
    public void setup() {
        doc = new Document("");
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello world!</h1></body></html>";
        String baseUri = "http://example.com/";
        Document result = Parser.parse(html, baseUri);
        assertNotNull(result);
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello world!</h1></body></html>", result.html());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello world!</h1>";
        String baseUri = "http://example.com/";
        Document result = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(result);
        assertEquals("<html><head></head><body><h1>Hello world!</h1></body></html>", result.html());
    }
}