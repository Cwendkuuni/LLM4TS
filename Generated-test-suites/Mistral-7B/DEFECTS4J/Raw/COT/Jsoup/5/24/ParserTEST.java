package org.jsoup.parser; // import necessary classes here

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Parser.TokenQueue;
import org.junit.Test;

public class ParserTest {
    // Test case for the parse method
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element htmlNode = doc.head();
        Element titleNode = htmlNode.child(0);
        Element bodyNode = doc.body();
        Element h1Node = bodyNode.child(0);

        assertEquals("Test", titleNode.text());
        assertEquals("Hello World!", h1Node.text());
    }

    // Test case for parseBodyFragment method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        Element h1Node = doc.body().child(0);

        assertEquals("Hello World!", h1Node.text());
    }

    // Test case for parseBodyFragmentRelaxed method
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<html><body><h1>Hello World!</h1></body>"; // missing html tag
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
        Element h1Node = doc.body().child(0);

        assertEquals("Hello World!", h1Node.text());
    }

    // Test case for parse method with an empty string
    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyString() {
        Parser.parse("", "http://test.com");
    }

    // Test case for parseBodyFragment method with an empty string
    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentEmptyString() {
        Parser.parseBodyFragment("", "http://test.com");
    }

    // Test case for parse method with null baseUri
    @Test(expected = NullPointerException.class)
    public void testParseNullBaseUri() {
        Parser.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>", null);
    }

    // Test case for parseBodyFragment method with null baseUri
    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentNullBaseUri() {
        Parser.parseBodyFragment("<h1>Hello World!</h1>", null);
    }

    // Test case for parse method with invalid tag name
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTagName() {
        String html = "<html><body><zab tag='invalid'></zab></body></html>";
        Parser parser = new Parser(html, "http://test.com", false);
        parser.parse();
    }

    // Test case for parseBodyFragment method with invalid tag name
    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentInvalidTagName() {
        String bodyHtml = "<zab tag='invalid'></zab>";
        Parser parser = new Parser(bodyHtml, "http://test.com", true);
        parser.parse();
    }
}