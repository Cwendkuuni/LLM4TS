package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.LinkedList;

import org.junit.Test;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    // Test case for Parser constructor
    @Test
    public void testParserConstructor() {
        String html = "<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Paragraph", doc.body().text());
    }

    // Test case for parse method
    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>";
        Parser parser = new Parser();
        Document doc = parser.parse(html, "http://test.com");
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Paragraph", doc.body().text());
    }

    // Test case for parseBodyFragment method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test Paragraph</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        assertEquals("", doc.head().text());
        assertEquals("Test Paragraph", doc.body().text());
    }

    // Test case for parseBodyFragmentRelaxed method
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<script type=\"text/javascript\">function Test(){}</script>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
        Element script = doc.body().child(0);
        assertEquals("text/javascript", script.attr("type"));
    }

    // Test case for parse method with empty tag
    @Test
    public void testParseEmptyTag() {
        String html = "<img src=\"test.png\" />";
        Parser parser = new Parser();
        Document doc = parser.parse(html, "http://test.com");
        Element img = doc.body().child(0);
        assertTrue(img.tag().isSelfClosing());
    }

    // Test case for parse method with unknown self-closing tag
    @Test
    public void testParseUnknownSelfClosingTag() {
        String html = "<mytag />";
        Parser parser = new Parser();
        Document doc = parser.parse(html, "http://test.com");
        Element mytag = doc.body().child(0);
        assertTrue(mytag.tag().isSelfClosing());
    }

    // Test case for parse method with empty body fragment
    @Test
    public void testParseEmptyBodyFragment() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        assertEquals("", doc.body().text());
    }

    // Test case for parse method with implicit parent
    @Test
    public void testParseImplicitParent() {
        String html = "<div><img src=\"test.png\"></div>";
        Parser parser = new Parser();
        Document doc = parser.parse(html, "http://test.com");
        Element img = doc.body().child(0).child(0);
        assertEquals("img", img.tag().tagName());
    }

    // Test case for parse method with invalid ancestor
    @Test(expected = AssertionError.class)
    public void testParseInvalidAncestor() {
        String html = "<html><body><div></div></html>";
        Parser parser = new Parser();
        Document doc = parser.parse(html, "http://test.com");
        Element div = doc.body().child(0);
        parser.addChildToParent(new Element(htmlTag, ""), false);
        parser.addChildToParent(new Element(bodyTag, ""), false);
        parser.addChildToParent(div, false);
        parser.addChildToParent(new Element(headTag, ""), false); // should throw an AssertionError
    }

    // Test case for parse method with relaxed parsing
    @Test
    public void testParseRelaxed() {
        String html = "<script src=\"test.js\"></script>";
        Parser parser = new Parser();
        parser.relaxed = true;
        Document doc = parser.parse(html, "http://test.com");
        Element script = doc.body().child(0);
        assertEquals("script", script.tag().tagName());
        assertNull(script.text());
    }
}