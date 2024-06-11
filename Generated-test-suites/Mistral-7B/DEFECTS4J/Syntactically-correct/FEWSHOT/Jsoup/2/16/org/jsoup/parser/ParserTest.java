package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(htmlTag, last(doc.body()).tagName());
        assertEquals("Test", last(doc.head()).child(0).text());
        assertEquals("Hello World", last(doc.body()).child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Fragment</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(bodyTag, last(doc.body()).tagName());
        assertEquals("Fragment", last(doc.body()).child(0).text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertTrue(doc.body().children().get(0) instanceof Comment);
    }

    @Test
    public void testParseCdata() {
        String html = "<html><script><![CDATA[alert('Hello World');]]></script></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertTrue(last(doc.body()).child(0) instanceof DataNode);
        assertEquals("alert('Hello World');", last(doc.body()).child(0).data());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"http://example.com/foo\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals("http://example.com/foo", doc.xmlNs("foo"));
        assertEquals("en", doc.xmlLang());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><div><p>Content</p></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(divTag, last(doc.body()).child(0).tagName());
        assertEquals(pTag, last(doc.body()).child(0).child(0).tagName());
        assertEquals("Content", last(doc.body()).child(0).child(0).text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><div><img src=\"image.jpg\" alt=\"Image\"/></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(divTag, last(doc.body()).child(0).tagName());
        assertEquals(imgTag, last(doc.body()).child(0).child(0).tagName());
        assertEquals("image.jpg", last(doc.body()).child(0).child(0).absUrl("src"));
        assertEquals("Image", last(doc.body()).child(0).child(0).attr("alt"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src=\"image.jpg\" alt=\"Image\" class=\"class1 class2\"/>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("image.jpg", last(doc.body()).child(0).attr("src"));
        assertEquals("Image", last(doc.body()).child(0).attr("alt"));
        assertEquals("class1 class2", last(doc.body()).child(0).attr("class"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Content</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(pTag, last(doc.body()).child(0).tagName());
        assertEquals("Content", last(doc.body()).child(0).child(0).text());
    }

    private Element last(Document doc) {
        return doc.body().lastChild();
    }

    private Tag divTag = Tag.valueOf("div");
    private Tag imgTag = Tag.valueOf("img");
    private Tag pTag = Tag.valueOf("p");
}