package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element htmlNode = doc.body();
        assertTrue(htmlNode.tagName().equals(bodyTag.getName()));
        Element titleNode = doc.head().title();
        assertEquals("Test", titleNode.text());
        Element h1Node = htmlNode.getChild(0);
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Body Fragment</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        Element bodyNode = doc.body();
        assertEquals("Body Fragment", bodyNode.getChild(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<marquee>Hello</marquee>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
        Element marqueeNode = doc.body().getChild(0);
        assertTrue(marqueeNode.tagName().equals(Tag.valueOf("marquee").getName()));
    }

    @Test
    public void testParseEmptyBodyFragment() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        assertTrue(doc.body().children().isEmpty());
    }

    @Test
    public void testParseInvalidHtml() {
        String invalidHtml = "<html><body><script>alert('Hello World');</script><div>";
        Document doc = Parser.parse(invalidHtml, "http://test.com");
        Element bodyNode = doc.body();
        assertTrue(bodyNode.children().size() == 2);
        Element scriptNode = bodyNode.getChild(0);
        assertTrue(scriptNode.tagName().equals(Tag.valueOf("script").getName()));
        Element divNode = bodyNode.getChild(1);
        assertTrue(divNode.tagName().equals(Tag.valueOf("div").getName()));
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        Document doc = Parser.parse(xmlDecl, "http://test.com");
        XmlDeclaration xmlDeclNode = doc.head().xml();
        assertEquals("about:legacy-compat", xmlDeclNode.data());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Comment commentNode = doc.body().child(0);
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element h1Node = doc.body().getChild(0);
        assertEquals("Hello World", h1Node.text());
        assertTrue(h1Node.tag().isSelfClosing());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1 id=\"title\">Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element h1Node = doc.body().getChild(0);
        assertEquals("Hello World", h1Node.text());
        assertEquals("title", h1Node.attribute("id").getValue());
        assertTrue(h1Node.tag().isValidParent(headTag));
    }

    @Test
    public void testParseAttribute() {
        String html = "<input type=\"text\" name=\"username\" value=\"test\" />";
        Document doc = Parser.parse(html, "http://test.com");
        Element inputNode = doc.body().getChild(0);
        assertEquals("text", inputNode.tag().getName());
        assertEquals("username", inputNode.attribute("name").getValue());
        assertEquals("test", inputNode.attribute("value").getValue());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is a <strong>test</strong>.</p>";
        Document doc = Parser.parse(html, "http://test.com");
        TextNode textNode = (TextNode) doc.body().child(0).child(0);
        assertEquals("This is a ", textNode.text());
        TextNode strongTextNode = (TextNode) doc.body().child(0).child(1);
        assertEquals("test", strongTextNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><script><![CDATA[document.write('Hello World');]]></script></html>";
        Document doc = Parser.parse(html, "http://test.com");
        DataNode cdataNode = (DataNode) doc.head().child(0);
        assertEquals("document.write('Hello World');", cdataNode.text());
    }
}