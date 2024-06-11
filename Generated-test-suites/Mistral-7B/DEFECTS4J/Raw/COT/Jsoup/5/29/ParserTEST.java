package org.jsoup.parser;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

import java.util.LinkedList;

/**
 * Test class for Parser.
 */
public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        String html = "<html><head><title>Test Title</title></head><body>" +
                "<h1 id='test-id'>Test Heading</h1>" +
                "<p>Test Paragraph.</p>" +
                "</body></html>";
        parser = new Parser(html, "http://example.com/");
    }

    

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.getElementById("test-id").text());
        assertEquals("Test Paragraph.", doc.body().text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser parser = new Parser(null, "http://example.com/", false);
        parser.parse(); // This should throw an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        Parser parser = new Parser("<html></html>", null, false);
        parser.parse(); // This should throw an exception
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<body><p>Test Paragraph.</p></body>", "http://example.com/");
        assertEquals("Test Paragraph.", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<body><p>Test Paragraph.</p></body>", "http://example.com/");
        assertEquals("Test Paragraph.", doc.body().text());
    }

    @Test
    public void testParseEmptyHtml() {
        Document doc = parser.parse();
        assertEquals("", doc.body().text());
        assertEquals(0, doc.childElements().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyBaseUri() {
        Parser parser = new Parser("<html></html>", "", false);
        parser.parse(); // This should throw an exception
    }

    @Test
    public void testParseStartTag() {
        parser.tq.input("html<head><title>Test Title</title><body><h1 id='test-id'>Test Heading</h1></body>");
        parser.parseStartTag();
        assertEquals("html", parser.last().tagName());

        parser.tq.input("h1");
        parser.parseStartTag();
        assertEquals("h1", parser.last().tagName());

        parser.tq.input("id='test-id'");
        Attribute attribute = parser.parseAttribute();
        assertEquals("test-id", attribute.key());
        assertEquals("", attribute.value());
    }

    @Test
    public void testParseEndTag() {
        parser.parseStartTag();
        parser.parseStartTag("body");
        parser.parseStartTag("h1");
        parser.parseEndTag();
        assertEquals(2, parser.stack.size());
        assertEquals("body", parser.stack.getLast().tagName());

        parser.parseEndTag();
        assertEquals(1, parser.stack.size());
        assertEquals("html", parser.stack.getLast().tagName());
    }

    @Test
    public void testParseComment() {
        parser.parseStartTag();
        parser.parseStartTag("!--Test Comment-->");
        assertEquals(2, parser.stack.size());
        assertTrue(parser.stack.getLast() instanceof Comment);

        parser.parseComment();
        assertEquals(1, parser.stack.size());
        assertEquals("html", parser.stack.getLast().tagName());
    }

    @Test
    public void testParseCdata() {
        parser.parseStartTag();
        parser.parseStartTag("![CDATA[Test Cdata]]]>");
        assertEquals(2, parser.stack.size());
        assertTrue(parser.stack.getLast() instanceof TextNode);
        assertEquals("Test Cdata", ((TextNode)parser.stack.getLast()).text());

        parser.parseCdata();
        assertEquals(1, parser.stack.size());
        assertEquals("html", parser.stack.getLast().tagName());
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseStartTag();
        parser.parseStartTag("<?xml version=\"1.0\"?>");
        assertEquals(2, parser.stack.size());
        assertTrue(parser.stack.getLast() instanceof XmlDeclaration);

        parser.parseXmlDecl();
        assertEquals(1, parser.stack.size());
        assertEquals("html", parser.stack.getLast().tagName());
    }

    @Test
    public void testParseTextNode() {
        parser.parseStartTag();
        parser.parseTextNode("Test Text Node");
        assertEquals(2, parser.stack.size());
        assertTrue(parser.stack.getLast() instanceof TextNode);
        assertEquals("Test Text Node", ((TextNode)parser.stack.getLast()).text());

        parser.parseTextNode();
        assertEquals(1, parser.stack.size());
        assertEquals("html", parser.stack.getLast().tagName());
    }

    @Test
    public void testParseAttribute() {
        parser.parseStartTag("p");
        parser.parseAttribute("data-test");
        parser.parseEndTag();

        assertEquals(1, parser.stack.size());
        assertTrue(parser.stack.getLast() instanceof Element);
        assertEquals("p", parser.stack.getLast().tagName());
        assertEquals(1, parser.stack.getLast().attributeCount());
        assertEquals("data-test", parser.stack.getLast().tag().attributeName("data-test").key());
        assertEquals("", parser.stack.getLast().tag().attributeName("data-test").value());
    }

    @Test
    public void testParseEmptyElement() {
        parser.parseStartTag("img");
        parser.parseEndTag();
        assertEquals(1, parser.stack.size());
        assertEquals("html", parser.stack.getLast().tagName());
    }

    @Test
    public void testParseInvalidTag() {
        parser.parseStartTag();
        parser.parseStartTag("x:invalidTag");

        // This should throw an exception and not parse further
        try {
            parser.parseEndTag();
            fail("Exception expected");
        } catch (RuntimeException e) {
            // Expected
        }

        assertEquals(1, parser.stack.size());
        assertEquals("html", parser.stack.getLast().tagName());
    }

    @Test
    public void testParseTagWithImplicitParent() {
        parser.parseStartTag("script src='test.js'");
        parser.parseEndTag();
        assertEquals(1, parser.stack.size());
        assertEquals("html", parser.stack.getLast().tagName());

        parser.parseStartTag("link rel='stylesheet' href='styles.css'");
        parser.parseEndTag();
        assertEquals(1, parser.stack.size());
        assertEquals("html", parser.stack.getLast().tagName());

        Element head = parser.stack.getLast();
        assertEquals(2, head.childElements().size());

        Element script = head.getFirstChild();
        assertEquals("script", script.tagName());
        assertEquals("test.js", script.absUrl("src"));

        Element link = head.getLastChild();
        assertEquals("link", link.tagName());
        assertEquals("styles.css", link.absUrl("href"));
    }

    @Test
    public void testParseTagWithSelfClosing() {
        parser.parseStartTag("img src='test.jpg'/>");
        assertEquals(1, parser.stack.size());
        assertEquals("html", parser.stack.getLast().tagName());

        Element img = parser.stack.getLast();
        assertEquals("img", img.tagName());
        assertTrue(img.isSelfClosing());
        assertEquals("test.jpg", img.absUrl("src"));
    }

    @Test
    public void testParseTagWithImplicitParentAndSelfClosing() {
        parser.parseStartTag("<html><head><title>Test Title</title></head><body><img src='test.jpg'/></body></html>");
        Document doc = parser.parse();

        assertEquals("Test Title", doc.title().text());
        assertEquals("test.jpg", doc.body().getElementsByTag("img").first().absUrl("src"));
    }

    @Test
    public void testParseTagWithBaseTag() {
        parser.parseStartTag("<html><base href='/foo'><head><title>Test Title</title></head><body><a href='bar/baz.html'>Test Link</a></body></html>");
        Document doc = parser.parse();

        assertEquals("Test Title", doc.title().text());
        assertEquals("/foo/bar/baz.html", doc.getElementsByTag("a").first().absUrl("href"));
    }

    @Test
    public void testParseTagWithInvalidParent() {
        parser.parseStartTag("<html><head><title>Test Title</title></head><body><div><h1>Test Heading</h1></div></body></html>");

        // This should throw an exception and not parse further
        try {
            parser.parse();
            fail("Exception expected");
        } catch (RuntimeException e) {
            // Expected
        }

        assertEquals(6, parser.stack.size());
        assertEquals("html", parser.stack.get(0).tagName());
        assertEquals("head", parser.stack.get(1).tagName());
        assertEquals("title", parser.stack.get(2).tagName());
        assertEquals("body", parser.stack.get(3).tagName());
        assertEquals("div", parser.stack.get(4).tagName());
        assertEquals("h1", parser.stack.get(5).tagName());
    }

    @Test
    public void testParseTagWithInvalidParentAndSelfClosing() {
        parser.parseStartTag("<html><head><title>Test Title</title></head><body><div><p /></div></body></html>");

        // This should throw an exception and not parse further
        try {
            parser.parse();
            fail("Exception expected");
        } catch (RuntimeException e) {
            // Expected
        }

        assertEquals(5, parser.stack.size());
        assertEquals("html", parser.stack.get(0).tagName());
        assertEquals("head", parser.stack.get(1).tagName());
        assertEquals("title", parser.stack.get(2).tagName());
        assertEquals("body", parser.stack.get(3).tagName());
        assertEquals("div", parser.stack.get(4).tagName());
    }

    
}