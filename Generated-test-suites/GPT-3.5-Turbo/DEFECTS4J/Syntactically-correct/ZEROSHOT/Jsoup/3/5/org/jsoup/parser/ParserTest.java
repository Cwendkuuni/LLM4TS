package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class ParserTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("");
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
        String baseUri = "http://www.example.com";
        document = Parser.parse(html, baseUri);
        
        // Assert document properties
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());

        // Assert head properties
        assertNotNull(document.head());
        assertEquals("Test", document.head().getElementsByTag("title").text());

        // Assert body properties
        assertNotNull(document.body());
        assertEquals("Hello, world!", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, world!</h1>";
        String baseUri = "http://www.example.com";
        document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertNotNull(document.body());
        assertEquals("Hello, world!", document.body().getElementsByTag("h1").text());
    }
}