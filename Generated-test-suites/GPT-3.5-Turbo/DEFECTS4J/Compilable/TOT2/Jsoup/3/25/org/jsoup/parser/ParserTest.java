package org.jsoup.parser;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragment_RelaxedParsing() {
        String bodyHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testEdgeCases() {
        // Edge case: parsing empty HTML
        String html = "";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        assertNotNull(document);

        // Edge case: parsing HTML with special characters
        html = "<div>&#169; Copyright</div>";
        document = Parser.parse(html, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testExceptions() {
        // Exception handling: Invalid HTML syntax
        String html = "<div><p>Paragraph</div>"; // Missing closing tag for <p>
        String baseUri = "http://example.com";
        try {
            Document document = Parser.parse(html, baseUri);
            fail("Invalid HTML should throw an exception");
        } catch (Exception e) {
            // expected exception
        }

        // Exception handling: Null inputs
        try {
            Document document = Parser.parse(null, baseUri);
            fail("Null HTML should throw an exception");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        try {
            Document document = Parser.parse("", null);
            fail("Null base URI should throw an exception");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }
}