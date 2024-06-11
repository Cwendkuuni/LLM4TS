package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @org.junit.Before
    public void setUp() {
    }

    @org.junit.Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.head();
        assertEquals(htmlTag, htmlNode.tag());

        Element titleNode = doc.head().title();
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test Page", titleNode.text());

        Element bodyNode = doc.body();
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = doc.body().child(0);
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        assertEquals("http://example.com", doc.baseUri());
    }

    @org.junit.Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc.body();
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = doc.body().child(0);
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(0, doc.head().childSize());
    }

    @org.junit.Test
    public void testParseXmlDecl() {
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document doc = Parser.parse("<html>" + xmlDecl + "</html>", "http://example.com");

        XmlDeclaration xmlDeclaration = doc.head().xmlData();
        assertEquals(xmlDecl, xmlDeclaration.getRawText());
        assertEquals("UTF-8", xmlDeclaration.getEncoding());
        assertTrue(xmlDeclaration.isStandalone());
    }

    @org.junit.Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element pNode = doc.body().child(0);
        assertEquals("p", pNode.tagName());

        Parser parser = new Parser(html, "http://example.com", false);
        parser.parseEndTag();
        Element pTag = parser.last();
        assertNull(pTag.child(0));
    }

    @org.junit.Test
    public void testParseStartTag() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element h1Node = doc.body().child(0);
        assertEquals("h1", h1Node.tagName());

        Parser parser = new Parser(html, "http://example.com", false);
        parser.parseStartTag();
        Element h1Tag = parser.last();
        assertEquals("h1", h1Tag.tagName());

        Attribute titleAttribute = h1Tag.classes();
        assertNotNull(titleAttribute);
        assertEquals("", titleAttribute.toString());

        parser.parseAttribute();
        Attribute classAttribute = h1Tag.classes();
        assertEquals("class", classAttribute.getKey());
        assertEquals("test", classAttribute.getValue());

        parser.parseEndTag();
        assertEquals(0, h1Tag.childSize());

        parser.parseStartTag();
        Element divTag = parser.last();
        assertEquals("div", divTag.tagName());

        parser.parseEndTag();
        assertEquals(0, divTag.childSize());
    }

    @org.junit.Test
    public void testParseTextNode() {
        String html = "<html><body><p>Test ParseTextNode</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element pNode = doc.body().child(0);
        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("Test ParseTextNode", textNode.text());

        Parser parser = new Parser(html, "http://example.com", false);
        parser.parseTextNode();
        TextNode textNode2 = (TextNode) parser.last();
        assertEquals("Test ParseTextNode", textNode2.text());
    }

    @org.junit.Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(0, doc.body().childSize());

        Parser parser = new Parser(html, "http://example.com", false);
        parser.parseComment();
        Element commentNode = parser.last();
        assertEquals("comment", commentNode.tagName());
        assertEquals("This is a comment", commentNode.text());
    }

    @org.junit.Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[ This is CDATA ]]></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element bodyNode = doc.body();
        TextNode cdataNode = (TextNode) bodyNode.child(0);
        assertEquals("This is CDATA", cdataNode.text());

        Parser parser = new Parser(html, "http://example.com", false);
        parser.parseCdata();
        TextNode cdataNode2 = (TextNode) parser.last();
        assertEquals("This is CDATA", cdataNode2.text());
    }

    @org.junit.Test
    public void testParserValidation() {
        try {
            new Parser(null, "http://example.com", false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            new Parser("", "http://example.com", false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}