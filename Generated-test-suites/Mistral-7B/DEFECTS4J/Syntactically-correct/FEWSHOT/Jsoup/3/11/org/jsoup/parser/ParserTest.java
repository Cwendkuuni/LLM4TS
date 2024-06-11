package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParserCreation() {
        Parser parser = new Parser("");
        assertNull(parser.doc);
        assertTrue(parser.stack.isEmpty());
        assertTrue(parser.tq.isEmpty());
        assertEquals("", parser.baseUri);
    }

    @Test
    public void testParseHtml() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        assertEquals("http://example.com/", parser.baseUri);
        assertEquals(htmlTag, parser.stack.getLast().tag());
        assertEquals(doc, parser.stack.getLast());

        Element htmlNode = parser.stack.getLast();
        assertEquals(1, htmlNode.childElementCount());

        Element headNode = htmlNode.child(0);
        assertEquals(headTag, headNode.tag());
        assertEquals(1, headNode.childElementCount());

        Element titleNode = headNode.child(0);
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag, bodyNode.tag());
        assertEquals(1, bodyNode.childElementCount());

        Element pNode = bodyNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Parser parser = new Parser(bodyHtml, "http://example.com/", true);
        Document doc = parser.parse();

        assertEquals("http://example.com/", parser.baseUri);
        assertEquals(htmlTag, parser.stack.getLast().tag());
        assertEquals(doc, parser.stack.getLast());

        Element htmlNode = parser.stack.getLast();
        assertEquals(1, htmlNode.childElementCount());

        Element bodyNode = htmlNode.child(0);
        assertEquals(bodyTag, bodyNode.tag());
        assertEquals(1, bodyNode.childElementCount());

        Element pNode = bodyNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseWithRelaxedParsing() {
        String bodyHtml = "<div><p>Hello World";
        Parser parser = new Parser(bodyHtml, "http://example.com/", true);
        Document doc = parser.parse();

        assertEquals("http://example.com/", parser.baseUri);
        assertEquals(htmlTag, parser.stack.getLast().tag());
        assertEquals(doc, parser.stack.getLast());

        Element htmlNode = parser.stack.getLast();
        assertEquals(1, htmlNode.childElementCount());

        Element divNode = htmlNode.child(0);
        assertEquals("div", divNode.tagName());
        assertEquals(1, divNode.childElementCount());

        Element pNode = divNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        assertEquals(1, parser.stack.size());
        assertEquals(htmlTag, parser.stack.getLast().tag());
        Element htmlNode = parser.stack.getLast();
        assertEquals(2, htmlNode.childElementCount());

        Element commentNode = htmlNode.child(0);
        assertEquals("comment", commentNode.tagName());
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseXMLDeclaration() {
        String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element htmlNode = parser.stack.getLast();
        assertNotNull(htmlNode.xmlDecl());

        assertEquals("http://www.w3.org/2001/XMLSchema-instance", htmlNode.xmlDecl().namespaceUri());
        assertEquals("http://www.w3.org/2001/XMLSchema", htmlNode.xmlDecl().prefix());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        assertEquals("", parser.stack.getLast().tag().tagName());
        assertEquals("body", parser.stack.getLast().previousSibling().tag().tagName());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p id=\"test\">Hello World</p></body></html>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element pNode = doc.body().child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
        assertEquals("test", pNode.id());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div id=\"test\" class=\"foo bar\" title=\"Hello World\"></div>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element divNode = doc.body().child(0);
        assertEquals("div", divNode.tagName());
        Map<String, Attribute> attrs = divNode.attributes();
        assertEquals(3, attrs.size());

        Attribute idAttr = attrs.get("id");
        assertEquals("id", idAttr.getKey());
        assertEquals("test", idAttr.getValue());

        Attribute classAttr = attrs.get("class");
        assertEquals("class", classAttr.getKey());
        assertEquals("foo bar", classAttr.getValue().getString());

        Attribute titleAttr = attrs.get("title");
        assertEquals("title", titleAttr.getKey());
        assertEquals("Hello World", titleAttr.getValue().getString());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element pNode = doc.body().child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[Hello World]]></body></html>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element bodyNode = doc.body();
        TextNode textNode = (TextNode) bodyNode.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><script>alert('Hello World')</script>";
        Parser parser = new Parser(html, "http://example.com/");
        try {
            parser.parse();
            fail("Expected ParserException");
        } catch (ParserException e) {
            // Expected
        }

        assertEquals(htmlTag, parser.stack.getLast().tag());
        assertEquals(1, parser.stack.size());
    }

    @Test
    public void testParseSelfClosingTag() {
        String html = "<html><body><img src=\"test.png\" />";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element imgNode = doc.body().child(0);
        assertTrue(imgNode.hasText());
        assertEquals(TextNode.class, imgNode.child(0).getClass());
        assertEquals("test.png", imgNode.attr("src"));
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html><body><script>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element scriptNode = doc.body().child(0);
        assertNotNull(scriptNode.parent());
        assertEquals("script", scriptNode.tagName());

        Element headNode = scriptNode.parent();
        assertEquals("head", headNode.tagName());
        assertEquals(1, headNode.childElementCount());

        Element titleNode = headNode.child(0);
        assertEquals("title", titleNode.tagName());
        assertEquals("", titleNode.text());

        Element bodyNode = doc.body();
        assertEquals(1, bodyNode.childElementCount());
        assertEquals("body", bodyNode.tagName());
    }

    @Test
    public void testParseInvalidTagName() {
        String html = "<html><body><tag>Hello World</tag>";
        Parser parser = new Parser(html, "http://example.com/");
        try {
            parser.parse();
            fail("Expected ParserException");
        } catch (ParserException e) {
            // Expected
        }

        assertEquals(htmlTag, parser.stack.getLast().tag());
        assertEquals(1, parser.stack.size());
    }

    @Test
    public void testParseInvalidStartTag() {
        String html = "<html><body><p>Hello World<p>";
        Parser parser = new Parser(html, "http://example.com/");
        try {
            parser.parse();
            fail("Expected ParserException");
        } catch (ParserException e) {
            // Expected
        }

        assertEquals(htmlTag, parser.stack.getLast().tag());
        assertEquals(1, parser.stack.size());
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<html><body><img src=\"test.png\" />";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element imgNode = doc.body().child(0);
        assertFalse(imgNode.hasText());
        assertEquals(Element.class, imgNode.child(0).getClass());
        assertEquals("img", imgNode.tagName());
    }

    @Test
    public void testParseInvalidEndTag() {
        String html = "<html><body><p>Hello World</body><p>";
        Parser parser = new Parser(html, "http://example.com/");
        try {
            parser.parse();
            fail("Expected ParserException");
        } catch (ParserException e) {
            // Expected
        }

        assertEquals(htmlTag, parser.stack.getLast().tag());
        assertEquals(1, parser.stack.size());
    }

    @Test
    public void testParseInvalidEndTagOrder() {
        String html = "<html><body><p>Hello World</body></html>";
        Parser parser = new Parser(html, "http://example.com/");
        try {
            parser.parse();
            fail("Expected ParserException");
        } catch (ParserException e) {
            // Expected
        }

        assertEquals(htmlTag, parser.stack.getLast().tag());
        assertEquals(1, parser.stack.size());
    }
}