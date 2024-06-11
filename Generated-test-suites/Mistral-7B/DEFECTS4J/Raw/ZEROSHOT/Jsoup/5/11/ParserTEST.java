package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.LinkedList;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><div><p>Test</p></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("Test Title", doc.title().text());
        assertEquals(4, doc.numChildren());
        assertEquals("html", doc.tagName());

        Element body = doc.body();
        assertEquals(1, body.numChildren());
        assertEquals("div", body.child(0).tagName());

        Element div = body.child(0);
        assertEquals(1, div.numChildren());
        assertEquals("p", div.child(0).tagName());

        assertEquals("Test", div.child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test</p></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals(1, doc.numChildren());
        assertEquals("html", doc.tagName());

        Element body = doc.body();
        assertEquals(1, body.numChildren());
        assertEquals("div", body.child(0).tagName());

        Element div = body.child(0);
        assertEquals(1, div.numChildren());
        assertEquals("p", div.child(0).tagName());

        assertEquals("Test", div.child(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Test</p></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        assertEquals(1, doc.numChildren());
        assertEquals("html", doc.tagName());

        Element body = doc.body();
        assertEquals(1, body.numChildren());
        assertEquals("div", body.child(0).tagName());

        Element div = body.child(0);
        assertEquals(1, div.numChildren());
        assertEquals("p", div.child(0).tagName());

        assertEquals("Test", div.child(0).text());

        doc.body().appendChild(new TextNode("Hello"));
        assertEquals(2, doc.body().numChildren());
    }
}