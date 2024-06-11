package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical use case test
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        
        // Edge case test
        String emptyHtml = "";
        String emptyBaseUri = "";
        Document emptyDoc = Parser.parse(emptyHtml, emptyBaseUri);
        assertNotNull(emptyDoc);
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case test
        String bodyHtml = "<h1>Hello World!</h1><p>This is a paragraph.</p>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        
        // Edge case test
        String emptyBodyHtml = "";
        String emptyBaseUri = "";
        Document emptyDoc = Parser.parseBodyFragment(emptyBodyHtml, emptyBaseUri);
        assertNotNull(emptyDoc);
    }

    @Test
    public void testParseRelaxed() {
        // Typical use case test
        String bodyHtml = "<h1>Hello World!</h1><p>This is a paragraph.</p>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        
        // Edge case test
        String emptyBodyHtml = "";
        String emptyBaseUri = "";
        Document emptyDoc = Parser.parseRelaxed(emptyBodyHtml, emptyBaseUri);
        assertNotNull(emptyDoc);
    }

}