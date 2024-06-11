package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical use case
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals("h1", doc.body().child(0).tagName());
        assertEquals("Hello, World!", doc.body().child(0).text());

        // Edge case: empty html
        html = "";
        doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("html", doc.tagName());

        // Error scenario: null html and baseUri
        try {
            Parser.parse(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("body", doc.tagName());
        assertEquals("p", doc.body().child(0).tagName());
        assertEquals("Hello, World!", doc.body().child(0).text());

        // Edge case: empty bodyHtml
        bodyHtml = "";
        doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("body", doc.tagName());

        // Error scenario: null bodyHtml and baseUri
        try {
            Parser.parseBodyFragment(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        // Typical use case
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("body", doc.tagName());
        assertEquals("div", doc.body().child(0).tagName());
        assertEquals("p", doc.body().child(0).child(0).tagName());
        assertEquals("Hello, World!", doc.body().child(0).child(0).text());

        // Edge case: empty bodyHtml
        bodyHtml = "";
        doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("body", doc.tagName());

        // Error scenario: null bodyHtml and baseUri
        try {
            Parser.parseBodyFragmentRelaxed(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

}