package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

public class ParserTest {

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Document document = Parser.parse(html, "http://example.com");
        assertEquals("http://example.com", document.baseUri());
        assertEquals(Parser.htmlTag, document.tagName());
        assertEquals(0, document.childElementCount());
    }

    @Test
    public void testParseSimpleHtml() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document document = Parser.parse(html, "http://example.com");

        assertEquals("http://example.com", document.baseUri());
        assertEquals(Parser.htmlTag, document.tagName());
        assertEquals(Parser.bodyTag, document.body().tagName());
        assertEquals(1, document.childElementCount());

        Element body = document.body();
        assertEquals(Parser.pTag, body.tagName());
        assertEquals(1, body.childElementCount());

        Element p = body.firstChild();
        assertEquals("Hello World", p.text().toString());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals("http://example.com", document.baseUri());
        assertEquals(Parser.bodyTag, document.tagName());
        assertEquals(1, document.childElementCount());

        Element body = document.body();
        assertEquals(Parser.pTag, body.tagName());
        assertEquals(1, body.childElementCount());

        Element p = body.firstChild();
        assertEquals("Hello World", p.text().toString());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        assertEquals("http://example.com", document.baseUri());
        assertEquals(Parser.bodyTag, document.tagName());
        assertEquals(1, document.childElementCount());

        Element body = document.body();
        assertEquals(Parser.pTag, body.tagName());
        assertEquals(1, body.childElementCount());

        Element p = body.firstChild();
        assertEquals("Hello World", p.text().toString());
    }

    @Test
    public void testParseComplexHtml() {
        String html = "<html xmlns:x=\"http://example.com/x\" xml:lang=\"en\">" +
                "<head><title>Test Page</title></head>" +
                "<body><p id=\"p1\">Hello <em>World</em></p>" +
                "<script type=\"text/javascript\" src=\"script.js\"></script>" +
                "</body></html>";

        Document document = Parser.parse(html, "http://example.com");

        assertEquals("http://example.com", document.baseUri());
        assertEquals(Parser.htmlTag, document.tagName());
        assertEquals(Parser.headTag, document.head().tagName());
        assertEquals(1, document.head().childElementCount());
        assertEquals(Parser.titleTag, document.head().firstChild().tagName());
        assertEquals("Test Page", document.head().firstChild().text().toString());

        assertEquals(Parser.bodyTag, document.body().tagName());
        assertEquals(1, document.body().childElementCount());
        assertEquals(Parser.pTag, document.body().firstChild().tagName());
        assertEquals(1, document.body().firstChild().childElementCount());
        assertEquals(Parser.emTag, document.body().firstChild().firstChild().tagName());
        assertEquals("World", document.body().firstChild().firstChild().text().toString());
        assertEquals("p1", document.body().firstChild().id());

        assertEquals(1, document.getElementsByTag("script").size());
        assertEquals("script.js", document.getElementsByTag("script").get(0).attr("src"));
    }

    @Test
    public void testParseXmlDeclaration() {
        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                "<html xmlns:x=\"http://example.com/x\" xml:lang=\"en\">" +
                "<head><title>Test Page</title></head>" +
                "<body><p id=\"p1\">Hello <em>World</em></p>" +
                "<script type=\"text/javascript\" src=\"script.js\"></script>" +
                "</body></html>";

        Document document = Parser.parse(html, "http://example.com");
        assertEquals("UTF-8", document.characterSet());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Document document = Parser.parse(html, "http://example.com");
        assertEquals(1, document.body().childNodeSize());
        assertEquals(Parser.Comment.class, document.body().child(0).getClass());
        assertEquals("This is a comment", document.body().child(0).text().toString());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[This is CDATA]]><body></body></html>";
        Document document = Parser.parse(html, "http://example.com");
        assertEquals(1, document.body().childNodeSize());
        assertEquals(Parser.DataNode.class, document.body().child(0).getClass());
        assertEquals("This is CDATA", document.body().child(0).text().toString());
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<img alt=\"\" />";
        Document document = Parser.parse(html, "http://example.com");
        assertEquals(1, document.body().childElementCount());

        Element img = document.body().firstChild();
        assertEquals(Parser.imgTag, img.tagName());
        assertEquals(0, img.childElementCount());
        assertEquals(0, img.childNodeSize());
        assertNull(img.text());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html><body><img src=\"image.png\" /></body></html>";
        Document document = Parser.parse(html, "http://example.com");

        Element body = document.body();
        assertEquals(1, body.childElementCount());

        Element img = body.firstChild();
        assertEquals(Parser.imgTag, img.tagName());
        assertEquals(0, img.childElementCount());
        assertEquals(0, img.childNodeSize());
        assertNull(img.text());

        Element parent = body.parent();
        assertEquals(Parser.htmlTag, parent.tagName());
        assertEquals(1, parent.childElementCount());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><img src=\"image.png\" ></body></html>";
        try {
            Parser.parse(html, "http://example.com");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}