package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.body().children().get(0).text());
        assertEquals("http://test.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test Heading</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");

        assertEquals("http://test.com", doc.baseUri());
        assertEquals(bodyTag, doc.tag());
        assertEquals(1, doc.body().children().size());
        assertEquals("Test Heading", doc.body().children().get(0).text());
    }

    @Test
    public void testParseAttributes() {
        String html = "<div id='test' class='testClass'><p>Test text</p></div>";
        Document doc = Parser.parse(html, "http://test.com");
        Element div = doc.body().children().get(0);

        assertEquals("test", div.id());
        assertEquals("testClass", div.classes().get(0));
        assertEquals(1, div.children().size());
        assertEquals("Test text", div.children().get(0).text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment --><html><body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertTrue(doc.body().children().get(0).children().get(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) doc.body().children().get(0).children().get(0)).text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version='1.0'?><html><body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertTrue(doc instanceof XmlDocument);
        assertEquals("1.0", doc.xmlVersion());
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><p>Test text</p></div>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals(1, doc.body().children().size());
        assertEquals("div", doc.body().children().get(0).tagName());
        assertEquals(1, doc.body().children().get(0).children().size());
        assertEquals("p", doc.body().children().get(0).children().get(0).tagName());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div id='test' class='testClass'><p>Test text</p></div>";
        Document doc = Parser.parse(html, "http://test.com");
        Element div = doc.body().children().get(0);

        assertEquals(1, div.children().size());
        assertEquals("p", div.children().get(0).tagName());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Test text</p>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals(1, doc.body().children().size());
        assertEquals("p", doc.body().children().get(0).tagName());
        assertEquals(1, doc.body().children().get(0).children().size());
        assertEquals("Test text", doc.body().children().get(0).children().get(0).text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]><html><body></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals(1, doc.body().children().size());
        assertTrue(doc.body().children().get(0) instanceof TextNode);
        assertEquals("This is CDATA", doc.body().children().get(0).text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href='/new-dir'><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals("/new-dir", doc.baseUri());
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<img src='test.png'/>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals(1, doc.body().children().size());
        assertEquals("img", doc.body().children().get(0).tagName());
        assertTrue(doc.body().children().get(0).isEmpty());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<script src='test.js'></script>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals(1, doc.body().children().size());
        assertEquals("script", doc.body().children().get(0).tagName());
        assertTrue(doc.body().children().get(0).isEmpty());
        assertEquals(1, doc.body().children().get(0).children().size());
        assertEquals("script", doc.body().children().get(0).children().get(0).tagName());
    }

    @Test
    public void testParseInvalidParent() {
        String html = "<html><body><div><p>Test text</p></div><p>Test text</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals(2, doc.body().children().size());
        assertEquals("div", doc.body().children().get(0).tagName());
        assertEquals(1, doc.body().children().get(0).children().size());
        assertEquals("p", doc.body().children().get(1).tagName());
        assertEquals(1, doc.body().children().get(1).children().size());
        assertEquals("Test text", doc.body().children().get(1).children().get(0).text());
    }
}