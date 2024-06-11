package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import org.jsoup.nodes.*;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Title</title></head><body><h1>Heading</h1><p>Paragraph</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("Title", doc.title().text());
        assertEquals("Heading", doc.select("h1").text());
        assertEquals("Paragraph", doc.select("p").text());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element body = doc.body();
        assertEquals(2, body.childSize());
        assertEquals("Paragraph 1", body.child(0).text());
        assertEquals("Paragraph 2", body.child(1).text());
    }

    @Test
    public void testParseEmptyDocument() {
        String html = "";
        Document doc = Parser.parse(html, "http://example.com");

        assertTrue(doc.isEmpty());
        assertNull(doc.title());
        assertNull(doc.body());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><head><title>Title</title>"; // missing closing tag
        Document doc = Parser.parse(html, "http://example.com");

        assertNotNull(doc);
        assertEquals(1, doc.head().childSize());
        assertEquals("Title", doc.head().child(0).text());
        assertEquals(0, doc.body().childSize());
    }

    @Test
    public void testParseWithBaseTag() {
        String html = "<html><head><base href=\"/subdir/\"/><title>Title</title></head><body><a href=\"page.html\">Link</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("Title", doc.title().text());
        assertEquals("/subdir/page.html", doc.select("a").absUrl("href"));
    }

    @Test
    public void testParseWithScriptTag() {
        String html = "<html><head><title>Title</title><script src=\"script.js\"></script></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element script = doc.selectFirst("script");
        assertNotNull(script);
        assertEquals("script.js", script.absUrl("src"));
    }

    @Test
    public void testParseWithStyleTag() {
        String html = "<html><head><title>Title</title><style type=\"text/css\">body { color: red; }</style></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("red", doc.body().cssText("color"));
    }

    @Test
    public void testParseWithInvalidTag() {
        String html = "<html><head><title>Title</title><badtag></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        // no exception thrown, but no badtag element in the document
        assertNotNull(doc);
        assertEquals(0, doc.select("badtag").size());
    }

    @Test
    public void testParseWithComment() {
        String html = "<html><!-- This is a comment --><head><title>Title</title></head><body><p>Paragraph</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element comment = doc.selectFirst("comment()");
        assertNotNull(comment);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseWithCdata() {
        String html = "<html><![CDATA[ <p>Paragraph in CDATA</p> ]]><head><title>Title</title></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element cdata = doc.selectFirst("p");
        assertNotNull(cdata);
        assertEquals("<p>Paragraph in CDATA</p>", cdata.outerHtml());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        Parser.parse("<html></html>", null);
    }
}