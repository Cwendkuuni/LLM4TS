package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.head();
        assertEquals("html", htmlNode.tagName());
        assertNotNull(htmlNode.children());
        assertEquals(2, htmlNode.children().size());

        Element titleNode = htmlNode.getFirstChild();
        assertEquals("head", titleNode.tagName());
        assertNotNull(titleNode.children());
        assertEquals(1, titleNode.children().size());

        Element titleElement = titleNode.getFirstChild();
        assertEquals("title", titleElement.tagName());
        assertEquals("Test Page", titleElement.text());

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());
        assertNotNull(bodyNode.children());
        assertEquals(1, bodyNode.children().size());

        Element h1Node = bodyNode.getFirstChild();
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World!", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());
        assertNotNull(bodyNode.children());
        assertEquals(1, bodyNode.children().size());

        Element pNode = bodyNode.getFirstChild();
        assertEquals("p", pNode.tagName());
        assertEquals("Test", pNode.text());
    }

    @Test
    public void testParseWithRelaxedParsing() {
        String bodyHtml = "<p>Test <img src=\"image.png\" alt=\"Image\"></p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());
        assertNotNull(bodyNode.children());
        assertEquals(1, bodyNode.children().size());

        Element pNode = bodyNode.getFirstChild();
        assertEquals("p", pNode.tagName());
        assertEquals("Test ", pNode.text()); // note no closing tag for img

        Element imgNode = pNode.getChild(0);
        assertEquals("img", imgNode.tagName());
        assertEquals("image.png", imgNode.absUrl("src"));
        assertEquals("Image", imgNode.attr("alt"));
    }

    @Test
    public void testParseWithXmlDeclaration() {
        String xmlHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><p>Test</p></body></html>";
        Document doc = Parser.parse(xmlHtml, "http://example.com");

        XmlDeclaration xmlDecl = doc.head().getFirstChild();
        assertEquals("xml", xmlDecl.tagName());
        assertEquals("UTF-8", xmlDecl.attr("encoding"));
    }

    @Test
    public void testParseWithComment() {
        String html = "<html><!-- This is a test comment --><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Comment comment = doc.body();
        assertEquals("!-- This is a test comment --", comment.text());
    }

    @Test
    public void testParseWithDataNodes() {
        String html = "<html><body><script type=\"text/javascript\">console.log('Test');</script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Node scriptNode = doc.body().getFirstChild();
        assertEquals("script", scriptNode.tagName());
        assertEquals("text/javascript", scriptNode.attr("type"));
        assertEquals("console.log('Test');", scriptNode.text());

        Node scriptContentNode = scriptNode.getFirstChild();
        assertEquals("TextNode", scriptContentNode.getClass().getSimpleName());
        assertEquals("console.log('Test');", scriptContentNode.text());
    }

    @Test
    public void testParseWithBaseTag() {
        String html = "<html><head><base href=\"/subdir\"></head><body><a href=\"test.html\">Link</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com/index.html");

        Element baseNode = doc.head().getFirstChild();
        assertEquals("base", baseNode.tagName());
        assertEquals("/subdir", baseNode.attr("href"));

        Element linkNode = doc.body().getFirstChild();
        assertEquals("a", linkNode.tagName());
        assertEquals("/subdir/test.html", linkNode.absUrl("href"));
    }
}