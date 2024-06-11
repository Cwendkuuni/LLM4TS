package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Element htmlTag = doc.select("html").first();
        assertTrue("Tag 'html' not found", htmlTag != null);
        
        Element headTag = htmlTag.select("head").first();
        assertTrue("Tag 'head' not found", headTag != null);
        
        Element titleTag = headTag.select("title").first();
        assertTrue("Tag 'title' not found", titleTag != null);
        assertEquals("Test", titleTag.text());
        
        Element bodyTag = htmlTag.select("body").first();
        assertTrue("Tag 'body' not found", bodyTag != null);
        
        Element h1Tag = bodyTag.select("h1").first();
        assertTrue("Tag 'h1' not found", h1Tag != null);
        assertEquals("Hello World!", h1Tag.text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        Element h1Tag = doc.select("h1").first();
        assertTrue("Tag 'h1' not found", h1Tag != null);
        assertEquals("Hello World!", h1Tag.text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Comment comment = doc.select("comment").first();
        assertTrue("Comment not found", comment != null);
        assertEquals(" This is a comment ", comment.getData());
    }

    @Test
    public void testParseTextNode() {
        String html = "Hello World!";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        TextNode textNode = doc.textNodes().first();
        assertTrue("Text node not found", textNode != null);
        assertEquals("Hello World!", textNode.text());
    }
}