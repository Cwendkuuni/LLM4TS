package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com/");
        Element htmlNode = doc.rootElement();

        assertEquals(htmlTag, htmlNode.tag());
        assertEquals("https://example.com/", htmlNode.baseUri());

        Element headNode = htmlNode.child(0);
        assertEquals(headTag, headNode.tag());

        Element titleNode = headNode.child(0);
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseWithBaseFragment() {
        String html = "<html><body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(html, "https://example.com/");
        Element htmlNode = doc.rootElement();
        assertEquals(htmlTag, htmlNode.tag());
        assertEquals("https://example.com/", htmlNode.baseUri());

        Element bodyNode = htmlNode.child(0);
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseWithRelaxedParsing() {
        String html = "<html><body><div>Hello World</div></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "https://example.com/");
        Element htmlNode = doc.rootElement();
        assertEquals(htmlTag, htmlNode.tag());
        assertEquals("https://example.com/", htmlNode.baseUri());

        Element bodyNode = htmlNode.child(0);
        assertEquals(bodyTag, bodyNode.tag());

        Element divNode = bodyNode.child(0);
        assertEquals("div", divNode.tagName());
        assertEquals("Hello World", divNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(xml, "https://example.com/");
        Element xmlDeclaration = doc.selectFirst("xml[1]");
        assertNotNull(xmlDeclaration);
        assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
    }

    @Test
    public void testParseComment() {
        String html = "<!DOCTYPE html>\n<!-- This is a comment -->\n<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com/");
        Comment comment = doc.selectFirst("comment()[1]");
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><p>Hello World</p></div>";
        Document doc = Parser.parse(html, "https://example.com/");
        Element divNode = doc.selectFirst("div[1]");
        assertEquals("div", divNode.tagName());
        Element pNode = divNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
        assertEquals(0, divNode.childSize());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div><p>Hello World</p></div>";
        Document doc = Parser.parse(html, "https://example.com/");
        Element divNode = doc.selectFirst("div[1]");
        assertEquals("div", divNode.tagName());
        Element pNode = divNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
        assertEquals(1, divNode.childSize());
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"https://example.com/\" class=\"link\">Link</a>";
        Document doc = Parser.parse(html, "https://example.com/");
        Element aNode = doc.selectFirst("a[1]");
        assertEquals("a", aNode.tagName());
        assertEquals("https://example.com/", aNode.attr("href"));
        assertEquals("link", aNode.attr("class"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";
        Document doc = Parser.parse(html, "https://example.com/");
        Element pNode = doc.selectFirst("p[1]");
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[alert('Hello World');]]></script>";
        Document doc = Parser.parse(html, "https://example.com/");
        Element scriptNode = doc.selectFirst("script[1]");
        assertEquals("script", scriptNode.tagName());
        TextNode cdataNode = (TextNode) scriptNode.child(0);
        assertEquals("alert('Hello World');", cdataNode.text());
    }
}