package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParseMethod() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());
        Element headNode = doc.head();
        Element titleNode = headNode.child(0);
        assertEquals("head", titleNode.parent().tagName());
        assertEquals("title", titleNode.tagName());
        assertEquals("Test", titleNode.text());
        Element h1Node = htmlNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello", h1Node.text());
    }

    @Test
    public void testParseBodyFragmentMethod() {
        String bodyHtml = "<body><p>Test</p></body>";
        Parser parser = new Parser(bodyHtml, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        Element bodyNode = root.body();
        assertEquals("body", bodyNode.tagName());
        Element pNode = bodyNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Test", pNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        Parser parser = new Parser(null, "http://test.com");
        parser.parse(); // should throw exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyHtml() {
        Parser parser = new Parser("", "http://test.com");
        parser.parse(); // should throw exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidBaseUri() {
        Parser parser = new Parser("<html><head></head></html>", null);
        parser.parse(); // should throw exception
    }

    @Test
    public void testParseCommentMethod() {
        String html = "<html><!-- This is a test comment --></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        Element commentNode = root.child(0);
        assertEquals("comment", commentNode.tagName());
        assertEquals("This is a test comment", commentNode.text());
    }

    @Test
    public void testParseXmlDeclMethod() {
        String html = "<html xmlns:foo='bar'><head></head></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        XmlDeclaration decl = (XmlDeclaration) root.child(0);
        assertEquals("xmlns:foo", decl.xmlns());
        assertEquals("bar", decl.xmlnsPrefix());
    }

    @Test
    public void testParseEndTagMethod() {
        String html = "<html><body><p>Test</p></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        Element pNode = root.body().child(0);
        parser.parseEndTag();
        assertNull(pNode);
    }

    @Test
    public void testParseStartTagMethodWithAttributes() {
        String html = "<html><body><a href='test.html' class='test' id='testId'>Test</a></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        Element aNode = root.body().child(0).child(0);
        assertEquals("a", aNode.tagName());
        Map<String, String> attributes = aNode.attributes();
        assertEquals("test", attributes.get("class"));
        assertEquals("testId", attributes.get("id"));
        assertEquals("test.html", attributes.get("href"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStartTagMethodWithInvalidTagName() {
        String html = "<html><body><z></z></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        parser.parse(); // should throw exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStartTagMethodWithEmptyTagName() {
        String html = "<html><body></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        parser.parse(); // should throw exception
    }

    @Test
    public void testParseTextNodeMethod() {
        String html = "<html><body><p>Test</p></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        Element pNode = root.body().child(0);
        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testParseCdataMethod() {
        String html = "<html><body><![CDATA[This is some CDATA]]></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        TextNode textNode = (TextNode) root.body().child(0);
        assertEquals("This is some CDATA", textNode.text());
    }
}