package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseHtml() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Parser parser = new Parser(html, "https://example.com");
        Document doc = parser.parse();
        Element htmlNode = doc.body();
        Element headNode = htmlNode.head();
        Element titleNode = headNode.title();
        Element h1Node = htmlNode.getElementsByTag("h1").first();

        assertEquals("Test Title", titleNode.text());
        assertEquals("Test Heading", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Test Heading</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");
        Element bodyNode = doc.body();
        Element h1Node = bodyNode.getElementsByTag("h1").first();

        assertEquals("Test Heading", h1Node.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser parser = new Parser(null, "https://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String invalidHtml = "<html><body><h1<script>alert('XSS');</script>";
        Parser parser = new Parser(invalidHtml, "https://example.com");
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><h1>Test Heading</h1></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com");
        Element bodyNode = doc.body();
        Element h1Node = bodyNode.getElementsByTag("h1").first();

        assertEquals("Test Heading", h1Node.text());
    }

    // Add more test cases here for other methods and edge cases
}