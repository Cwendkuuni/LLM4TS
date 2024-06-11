package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());

        Element htmlNode = doc.body();
        assertEquals(htmlTag, htmlNode.tag());
        assertNull(htmlNode.prevSibling());
        assertNull(htmlNode.nextSibling());

        Element headNode = htmlNode.previousSibling();
        assertEquals(headTag, headNode.tag());
        assertNull(headNode.prevSibling());
        assertSame(htmlNode, headNode.nextSibling());

        Element titleNode = headNode.children().get(0);
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());
        assertEquals("Test", titleNode.text().text());
        assertNull(titleNode.prevSibling());
        assertSame(headNode, titleNode.parent());
        assertSame(headNode, titleNode.nextSibling());

        Element bodyNode = htmlNode;
        assertEquals(bodyTag, bodyNode.tag());
        assertNull(bodyNode.prevSibling());
        assertSame(htmlNode, bodyNode.parent());
        assertNull(bodyNode.nextSibling());

        Element h1Node = bodyNode.children().get(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello", h1Node.text().text());
        assertEquals("Hello", h1Node.text().toString());
        assertNull(h1Node.prevSibling());
        assertSame(bodyNode, h1Node.parent());
        assertNull(h1Node.nextSibling());

        assertEquals(3, doc.childNodes().size());
        assertEquals(Arrays.asList(htmlNode, headNode, bodyNode), Arrays.asList(doc.childNodes().get(0), doc.childNodes().get(1), doc.childNodes().get(2)));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<head><title>Test</title></head><body><h1>Hello</h1></body>";
        Parser parser = new Parser(bodyHtml, "http://example.com", true);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());

        Element htmlNode = doc.body();
        assertEquals(bodyTag, htmlNode.tag());
        assertNull(htmlNode.prevSibling());
        assertNull(htmlNode.nextSibling());

        Element titleNode = null;
        for (Element child : htmlNode.children()) {
            if (titleTag.equals(child.tag())) {
                titleNode = child;
                break;
            }
        }
        assertNotNull(titleNode);
        assertEquals("Test", titleNode.text());
        assertEquals("Test", titleNode.text().text());
        assertNull(titleNode.prevSibling());
        assertSame(htmlNode, titleNode.parent());
        assertNull(titleNode.nextSibling());

        Element h1Node = htmlNode.children().get(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello", h1Node.text().text());
        assertEquals("Hello", h1Node.text().toString());
        assertNull(h1Node.prevSibling());
        assertSame(htmlNode, h1Node.parent());
        assertNull(h1Node.nextSibling());

        assertEquals(2, doc.childNodes().size());
        assertEquals(Arrays.asList(htmlNode), Arrays.asList(doc.childNodes().get(0)));
        assertNull(doc.head());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- Test --></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tag());

        Element commentNode = ((Comment) doc.childNodes().get(0).child(0));
        assertEquals("Test", commentNode.data());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[ Test ]]></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tag());

        Element cdataNode = ((TextNode) doc.childNodes().get(0).child(0));
        assertEquals("Test", cdataNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html <?xml version=\"1.0\" encoding=\"UTF-8\"?>>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tag());

        Element xmlDeclarationNode = ((XmlDeclaration) doc.childNodes().get(0).child(0));
        assertEquals("UTF-8", xmlDeclarationNode.encoding());
        assertEquals(true, xmlDeclarationNode.isStandalone());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Test</p></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element bodyNode = doc.body();
        Element pNode = bodyNode.children().get(0);
        assertEquals("p", pNode.tagName());

        parser.parseEndTag();
        assertEquals(1, bodyNode.childNodes().size());
        assertEquals(bodyTag, bodyNode.child(0).tag());

        parser.parseEndTag();
        assertEquals(0, bodyNode.childNodes().size());
        assertEquals(null, bodyNode.child(0));
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><script src=\"script.js\"></script></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element bodyNode = doc.body();
        Element scriptNode = bodyNode.children().get(0);
        assertEquals("script", scriptNode.tagName());
        assertEquals("script.js", scriptNode.absUrl("src"));

        parser.parseStartTag();
        assertEquals("html", parser.currentTagName());

        parser.parseStartTag();
        assertEquals("html", parser.currentTagName());

        parser.parseStartTag("body");
        assertEquals("body", parser.currentTagName());

        parser.parseStartTag("script");
        assertEquals("script", parser.currentTagName());

        parser.parseStartTag("div");
        assertEquals("div", parser.currentTagName());

        parser.parseEndTag();
        assertEquals("div", parser.currentTagName());

        parser.parseEndTag();
        assertEquals("script", parser.currentTagName());

        parser.parseEndTag();
        assertEquals("body", parser.currentTagName());

        parser.parseEndTag();
        assertEquals("html", parser.currentTagName());

        parser.parseEndTag();
        assertEquals(null, parser.currentTagName());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body class=\"test\"><p id=\"foo\" title=\"bar\"></p></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element pNode = doc.body().children().get(0);
        Map<String, String> attributes = pNode.attributes();

        assertEquals(2, attributes.size());
        assertEquals("test", attributes.get("class"));
        assertEquals("bar", attributes.get("title"));

        parser.parseStartTag();
        assertEquals(null, parser.currentTagName());

        parser.parseAttribute();
        assertEquals("class", parser.currentAttributeKey());
        assertEquals("test", parser.currentAttributeValue());

        parser.parseAttribute();
        assertEquals("id", parser.currentAttributeKey());
        assertEquals("foo", parser.currentAttributeValue());

        parser.parseAttribute();
        assertEquals("title", parser.currentAttributeKey());
        assertEquals("bar", parser.currentAttributeValue());

        parser.parseAttribute();
        assertEquals(null, parser.currentAttributeKey());
        assertEquals(null, parser.currentAttributeValue());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Test</p></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element pNode = doc.body().children().get(0);
        assertEquals("Test", pNode.text().toString());
        assertEquals("Test", pNode.text().text());

        parser.parseTextNode();
        assertEquals("Test", parser.currentText());

        parser.parseTextNode();
        assertEquals("", parser.currentText());

        parser.parseAttribute();
        assertEquals(null, parser.currentText());

        parser.parseStartTag();
        assertEquals(null, parser.currentText());
    }

    @Test
    public void testParseInvalidInput() {
        String html = "<html><body><p>Test<";
        Parser parser = new Parser(html, "http://example.com");

        try {
            parser.parse();
            fail();
        } catch (RuntimeException e) {
            // expected
        }
    }
}