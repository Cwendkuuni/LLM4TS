package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello World</h1>" +
                "<p>This is a <b>test</b>.</p>" +
                "</body></html>";

        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.head();
        assertEquals("html", htmlNode.tagName());
        assertEquals("http://example.com", htmlNode.baseUri());

        Element titleNode = htmlNode.child(0);
        assertEquals("head", titleNode.tagName());

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());

        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());

        Element pNode = bodyNode.child(1);
        assertEquals("p", pNode.tagName());

        Element bNode = pNode.child(0);
        assertEquals("b", bNode.tagName());
        assertEquals("test", bNode.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a <b>test</b></p>";

        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());

        Element pNode = bodyNode.child(0);
        assertEquals("p", pNode.tagName());

        Element bNode = pNode.child(0);
        assertEquals("b", bNode.tagName());
        assertEquals("test", bNode.text());
    }

    @Test
    public void testParseRelaxedBodyFragment() {
        String bodyHtml = "<b>test</b>";

        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());

        Element bNode = bodyNode.child(0);
        assertEquals("b", bNode.tagName());
        assertEquals("test", bNode.text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";

        Document doc = Parser.parse(html, "http://example.com");

        Element commentNode = doc.body().child(0);
        assertEquals("comment", commentNode.tagName());
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<xml version='1.0' encoding='UTF-8'><!-- This is a comment --></xml>";

        Document doc = Parser.parse(html, "http://example.com");

        Element xmlDeclNode = doc.body().child(0);
        assertEquals("xml-decl", xmlDeclNode.tagName());
        assertTrue(xmlDeclNode.xmlDecl().standalone);
        assertEquals("UTF-8", xmlDeclNode.xmlDecl().encoding);
        assertEquals("1.0", xmlDeclNode.xmlDecl().version);
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>This is a <b>test</b></p>";

        Document doc = Parser.parse(html, "http://example.com");

        Element bNode = doc.body().child(1).child(0);
        assertEquals("b", bNode.tagName());

        Parser parser = new Parser(html, "http://example.com", false);
        parser.parseEndTag();

        assertEquals(1, parser.stack.size());
        assertEquals(doc.body(), parser.stack.getLast());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello World</h1>" +
                "<p>This is a <b>test</b></p></body></html>";

        Parser parser = new Parser(html, "http://example.com", false);

        parser.parseStartTag();
        assertEquals("html", parser.last().tagName());

        parser.parseStartTag();
        assertEquals("head", parser.last().tagName());

        parser.parseStartTag();
        assertEquals("title", parser.last().tagName());

        parser.parseStartTag();
        assertEquals("body", parser.last().tagName());

        parser.parseStartTag();
        assertEquals("h1", parser.last().tagName());

        parser.parseStartTag();
        assertEquals("p", parser.last().tagName());

        parser.parseStartTag();
        assertEquals("b", parser.last().tagName());
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src='image.jpg' title='Image' alt='Alternate Text'/>";

        Parser parser = new Parser(html, "http://example.com", false);

        Attribute srcAttr = parser.parseAttribute();
        assertEquals("src", srcAttr.getKey());
        assertEquals("image.jpg", srcAttr.getValue());

        Attribute titleAttr = parser.parseAttribute();
        assertEquals("title", titleAttr.getKey());
        assertEquals("Image", titleAttr.getValue());

        Attribute altAttr = parser.parseAttribute();
        assertEquals("alt", altAttr.getKey());
        assertEquals("Alternate Text", altAttr.getValue());

        parser.parseAttribute(); // empty attribute
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is a test</p>";

        Parser parser = new Parser(html, "http://example.com", false);

        TextNode textNode = (TextNode) parser.parseTextNode().get();
        assertEquals("This is a test", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[function test() { alert('Hello World'); }]]></script>";

        Parser parser = new Parser(html, "http://example.com", false);

        TextNode cdataNode = (TextNode) parser.parseCdata().get();
        assertEquals("function test() { alert('Hello World'); }", cdataNode.text());
    }
}