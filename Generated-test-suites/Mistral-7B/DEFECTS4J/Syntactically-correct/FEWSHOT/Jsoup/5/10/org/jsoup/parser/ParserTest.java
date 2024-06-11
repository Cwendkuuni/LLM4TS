package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.tag());
        assertEquals(doc.head(), doc.body().previousSibling());

        Element title = doc.head().child(0);
        assertEquals(titleTag, title.tag());
        assertEquals("Test", title.text());

        Element h1 = doc.body().child(0);
        assertEquals(h1Tag, h1.tag());
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals(htmlTag, doc.tag());
        assertEquals(0, doc.head().childCount());
        assertEquals(1, doc.body().childCount());

        Element h1 = doc.body().child(0);
        assertEquals(h1Tag, h1.tag());
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div class='myclass'>Hello World</div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        assertEquals(htmlTag, doc.tag());
        assertEquals(0, doc.head().childCount());
        assertEquals(1, doc.body().childCount());

        Element div = doc.body().child(0);
        assertEquals(divTag, div.tag());
        assertEquals("myclass", div.classes());
        assertEquals("Hello World", div.child(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        String html = "";
        Parser.parse(html, "http://example.com");
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><p<script>alert('Hello World')</script>";
        Document doc = Parser.parse(html, "http://example.com");

        // The parser should have ignored the script tag and continued parsing.
        assertEquals(htmlTag, doc.tag());
        assertEquals(1, doc.body().childCount());
        assertEquals(1, doc.body().child(0).childCount());
        assertEquals(pTag, doc.body().child(0).tag());
    }

    @Test
    public void testParseWithBaseHref() {
        String html = "<html><head><base href='/foo'><title>Test</title></head><body><a href='/bar'>Link</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("/foo/Test", doc.title().text());
        assertEquals("/bar", doc.select("a[href]").get(0).absUrl("href"));
    }

    @Test
    public void testParseWithBaseTarget() {
        String html = "<html><head><base target='_blank'><title>Test</title></head><body><a href='/foo'>Link</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("/foo", doc.select("a[href]").get(0).absUrl("href"));
        assertEquals("_blank", doc.head().base().attr("target"));
    }

    @Test
    public void testParseWithEmptyBodyFragment() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals(htmlTag, doc.tag());
        assertEquals(0, doc.head().childCount());
        assertEquals(0, doc.body().childCount());
    }

    @Test
    public void testParseSelfClosingTags() {
        String html = "<html><head><title>Test</title></head><body><img src='image.png' /></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element img = doc.body().child(0);
        assertTrue(img.tag().isSelfClosing());
    }

    @Test
    public void testParseUnknownSelfClosingTags() {
        String html = "<html><head><title>Test</title></head><body><div />></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element div = doc.body().child(0);
        assertTrue(div.tag().isValidSelfClosingTag());
    }

    @Test
    public void testParseImplicitParents() {
        String html = "<html><head><title>Test</title></head><body><p><input type='text' /></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element input = doc.body().child(1).child(0);
        assertEquals("INPUT", input.tag().name());
        assertEquals("text", input.attr("type"));

        Element p = input.parent();
        assertEquals("P", p.tag().name());
    }
}