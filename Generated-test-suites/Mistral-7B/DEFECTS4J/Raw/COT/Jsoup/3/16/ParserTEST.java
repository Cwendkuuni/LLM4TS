package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Test class for Parser.
 */
public class ParserTest {

    @Test
    public void testParse() throws IOException {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello World</h1><p>This is a test.</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element htmlNode = doc.head();
        assertEquals("html", htmlNode.tagName());
        Element titleNode = htmlNode.child(0);
        assertEquals("head", titleNode.tagName());
        assertEquals("Test", titleNode.text());

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());
        List<Element> pList = bodyNode.select("p");
        assertEquals(1, pList.size());
        assertEquals("This is a test.", pList.get(0).text());
    }

    @Test
    public void testParseBodyFragment() throws IOException {
        String bodyHtml = "<p>This is a test.</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals("html", doc.tagName());
        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());
        List<Element> pList = bodyNode.select("p");
        assertEquals(1, pList.size());
        assertEquals("This is a test.", pList.get(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello World</h1></body></html>";
        Parser.parse(html, null);
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Document doc = Parser.parse(html, "http://example.com");
        Element commentNode = doc.body();
        assertEquals("comment", commentNode.tagName());
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<xml version='1.0' encoding='UTF-8'?>";
        Document doc = Parser.parse(html, "http://example.com");
        Element xmlDeclNode = doc.body();
        assertEquals("xml-declaration", xmlDeclNode.tagName());
        assertEquals("UTF-8", xmlDeclNode.attr("encoding"));
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>This is a test.<div>";
        Document doc = Parser.parse(html, "http://example.com");
        Element divNode = doc.body().get(1);
        assertEquals("div", divNode.tagName());
        Element pNode = doc.body().get(0);
        assertEquals(0, pNode.children().size());
    }

    @Test
    public void testParseStartTag() {
        String html = "<p><b>Hello</b> World</p>";
        Document doc = Parser.parse(html, "http://example.com");
        Element pNode = doc.body().get(0);
        assertEquals(2, pNode.children().size());
        Element bNode = (Element) pNode.child(0);
        assertEquals("b", bNode.tagName());
        assertEquals("Hello", bNode.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href='http://example.com' id='link'>Link</a>";
        Document doc = Parser.parse(html, "http://example.com");
        Element aNode = doc.body().select("a").get(0);
        assertEquals("a", aNode.tagName());
        assertEquals("http://example.com", aNode.attr("href"));
        assertEquals("link", aNode.attr("id"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is a test.</p>";
        Document doc = Parser.parse(html, "http://example.com");
        Element pNode = doc.body().get(0);
        assertEquals(1, pNode.children().size());
        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("This is a test.", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[alert('Hello World');]]></script>";
        Document doc = Parser.parse(html, "http://example.com");
        Element scriptNode = doc.body().get(0);
        assertEquals("script", scriptNode.tagName());
        DataNode cdataNode = (DataNode) scriptNode.child(0);
        assertEquals("alert('Hello World');", cdataNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnknownTag() {
        String html = "<zoo></zoo>";
        Parser.parse(html, "http://example.com");
    }

    @Test
    public void testParseSelfClosedTag() {
        String html = "<img src='image.png'/>";
        Document doc = Parser.parse(html, "http://example.com");
        Element imgNode = doc.body().select("img").get(0);
        assertEquals("img", imgNode.tagName());
        assertEquals(0, imgNode.children().size());
    }
}