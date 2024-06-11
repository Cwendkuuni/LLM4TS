package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head>" +
                "<body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element htmlNode = doc;
        Element headNode = htmlNode.selectFirst("head");
        Element titleNode = headNode.selectFirst("title");
        Element bodyNode = htmlNode.body();
        Element h1Node = bodyNode.selectFirst("h1");

        assertEquals("html", htmlNode.tagName());
        assertEquals("head", headNode.tagName());
        assertEquals("title", titleNode.tagName());
        assertEquals("h1", h1Node.tagName());
        assertEquals("Test Page", titleNode.text());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        Element h1Node = doc.body().selectFirst("h1");

        assertEquals("body", doc.tagName());
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        String html = "<html><body></body></html>";
        Parser.parse(html, null);
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element commentNode = doc.selectFirst("html comment");

        assertEquals(Comment.class, commentNode.getClass());
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[This is some CDATA]]></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element cdataNode = doc.selectFirst("html cdata");

        assertEquals(DataNode.class, cdataNode.getClass());
        assertEquals("This is some CDATA", cdataNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
        Document doc = Parser.parse(html, "http://example.com");
        XmlDeclaration xmlDecl = doc.selectFirst("html xml-declaration");

        assertEquals("http://www.w3.org/2001/XMLSchema-instance", xmlDecl.xmlns().getURI().toString());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element pNode = doc.body().selectFirst("p");
        assertEquals(Element.class, pNode.parent().getClass());
        assertEquals(Body.class, pNode.parent().tagName());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1 id=\"test\">Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element h1Node = doc.body().selectFirst("h1");
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello", h1Node.text());
        assertEquals("test", h1Node.id());
    }

    @Test
    public void testParseAttributes() {
        String html = "<img src=\"image.png\" alt=\"Image\" title=\"Title\">";
        Document doc = Parser.parse(html, "http://example.com");
        Element imgNode = doc.selectFirst("img");

        Map<String, String> attributes = imgNode.attributes();
        assertEquals(3, attributes.size());
        assertEquals("image.png", attributes.get("src"));
        assertEquals("Image", attributes.get("alt"));
        assertEquals("Title", attributes.get("title"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element pNode = doc.body().selectFirst("p");

        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("Hello World", textNode.text());
    }
}