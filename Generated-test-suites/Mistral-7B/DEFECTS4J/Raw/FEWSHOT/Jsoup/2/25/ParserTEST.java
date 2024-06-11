package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.getElementsByTag("html").first();
        assertEquals(htmlTag, htmlNode.tag());
        Element headNode = htmlNode.getElementsByTag("head").first();
        Element titleNode = headNode.getElementsByTag("title").first();
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());
        Element bodyNode = htmlNode.getElementsByTag("body").first();
        Element h1Node = bodyNode.getElementsByTag("h1").first();
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        // Test parsing an empty document
        html = "";
        doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.tag());
        assertEquals(0, doc.childElementCount());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc.getElementsByTag("body").first();
        assertEquals(bodyTag, bodyNode.tag());
        Element h1Node = bodyNode.getElementsByTag("h1").first();
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        // Test parsing an empty body fragment
        bodyHtml = "";
        doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals(bodyTag, doc.tag());
        assertEquals(0, doc.childElementCount());
        assertEquals(0, doc.body().childElementCount());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a test comment --><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element htmlNode = doc.getElementsByTag("html").first();
        Comment comment = htmlNode.getElementsByTag("comment").first();
        assertEquals("This is a test comment", comment.data());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"http://example.com/foo\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "http://example.com");
        XmlDeclaration xmlDecl = doc.getElementsByTag("xml-decl").first();
        assertEquals("http://example.com/foo", xmlDecl.xmlns("foo"));
        assertEquals("en", xmlDecl.xmlLang());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element h1Node = doc.getElementsByTag("h1").first();
        assertEquals("Hello World", h1Node.text());
        assertEquals(0, h1Node.childElementCount());

        Element bodyNode = doc.getElementsByTag("body").first();
        assertEquals(0, bodyNode.childElementCount());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello <b>World</b></h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element h1Node = doc.getElementsByTag("h1").first();
        Element bNode = h1Node.getElementsByTag("b").first();
        assertEquals("World", bNode.text());

        // Test invalid start tag
        html = "<html><body><h1></html>";
        try {
            Parser.parse(html, "http://example.com");
            fail();
        } catch (RuntimeException e) {
            // Expected error
        }
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"http://example.com\" title=\"Test\">Link</a>";
        Document doc = Parser.parse(html, "http://example.com");
        Element linkNode = doc.getElementsByTag("a").first();
        assertEquals("http://example.com", linkNode.attr("href"));
        assertEquals("Test", linkNode.attr("title"));

        // Test missing attribute
        html = "<a href=\"http://example.com\">Link</a>";
        doc = Parser.parse(html, "http://example.com");
        assertNull(linkNode.attr("title"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        TextNode textNode = doc.getElementsByTag("p").first().getElementsByTag("text").first();
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[Hello World]]></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        TextNode textNode = doc.getElementsByTag("body").first().getElementsByTag("cdata").first();
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/subdir\"/><title>Test</title></head><body><a href=\"foo.html\">Link</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("/subdir/foo.html", doc.baseUri());
    }
}