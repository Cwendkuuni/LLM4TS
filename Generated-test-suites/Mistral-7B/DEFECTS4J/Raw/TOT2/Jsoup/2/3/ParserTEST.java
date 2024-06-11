package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {
    // Test case for parse method
    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element htmlNode = doc.head();
        Assert.assertEquals("html", htmlNode.tagName());

        Element titleNode = doc.head().title();
        Assert.assertEquals("Test Page", titleNode.text());

        Element bodyNode = doc.body();
        Assert.assertEquals("body", bodyNode.tagName());

        Element h1Node = bodyNode.child(0);
        Assert.assertEquals("h1", h1Node.tagName());
        Assert.assertEquals("Hello World", h1Node.text());
    }

    // Test case for parseBodyFragment method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Parser parser = new Parser(bodyHtml, "http://example.com");
        Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc.body();
        Assert.assertEquals("body", bodyNode.tagName());

        Element h1Node = bodyNode.child(0);
        Assert.assertEquals("h1", h1Node.tagName());
        Assert.assertEquals("Hello World", h1Node.text());
    }

    // Test case for parse method with empty input
    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyInput() {
        String html = "";
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
    }

    // Test case for parse method with null input
    @Test(expected = NullPointerException.class)
    public void testParseNullInput() {
        String html = null;
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
    }
}