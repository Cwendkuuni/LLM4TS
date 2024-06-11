package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.*;

import java.util.LinkedList;

/**
 * Test class for the Parser class.
 */
public class ParserTest {

    @Test
    public void testParser() {
        // Test parsing a simple HTML document
        String html = "<html><head><title>Test Parser</title></head>" +
                "<body><h1>Hello World</h1></body></html>";

        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        Element headNode = doc.head();
        Element titleNode = headNode.child(0);

        assertEquals("html", htmlNode.tagName());
        assertEquals("head", headNode.tagName());
        assertEquals("title", titleNode.tagName());
        assertEquals("Test Parser", titleNode.text());

        // Test parsing a fragment of HTML
        html = "<body><h1>Hello</h1><p>World</p></body>";

        doc = Parser.parseBodyFragment(html, "http://example.com");

        htmlNode = doc;
        Element h1Node = htmlNode.child(0);
        Element pNode = htmlNode.child(1);

        assertEquals("body", htmlNode.tagName());
        assertEquals("h1", h1Node.tagName());
        assertEquals("p", pNode.tagName());
        assertEquals("Hello", h1Node.text());
        assertEquals("World", pNode.text());

        // Test parsing a relaxed fragment of HTML
        html = "<body><h1>Hello</h1><p>World</p></body>";

        doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com");

        htmlNode = doc;
        h1Node = htmlNode.child(0);
        pNode = htmlNode.child(1);

        assertEquals("body", htmlNode.tagName());
        assertEquals("h1", h1Node.tagName());
        assertEquals("p", pNode.tagName());
        assertEquals("Hello", h1Node.text());
        assertEquals("World", pNode.text());

        // Test handling of empty elements and self-closing elements
        html = "<img src=\"image.png\" alt=\"image\"/>";

        doc = Parser.parseBodyFragment(html, "http://example.com");

        Element imgNode = doc.body().child(0);

        assertEquals("img", imgNode.tagName());
        assertTrue(imgNode.isSelfClosing());
        assertFalse(imgNode.hasChildren());

        // Test handling of comments and CDATA sections
        html = "<html><!-- Test comment --><script><![CDATA[ Java code ]]></script></html>";

        doc = Parser.parse(html, "http://example.com");

        Comment commentNode = doc.body().child(0);
        Element scriptNode = doc.body().child(1);

        assertEquals("!-- Test comment --", commentNode.text());
        assertTrue(scriptNode.hasChildren());
        assertTrue(scriptNode.child(0) instanceof TextNode);
        assertEquals(" Java code ", scriptNode.child(0).text());

        // Test handling of base tag
        html = "<html><head><base href=\"http://sub.example.com\"/><title>Test Parser</title></head>" +
                "<body><a href=\"link.html\">Link</a></body></html>";

        doc = Parser.parse(html, "http://example.com");

        assertEquals("http://sub.example.com", doc.baseUri());
        assertEquals("http://example.com/link.html", doc.select("a[href]").get(0).absUrl("href"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        Parser.parse("<html></html>", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyHtml() {
        Parser.parse("", "http://example.com");
    }
}