package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");

        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());
        Element headNode = htmlNode.child(0);
        assertEquals("head", headNode.tagName());
        Element titleNode = headNode.child(0);
        assertEquals("title", titleNode.tagName());
        assertEquals("Test", titleNode.text());
        Element bodyNode = htmlNode.child(1);
        assertEquals("body", bodyNode.tagName());
        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com/");

        Element htmlNode = doc.body();
        assertEquals("body", htmlNode.tagName());
        Element h1Node = htmlNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://example.com/");
        assertEquals("html", doc.tagName());
        assertEquals(0, doc.numChildren());
    }

    @Test
    public void testParseComments() {
        String html = "<html><!-- This is a comment --><head><title>Test</title></head><body><!-- This is another comment -->" +
                "<h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");

        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());
        Element headNode = htmlNode.child(0);
        assertEquals("head", headNode.tagName());
        Element titleNode = headNode.child(0);
        assertEquals("title", titleNode.tagName());
        assertEquals("Test", titleNode.text());
        Element bodyNode = htmlNode.child(1);
        assertEquals("body", bodyNode.tagName());
        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());

        Comment comment1 = (Comment) headNode.child(0);
        assertEquals("This is a comment", comment1.text());
        Comment comment2 = (Comment) bodyNode.child(0);
        assertEquals("This is another comment", comment2.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[ <h1>This is CDATA</h1> ]]></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");

        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());
        Element h1Node = htmlNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("This is CDATA", h1Node.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:my=\"http://example.com/my\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "http://example.com/");

        XmlDeclaration xmlDecl = doc.head().child(0);
        assertEquals("xmlns:my", xmlDecl.xmlnsAttribute().key());
        assertEquals("http://example.com/my", xmlDecl.xmlnsAttribute().value());
        assertEquals("xml:lang", xmlDecl.xmlLangAttribute().key());
        assertEquals("en", xmlDecl.xmlLangAttribute().value());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");

        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());
        Element pNode = htmlNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1 id=\"heading\">Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");

        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());
        Element h1Node = htmlNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
        assertEquals("heading", h1Node.id());
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"http://example.com/\" title=\"This is a link\">Link</a>";
        Document doc = Parser.parse(html, "http://example.com/");

        Element aNode = doc.body().child(0);
        assertEquals("a", aNode.tagName());
        assertEquals("http://example.com/", aNode.absUrl("href"));
        assertEquals("This is a link", aNode.absUrl("title"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");

        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());
        Element pNode = htmlNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
    }
}