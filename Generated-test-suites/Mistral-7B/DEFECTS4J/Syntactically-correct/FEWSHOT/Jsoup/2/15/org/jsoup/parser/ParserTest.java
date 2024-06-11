package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(htmlTag, htmlNode.tag());
        Element headNode = htmlNode.child(0);
        assertEquals(headTag, headNode.tag());
        Element titleNode = headNode.child(0);
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());
        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag, bodyNode.tag());
        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());
        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBasicAttributes() {
        String html = "<div id='test' class='testclass' title='Test Title'>Test Content</div>";
        Document doc = Parser.parse(html, "http://example.com");

        Element divNode = doc.selectFirst("div[id=test]");
        assertEquals("test", divNode.attr("id"));
        assertEquals("testclass", divNode.attr("class"));
        assertEquals("Test Title", divNode.attr("title"));
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<img src='image.jpg' />";
        Document doc = Parser.parse(html, "http://example.com");

        Element imgNode = doc.selectFirst("img");
        assertEquals("img", imgNode.tagName());
        assertTrue(imgNode.attributes().isEmpty());
    }

    @Test
    public void testParseComments() {
        String html = "<html><!-- This is a comment --><head><title>Test</title></head><body><h1>Hello World</h1><!-- Another comment --></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element commentNode1 = doc.selectFirst("html>/*[position()=1()]");
        assertEquals(Comment.class, commentNode1.getClass());
        assertEquals("This is a comment", commentNode1.text());
        Element commentNode2 = doc.selectFirst("body>/*[position()=3()]");
        assertEquals(Comment.class, commentNode2.getClass());
        assertEquals("Another comment", commentNode2.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><script><![CDATA[function test(){alert('Hello World');}]]></script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element scriptNode = doc.selectFirst("script");
        assertEquals("<script><![CDATA[function test(){alert('Hello World');}]]></script>", scriptNode.outerHtml());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href='/test/></head><body><a href='about.html'>About</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("http://test.com/about.html", doc.baseUri());
        assertEquals("http://test.com/about.html", doc.selectFirst("a").attr("href"));
    }
}