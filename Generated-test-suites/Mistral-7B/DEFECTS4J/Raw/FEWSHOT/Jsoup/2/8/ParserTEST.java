package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        Element htmlNode = doc.childNodes().get(0);
        assertEquals(headTag, htmlNode.tagName());
        assertEquals(1, htmlNode.childNodes().size());
        Element headNode = htmlNode.childNodes().get(0);
        assertEquals(titleTag, headNode.tagName());
        assertEquals("Title", headNode.text().trim());

        Element bodyNode = doc.body();
        assertEquals(1, bodyNode.childNodes().size());
        Element h1Node = bodyNode.childNodes().get(0);
        assertEquals(h1Tag, h1Node.tagName());
        assertEquals("Heading", h1Node.text().trim());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Heading</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        Element htmlNode = doc.childNodes().get(0);
        assertEquals(bodyTag, htmlNode.tagName());
        assertEquals(1, htmlNode.childNodes().size());
        Element h1Node = htmlNode.childNodes().get(0);
        assertEquals(h1Tag, h1Node.tagName());
        assertEquals("Heading", h1Node.text().trim());

        assertEquals(doc.head(), null);
    }

    @Test
    public void testParseComments() {
        String html = "<html><!-- This is a comment --><head><title>Title</title></head><body><h1>Heading</h1><!-- Another comment --></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.childNodes().get(0);
        assertEquals(1, htmlNode.childNodes().size());
        Element headNode = htmlNode.childNodes().get(0);
        assertEquals(1, headNode.childNodes().size());
        Element titleNode = headNode.childNodes().get(0);
        assertEquals(titleTag, titleNode.tagName());
        assertEquals("Title", titleNode.text().trim());

        assertEquals(doc.body().childNodes().size(), 1);
        Element h1Node = doc.body().childNodes().get(0);
        assertEquals(h1Tag, h1Node.tagName());
        assertEquals("Heading", h1Node.text().trim());

        Comment firstComment = (Comment) htmlNode.childNodes().get(0);
        assertEquals("This is a comment", firstComment.text());

        Comment secondComment = (Comment) doc.body().childNodes().get(1);
        assertEquals("Another comment", secondComment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.w3.org/1999/xhtml\">";
        Document doc = Parser.parse(html, "http://example.com");

        XmlDeclaration xmlDecl = doc.head().childNodes().get(0);
        assertEquals("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.w3.org/1999/xhtml\"", xmlDecl.data().trim());
    }

    @Test
    public void testParseEmptyElements() {
        String html = "<img src=\"image.jpg\"/>";
        Document doc = Parser.parse(html, "http://example.com");

        Element imgNode = doc.body().childNodes().get(0);
        assertEquals("img", imgNode.tagName());
        assertTrue(imgNode.isEmpty());
        assertEquals("image.jpg", imgNode.absUrl("src"));
    }

    @Test
    public void testParseAttributes() {
        String html = "<a href=\"link.html\" id=\"linkID\" class=\"linkClass\">Link</a>";
        Document doc = Parser.parse(html, "http://example.com");

        Element linkNode = doc.body().childNodes().get(0);
        assertEquals("a", linkNode.tagName());
        assertEquals("link.html", linkNode.attr("href"));
        assertEquals("linkID", linkNode.attr("id"));
        assertEquals("linkClass", linkNode.attr("class"));
    }

    @Test
    public void testParseTextNodes() {
        String html = "<p>This is a <strong>test</strong>.</p>";
        Document doc = Parser.parse(html, "http://example.com");

        Element pNode = doc.body().childNodes().get(0);
        assertEquals("p", pNode.tagName());
        TextNode textNode = (TextNode) pNode.childNodes().get(0);
        assertEquals("This is a ", textNode.text().trim());
        TextNode strongTextNode = (TextNode) pNode.childNodes().get(1);
        assertEquals("test", strongTextNode.text().trim());
    }

    @Test
    public void testParseCdata() {
        String html = "<script type=\"text/javascript\">function test() { alert('Hello, world!'); }</script>";
        Document doc = Parser.parse(html, "http://example.com");

        Element scriptNode = doc.body().childNodes().get(0);
        assertEquals("script", scriptNode.tagName());
        TextNode cdataNode = (TextNode) scriptNode.childNodes().get(0);
        assertEquals("function test() { alert('Hello, world!'); }", cdataNode.text().trim());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<base href=\"/new-dir/\"/><html><head><title>Title</title></head><body><h1>Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("/new-dir/", doc.baseUri());
    }
}