package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element root = doc.body();
        assertEquals("Hello World", root.tagName());
        assertEquals("h1", root.tag().getName());
        assertEquals("Test", doc.head().title().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Parser parser = new Parser(bodyHtml, "http://example.com");
        Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element root = doc.body();
        assertEquals("Hello World", root.tagName());
        assertEquals("h1", root.tag().getName());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div class=\"test\">Hello World</div>";
        Parser parser = new Parser(bodyHtml, "http://example.com");
        Document doc = parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        Element root = doc.body();
        assertEquals("Hello World", root.text());
    }

    @Test
    public void testParseEmptyFragment() {
        String bodyHtml = "";
        Parser parser = new Parser(bodyHtml, "http://example.com");
        Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertTrue(doc.body().tagName().isEmpty());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element root = doc.body();
        Element img = root.getElementsByTag("img").get(0);
        Element parent = img.parent();

        assertNotNull(parent);
        assertEquals("img", parent.tagName());
        assertEquals("div", parent.tag().getName()); // should have created an implicit div parent
    }

    @Test
    public void testParseInvalidChild() {
        String html = "<html><body><h1>Hello</h1><table><tr><td>World</td></tr></table></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element root = doc.body();
        Element h1 = root.getElementsByTag("h1").get(0);
        Element table = root.getElementsByTag("table").get(0);

        assertFalse(h1.getParent().equals(table)); // h1 should not be a child of table
    }

    @Test
    public void testParseInvalidBaseUri() {
        String html = "<base href=\"/images/\">";
        Parser parser = new Parser("<html><body><img src=\"image.png\"></body></html>", null);
        Document doc = parser.parse();

        assertEquals("image.png", doc.imageUri("image.png")); // baseUri should be set to "/images/"
    }

}