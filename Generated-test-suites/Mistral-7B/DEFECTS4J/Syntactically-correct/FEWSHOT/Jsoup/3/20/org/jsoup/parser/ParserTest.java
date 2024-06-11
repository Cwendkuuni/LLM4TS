package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().firstChild().text());

        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertNotNull(doc.head().title());
        assertNotNull(doc.body().firstChild());
        assertSame(doc.head(), doc.child(0));
        assertSame(doc.body(), doc.child(1));
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");

        assertEquals("Hello World", doc.body().firstChild().text());
        assertNull(doc.head());

        assertNotNull(doc.body());
        assertSame(doc.body(), doc.child(0));
    }

    @Test
    public void testParseWithAttributes() {
        String html = "<p class='test' id='testId'>Test</p>";
        Document doc = Parser.parse(html, "http://example.com");

        Element p = doc.body().firstChild();
        assertEquals("Test", p.text());
        assertEquals("test", p.classAttr());
        assertEquals("testId", p.id());
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<img src='image.png' />";
        Document doc = Parser.parse(html, "http://example.com");

        Element img = doc.body().firstChild();
        assertNotNull(img);
        assertEquals("img", img.tagName());
        assertTrue(img.isEmpty());
    }

    @Test
    public void testParseWithImplicitParent() {
        String html = "<p><strong>Test</strong></p>";
        Document doc = Parser.parse(html, "http://example.com");

        Element p = doc.body().firstChild();
        assertNotNull(p);
        assertEquals("p", p.tagName());

        Element strong = p.firstChild();
        assertNotNull(strong);
        assertEquals("strong", strong.tagName());
    }

    @Test
    public void testParseWithBaseTag() {
        String html = "<base href='/subdirectory/'><html><body><a href='relative.html'>Test</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("/subdirectory/relative.html", doc.baseUri());
        Element a = doc.body().firstChild().firstChild();
        assertEquals("Test", a.text());
        assertEquals("/subdirectory/relative.html", a.absUrl("href"));
    }

    @Test
    public void testParseNoBaseTag() {
        String html = "<html><body><a href='relative.html'>Test</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("http://example.com/relative.html", doc.baseUri());
        Element a = doc.body().firstChild().firstChild();
        assertEquals("Test", a.text());
        assertEquals("http://example.com/relative.html", a.absUrl("href"));
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Document doc = Parser.parse(html, "http://example.com");

        Comment comment = doc.body().firstChild();
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is some CDATA]]>";
        Document doc = Parser.parse(html, "http://example.com");

        TextNode cdata = doc.body().firstChild();
        assertEquals("This is some CDATA", cdata.text());
    }
}