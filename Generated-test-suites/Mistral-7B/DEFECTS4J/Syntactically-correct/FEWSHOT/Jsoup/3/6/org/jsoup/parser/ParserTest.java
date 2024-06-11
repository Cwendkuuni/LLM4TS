package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.jsoup.nodes.*;

import java.util.*;

public class ParserTest {

    @Test
    public void testParseEmpty() {
        String html = "";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals(htmlTag, doc.child(0));
    }

    @Test
    public void testParseHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element htmlNode = doc.child(0);
        assertEquals(htmlTag, htmlNode);
        Element headNode = htmlNode.child(0);
        assertEquals(headTag, headNode);
        Element titleNode = headNode.child(0);
        assertEquals(titleTag, titleNode);
        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag, bodyNode);
        Element h1Node = bodyNode.child(0);
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(bodyHtml, baseUri, true);
        Document doc = parser.parse();
        Element htmlNode = doc.child(0);
        assertEquals(htmlTag, htmlNode);
        Element bodyNode = htmlNode.child(0);
        assertEquals(bodyTag, bodyNode);
        Element h1Node = bodyNode.child(0);
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseRelaxedBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(bodyHtml, baseUri, true);
        Document doc = parser.parse();
        Element htmlNode = doc.child(0);
        assertEquals(htmlTag, htmlNode);
        Element bodyNode = htmlNode.child(0);
        assertEquals(bodyTag, bodyNode);
        Element h1Node = bodyNode.child(0);
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment -->";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element htmlNode = doc.child(0);
        assertEquals(htmlTag, htmlNode);
        Element commentNode = htmlNode.child(0);
        assertEquals(Comment.class, commentNode.getClass());
        assertEquals("This is a comment", commentNode.data());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:my=\"http://example.com\" xml:lang=\"en\">";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element htmlNode = doc.child(0);
        assertEquals(htmlTag, htmlNode);
        XmlDeclaration xmlDeclaration = htmlNode.child(0);
        assertEquals("http://example.com", xmlDeclaration.xmlns("my"));
        assertEquals("en", xmlDeclaration.xmlLang());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element htmlNode = doc.child(0);
        assertEquals(htmlTag, htmlNode);
        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag, bodyNode);
        Element pNode = bodyNode.child(0);
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p>Hello World<img src=\"image.png\"></p></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element htmlNode = doc.child(0);
        assertEquals(htmlTag, htmlNode);
        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag, bodyNode);
        Element pNode = bodyNode.child(0);
        assertEquals("Hello World", pNode.text());
        Element imgNode = pNode.child(1);
        assertEquals("image.png", imgNode.absUrl("src"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src=\"image.png\" alt=\"Alternate Text\" title=\"Title Text\">";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element imgNode = doc.select("img").first();
        assertEquals("image.png", imgNode.absUrl("src"));
        assertEquals("Alternate Text", imgNode.attr("alt"));
        assertEquals("Title Text", imgNode.attr("title"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element htmlNode = doc.child(0);
        Element bodyNode = htmlNode.child(1);
        Element pNode = bodyNode.child(0);
        TextNode textNode = pNode.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><script><![CDATA[function test() { alert('Hello World'); }]]></script></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element htmlNode = doc.child(0);
        Element bodyNode = htmlNode.child(1);
        TextNode scriptNode = bodyNode.child(0);
        assertEquals("function test() { alert('Hello World'); }", scriptNode.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/test/\"></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element htmlNode = doc.child(0);
        Element headNode = htmlNode.child(0);
        Element baseNode = headNode.child(0);
        assertEquals("/test/", baseNode.absUrl("href"));
        assertEquals("/test/", doc.baseUri());
    }
}