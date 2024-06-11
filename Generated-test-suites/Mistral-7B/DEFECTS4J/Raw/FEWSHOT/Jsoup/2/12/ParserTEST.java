package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(headTag, doc.head().tagName());
        assertEquals("Test", doc.title().text());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(h1Tag, doc.body().childNode(0).tagName());
        assertEquals("Hello", doc.body().childNode(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNode(0).tagName());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(h1Tag, doc.body().childNode(0).tagName());
        assertEquals("Hello", doc.body().childNode(0).text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element comment = doc.body().childNodes().get(0);

        assertEquals(Comment.class, comment.getClass());
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">";
        Document doc = Parser.parse(html, "http://test.com");
        XmlDeclaration xmlDecl = doc.head().childNodes().get(0);

        assertEquals(xmlDecl.xmlLang(), null);
        assertEquals(xmlDecl.standalone(), false);
        assertEquals(xmlDecl.xmlVersion(), "1.0");
        assertEquals(xmlDecl.xmlnsPrefix(), "xhtml");
        assertEquals(xmlDecl.xmlnsUri(), "http://www.w3.org/1999/xhtml");
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals(1, doc.body().childNodes().size());
        assertEquals(pTag, doc.body().childNode(0).tagName());
        assertEquals(null, doc.body().childNodes().get(0).nextSibling());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title><link rel=\"stylesheet\" href=\"style.css\"></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.head().childNodes().size());
        assertEquals(titleTag, doc.head().childNodes().get(0).tagName());
        assertEquals("Test", doc.head().childNodes().get(0).text());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(h1Tag, doc.body().childNodes().get(0).tagName());
        assertEquals("Hello", doc.body().childNodes().get(0).text());
        assertEquals("style.css", doc.head().childNodes().get(1).attributes().get("href").getValue());
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"test.html\" class=\"test\" id=\"test-id\">Test</a>";
        Document doc = Parser.parse(html, "http://test.com");
        Element link = doc.body().childNodes().get(0);

        assertEquals(aTag, link.tagName());
        assertEquals("test.html", link.attributes().get("href").getValue());
        assertEquals("test", link.attributes().get("class").getValue());
        assertEquals("test-id", link.attributes().get("id").getValue());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";
        Document doc = Parser.parse(html, "http://test.com");
        TextNode textNode = (TextNode) doc.body().childNodes().get(0).childNodes().get(0);

        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[function test() { alert('Hello'); }]]></script>";
        Document doc = Parser.parse(html, "http://test.com");
        TextNode textNode = (TextNode) doc.body().childNodes().get(0).childNodes().get(0);

        assertEquals("<script><![CDATA[function test() { alert('Hello'); }]]></script>", textNode.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/test/\"></head><body><a href=\"test.html\">Test</a></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals("http://test/test.html", doc.absUrl("test.html"));
    }
}