package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isInstanceOf;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());
        Element headNode = htmlNode.child(0);
        assertEquals("head", headNode.tagName());
        Element titleNode = headNode.child(0);
        assertEquals("title", titleNode.tagName());
        assertEquals("Test Page", titleNode.text());
        Element bodyNode = htmlNode.child(1);
        assertEquals("body", bodyNode.tagName());
        Element pNode = bodyNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc;
        assertEquals("html", root.tagName());
        assertEquals(0, root.childSize());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        Element root = doc;
        assertEquals("html", root.tagName());
        Element body = root.child(0);
        assertEquals("body", body.tagName());
        Element p = body.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello < World </p>"; // missing > and whitespace
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        Element root = doc;
        assertEquals("html", root.tagName());
        Element body = root.child(0);
        assertEquals("body", body.tagName());
        Element p = body.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello < World ", p.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyBaseUri() {
        Parser.parse("<html></html>", "");
    }

    @Test
    public void testParseInvalidStartTag() {
        String html = "<html<title>Test";
        try {
            Parser.parse(html, "http://example.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseInvalidEndTag() {
        String html = "<html><body></p></html>";
        try {
            Parser.parse(html, "http://example.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseEmptyTag() {
        String html = "<img />";
        Document doc = Parser.parse(html, "http://example.com");
        Element img = doc.body().child(0);
        assertTrue(img.tag().isSelfClosing());
        assertEquals(0, img.childSize());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element htmlNode = doc.body();
        assertEquals(1, htmlNode.childSize());
        Element commentNode = htmlNode.child(0);
        assertEquals("comment", commentNode.tagName());
        assertEquals("This is a comment ", commentNode.text());
    }

    @Test
    public void testParseXmlDeclaration() {
        String html = "<
    xml version='1.0' encoding='UTF-8'>\n<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element xmlDeclaration = doc.body().child(0);
        assertEquals("xml-declaration", xmlDeclaration.tagName());
        assertEquals("1.0", xmlDeclaration.attribute("version").getValue());
        assertEquals("UTF-8", xmlDeclaration.attribute("encoding").getValue());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href='/test'></base></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("/test", doc.baseUri());
    }

    @Test
    public void testParseBaseTagWithEmptyValue() {
        String html = "<html><head><base href=''></base></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("", doc.baseUri());
    }

    @Test
    public void testParseScriptTag() {
        String html = "<html><head></head><body><script type='text/javascript'>console.log('Hello World');</script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element scriptNode = doc.body().child(0);
        assertEquals("script", scriptNode.tagName());
        assertEquals("text/javascript", scriptNode.attribute("type").getValue());
        assertEquals("console.log('Hello World');", scriptNode.text());
    }

    @Test
    public void testParseTextareaTag() {
        String html = "<html><head></head><body><textarea cols='20' rows='5'>Hello World</textarea></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element textareaNode = doc.body().child(0);
        assertEquals("textarea", textareaNode.tagName());
        assertEquals("20", textareaNode.attribute("cols").getValue());
        assertEquals("5", textareaNode.attribute("rows").getValue());
        assertEquals("Hello World", textareaNode.text());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<div><input type='text' name='username'></div>";
        Document doc = Parser.parse(html, "http://example.com");
        Element divNode = doc.body().child(0);
        assertEquals("div", divNode.tagName());
        Element inputNode = divNode.child(0);
        assertEquals("input", inputNode.tagName());
        assertEquals("text", inputNode.attribute("type").getValue());
        assertEquals("username", inputNode.attribute("name").getValue());
    }

    @Test
    public void testParseInvalidParent() {
        String html = "<html><body><div><p>Hello World</p></div></body></html>";
        try {
            Parser.parse(html, "http://example.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

}