package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.documentElementTag());
        assertEquals(3, doc.childElementCount());

        Element head = doc.head();
        assertEquals("head", head.tagName());
        assertNull(head.text());

        Element title = head.child(0);
        assertEquals("title", title.tagName());
        assertEquals("Test", title.text());

        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(1, body.childElementCount());

        Element h1 = body.child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello</h1></body>";
        Parser parser = new Parser(bodyHtml, "http://example.com", true);
        Document doc = parser.parse();

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.documentElementTag());
        assertEquals(1, doc.childElementCount());

        Element bodyFragment = doc.body();
        assertEquals("body", bodyFragment.tagName());
        assertEquals(1, bodyFragment.childElementCount());

        Element h1 = bodyFragment.child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello", h1.text());
    }

    @Test
    public void testParseEmpty() {
        String html = "";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.documentElementTag());
        assertEquals(0, doc.childElementCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        String html = null;
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
        fail("Should have thrown an IllegalArgumentException");
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html<body";
        Parser parser = new Parser(html, "http://example.com");

        try {
            parser.parse();
            fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            // Expected
        }

        assertEquals("http://example.com", parser.getBaseUri());
        assertEquals(1, parser.getConsumedTokenCount());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment -->";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element comment = doc.head();
        assertEquals("--", comment.tagName());
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        XmlDeclaration xmlDeclaration = doc.head().child(0);
        assertEquals("xml", xmlDeclaration.namespace());
        assertEquals("http://www.w3.org/1999/xhtml", xmlDeclaration.getNamespaceUri());
        assertEquals("en", xmlDeclaration.language());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello</p></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        assertEquals(3, doc.childElementCount());

        Element body = doc.body();
        assertEquals(1, body.childElementCount());

        Element p = body.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello", p.text());

        Element htmlElement = doc.documentElement();
        assertEquals("html", htmlElement.tagName());
        assertEquals(3, htmlElement.childElementCount());

        parser.parseEndTag("p");
        assertEquals(2, htmlElement.childElementCount());

        assertEquals("Hello", p.text());
        assertNull(htmlElement.child(1));
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1>Hello</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element body = doc.body();
        assertEquals(1, body.childElementCount());

        Element h1 = body.child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello", h1.text());

        Element htmlElement = doc.documentElement();
        assertEquals("html", htmlElement.tagName());
        assertEquals(3, htmlElement.childElementCount());

        parser.parseStartTag("p");
        parser.parseAttribute("class", "test");
        parser.parseAttribute("id", "testID");

        Element p = htmlElement.appendChild(new Element("p"));
        assertEquals("p", p.tagName());
        assertEquals(2, p.attributeCount());
        assertEquals("test", p.className());
        assertEquals("testID", p.id());

        parser.parseEndTag("p");
        assertEquals(2, htmlElement.childElementCount());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element body = doc.body();
        Element p = body.child(0);

        TextNode textNode = (TextNode) p.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[Hello World]]></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element body = doc.body();
        TextNode textNode = (TextNode) body.child(0);
        assertEquals("Hello World", textNode.text());
    }
}