package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");

        assertEquals(htmlTag, doc.tag());
        assertEquals(1, doc.childNodes().size());
        Element root = doc.childNodes().get(0);
        assertEquals(headTag, root.tag());
        assertEquals(1, root.childNodes().size());
        assertEquals(titleTag, root.childNodes().get(0).tag());
        assertEquals("Test", root.childNodes().get(0).text());

        root = root.nextSibling();
        assertEquals(bodyTag, root.tag());
        assertEquals(1, root.childNodes().size());
        assertEquals(h1Tag, root.childNodes().get(0).tag());
        assertEquals("Hello, World!", root.childNodes().get(0).text());

        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello, Body!</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");

        assertEquals(htmlTag, doc.tag());
        assertEquals(1, doc.childNodes().size());
        Element root = doc.childNodes().get(0);
        assertEquals(bodyTag, root.tag());
        assertEquals(1, root.childNodes().size());
        assertEquals(h1Tag, root.childNodes().get(0).tag());
        assertEquals("Hello, Body!", root.childNodes().get(0).text());

        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div id='myDiv'>Hello, Body!</div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com");

        assertEquals(htmlTag, doc.tag());
        assertEquals(1, doc.childNodes().size());
        Element root = doc.childNodes().get(0);
        assertEquals("div", root.tagName());
        assertEquals("myDiv", root.tag().getId());
        assertEquals("Hello, Body!", root.childText());

        assertEquals("https://example.com", doc.baseUri());
    }
}