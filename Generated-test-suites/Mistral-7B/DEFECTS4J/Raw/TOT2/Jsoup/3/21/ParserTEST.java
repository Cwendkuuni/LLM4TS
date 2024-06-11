package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Test Heading</h1></body>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("Test Heading", doc.body().select("h1").text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        String html = null;
        String baseUri = "http://example.com";
        try {
            Parser parser = new Parser(html, baseUri, false);
            parser.parse();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        try {
            Parser parser = new Parser(html, baseUri, false);
            parser.parse();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String html = "<html<title>Test Title</title>";
        String baseUri = "http://example.com";
        try {
            Parser parser = new Parser(html, baseUri, false);
            parser.parse();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment --><html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("This is a comment ", doc.head().select("meta").first().attr("content")); // assuming "meta" tag exists
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<xml version='1.0' encoding='UTF-8' standalone='yes'><html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></xml>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("UTF-8", doc.head().select("meta[name='Content-Type']").attr("content"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidXmlDecl() {
        String html = "<xml version='1.0 encoding='UTF-8' standalone='yes'<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></xml>";
        String baseUri = "http://example.com";
        try {
            Parser parser = new Parser(html, baseUri, false);
            parser.parse();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals(1, doc.body().childNodes().size());

        // test that "h1" tag is removed when closing tag is found
        String expectedHtml = "<html><head><title>Test Title</title></head></html>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        assertEquals(1, doc.head().childNodes().size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseMissingEndTag() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        try {
            parser.parse();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    // ... Continue adding test cases for other methods and edge cases
}