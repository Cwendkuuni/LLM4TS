package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        // Test parsing a simple HTML document
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        Element htmlNode = doc.rootElement();
        assertEquals(htmlTag, htmlNode.tag());

        Element headNode = htmlNode.child(0);
        assertEquals(headTag, headNode.tag());

        Element titleNode = headNode.child(0);
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test Page", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals(Tag.H1, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        // Test parsing an HTML document with a text node
        html = "<html><body><p>Hello World</p></body></html>";
        parser = new Parser(html, null, false);
        doc = parser.parse();

        bodyNode = doc.rootElement().child(0);
        Element pNode = bodyNode.child(0);
        assertEquals(TextNode.class, pNode.getClass());
        assertEquals("Hello World", pNode.text());

        // Test parsing an HTML document with an empty body
        html = "<html><body></body></html>";
        parser = new Parser(html, null, false);
        doc = parser.parse();

        Element emptyBodyNode = doc.rootElement().child(0);
        assertNotNull(emptyBodyNode);
        assertEquals(bodyTag, emptyBodyNode.tag());
        assertTrue(emptyBodyNode.children().isEmpty());

        // Test parsing an HTML document with a base tag
        html = "<html><head><base href=\"/test/\"></head><body></body></html>";
        parser = new Parser(html, null, false);
        doc = parser.parse();
        assertEquals("/test/", doc.baseUri());

        // Test parsing an HTML document with a comment
        html = "<html><!-- This is a comment --></html>";
        parser = new Parser(html, null, false);
        doc = parser.parse();
        assertNotNull(doc.getElementsByTag("!--").first());

        // Test parsing an HTML document with a CDATA section
        html = "<html><script><![CDATA[function test() { alert('Hello World'); }]]></script></html>";
        parser = new Parser(html, null, false);
        doc = parser.parse();
        Node cdataNode = doc.getElementsByTag("script").first().child(0);
        assertEquals(new DataNode("function test() { alert('Hello World'); }", null), cdataNode);

        // Test parsing an HTML document with a textarea tag
        html = "<html><body><textarea>Hello World</textarea></body></html>";
        parser = new Parser(html, null, false);
        doc = parser.parse();
        Node textareaNode = doc.getElementsByTag("textarea").first();
        TextNode textNode = (TextNode) textareaNode.child(0);
        assertEquals("Hello World", textNode.text());

        // Test parsing an HTML document with a title tag
        html = "<html><head><title>Test Page</title></head><body></body></html>";
        parser = new Parser(html, null, false);
        doc = parser.parse();
        assertEquals("Test Page", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        // Test parsing a body fragment with a simple HTML document
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);

        Element pNode = doc.rootElement().child(0);
        assertEquals(TextNode.class, pNode.getClass());
        assertEquals("Hello World", pNode.text());

        // Test parsing a body fragment with a text node
        bodyHtml = "<p>Hello World</p>";
        doc = Parser.parseBodyFragment(bodyHtml, null);
        pNode = doc.rootElement().child(0);
        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("Hello World", textNode.text());

        // Test parsing a body fragment with a base tag
        bodyHtml = "<html><head><base href=\"/test/\"></head><body></body></html>";
        doc = Parser.parseBodyFragment(bodyHtml, null);
        assertEquals("/test/", doc.baseUri());
    }

    @Test
    public void testParseWithRelaxedParsing() {
        // Test parsing with relaxed parsing enabled
        String html = "<html><body><div><span></span></div></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        Element divNode = doc.rootElement().child(0).child(0);
        Element spanNode = divNode.child(0);
        assertEquals(Tag.DIV, divNode.tag());
        assertEquals(Tag.SPAN, spanNode.tag());
        assertTrue(spanNode.children().isEmpty());

        // Test parsing with relaxed parsing enabled and an empty tag
        html = "<html><body><div><img /></div></body></html>";
        parser = new Parser(html, null, false);
        doc = parser.parse();
        Element imgNode = doc.rootElement().child(0).child(0);
        assertEquals(Tag.IMG, imgNode.tag());
        assertTrue(imgNode.children().isEmpty());

        // Test parsing with relaxed parsing enabled and a self-closing tag
        html = "<html><body><div><img src=\"image.png\" /></div></body></html>";
        parser = new Parser(html, null, false);
        doc = parser.parse();
        Element imgNode2 = doc.rootElement().child(0).child(0);
        assertEquals(Tag.IMG, imgNode2.tag());
        assertTrue(imgNode2.children().isEmpty());
    }
}