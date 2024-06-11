package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.*;

/**
 * Test cases for Parser class
 */
public class ParserTest {
    // Test constructor
    @Test
    public void testParserConstruction() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        String baseUri = "http://example.com/";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("Test Title", doc.title().text());
        assertEquals("<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>", doc.html().toString());
    }

    // Test parse method with empty HTML
    @Test
    public void testParseEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com/";

        try {
            Parser parser = new Parser(html, baseUri, false);
            parser.parse();
            fail("Expected ParserException");
        } catch (Parser.ParserException e) {
            // Expected exception
        }
    }

    // Test parse method with null HTML
    @Test(expected = NullPointerException.class)
    public void testParseNullHtml() {
        String baseUri = "http://example.com/";
        Parser parser = new Parser(null, baseUri, false);
        parser.parse();
    }

    // Test parse method with null baseUri
    @Test(expected = NullPointerException.class)
    public void testParseNullBaseUri() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        parser.parse();
    }

    // Test parseBodyFragment method with valid input
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Test Heading</h1></body>";
        String baseUri = "http://example.com/";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("<html><head></head><body><h1>Test Heading</h1></body></html>", doc.html().toString());
        assertEquals("Test Heading", doc.body().text());
    }

    // Test parseBodyFragment method with empty input
    @Test
    public void testParseBodyFragmentEmpty() {
        String bodyHtml = "";
        String baseUri = "http://example.com/";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("<html><head></head><body></body></html>", doc.html().toString());
        assertTrue(doc.body().children().isEmpty());
    }

    // Test parseBodyFragment method with null input
    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentNullInput() {
        String baseUri = "http://example.com/";
        Parser.parseBodyFragment(null, baseUri);
    }

    // Test parse method with valid HTML containing comments, CDATA, XML declaration, and attributes
    @Test
    public void testParseValidHtml() {
        String html = "<!-- Test Comment -->" +
                "<!-- Test Comment End -->" +
                "<![CDATA[Test CDATA]]>" +
                "<<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<html><head><title>Test Title</title>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"description\" content=\"Test Description\">" +
                "</head><body><h1 id=\"test-id\">Test Heading</h1></body></html>";
        String baseUri = "http://example.com/";

        Document doc = new Parser(html, baseUri, false).parse();

        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Description", doc.head().select("meta[name=\"description\"]").attr("content"));
        assertEquals("Test-id", doc.body().select("#test-id").text());
        assertEquals("<!-- Test Comment -->" +
                "<!-- Test Comment End -->" +
                "<![CDATA[Test CDATA]]>" +
                "<<?xml version=\"1.0\" encoding=\"UTF-8\"?>>" +
                "<html><head><title>Test Title</title>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"description\" content=\"Test Description\">" +
                "</head><body><h1 id=\"test-id\">Test Heading</h1></body></html>", doc.html().toString());
    }

    // Test parse method with invalid HTML
    @Test(expected = Parser.ParserException.class)
    public void testParseInvalidHtml() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1>"; // missing closing tag
        String baseUri = "http://example.com/";

        try {
            new Parser(html, baseUri, false).parse();
            fail("Expected ParserException");
        } catch (Parser.ParserException e) {
            // Expected exception
        }
    }

    // Test parse method with HTML containing invalid tag names
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTagNames() {
        String html = "<html><head><title>Test Title</title></head><body><invalid-tag attribute=\"value\"/></body></html>";
        String baseUri = "http://example.com/";

        try {
            new Parser(html, baseUri, false).parse();
            fail("Expected IllegalArgumentException");
        } catch (Parser.ParserException e) {
            // Ignore the ParserException, as it's expected that the Parser would throw an IllegalArgumentException for invalid tag names
        } catch (IllegalArgumentException ie) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Test parse method with HTML containing a script tag with an incorrect 'type' attribute
    @Test(expected = IllegalArgumentException.class)
    public void testParseScriptTagWithInvalidType() {
        String html = "<html><head><title>Test Title</title></head><body><script type=\"text/xml\" src=\"script.xml\"></script></body></html>";
        String baseUri = "http://example.com/";

        try {
            new Parser(html, baseUri, false).parse();
            fail("Expected IllegalArgumentException");
        } catch (Parser.ParserException e) {
            // Ignore the ParserException, as it's expected that the Parser would throw an IllegalArgumentException for an incorrect 'type' attribute in a script tag
        } catch (IllegalArgumentException ie) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}