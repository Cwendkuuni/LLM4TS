package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.getElementsByTag("html").first();
        assertEquals(htmlTag, htmlNode.tag());

        Element headNode = doc.getElementsByTag("head").first();
        assertEquals(headTag, headNode.tag());

        Element titleNode = doc.getElementsByTag("title").first();
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());

        Element bodyNode = doc.getElementsByTag("body").first();
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = doc.getElementsByTag("h1").first();
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc.getElementsByTag("body").first();
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = doc.getElementsByTag("h1").first();
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        Element headNode = doc.getElementsByTag("head").first();
        assertNull(headNode);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        Element bodyNode = doc.getElementsByTag("body").first();
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = doc.getElementsByTag("h1").first();
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        Element headNode = doc.getElementsByTag("head").first();
        assertNull(headNode);
    }

    @Test
    public void testParseEmptyBodyFragment() {
        String bodyHtml = "<body></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc.getElementsByTag("body").first();
        assertEquals(bodyTag, bodyNode.tag());
        assertTrue(bodyNode.childElements().isEmpty());

        Element headNode = doc.getElementsByTag("head").first();
        assertNull(headNode);
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body>"; // missing closing tag in head
        try {
            Document doc = Parser.parse(html, "http://example.com");
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }
}