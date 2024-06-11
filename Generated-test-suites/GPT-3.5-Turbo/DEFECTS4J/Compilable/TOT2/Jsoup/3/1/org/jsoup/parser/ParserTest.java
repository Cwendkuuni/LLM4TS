package org.jsoup.parser;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    private static final String BASE_URI = "https://www.example.com";

    private String html;
    private String bodyHtml;

    @Before
    public void setUp() {
        html = "<html><head><title>Test</title></head><body><div><p>Hello, World!</p></div></body></html>";
        bodyHtml = "<div><p>Hello, World!</p></div>";
    }

    @Test
    public void testParse() {
        Document document = Parser.parse(html, BASE_URI);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment(bodyHtml, BASE_URI);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        // Test relaxed parsing by providing a fragment with missing tags
        String relaxedHtml = "<p>Paragraph without enclosing tags";
        Document document = Parser.parseBodyFragment(relaxedHtml, BASE_URI);
        assertNotNull(document);
    }

    @Test
    public void testParseWithInvalidHtml() {
        // Test parsing with invalid HTML
        String invalidHtml = "<<<<";
        Document document = Parser.parse(invalidHtml, BASE_URI);
        assertNotNull(document);
    }

    @Test
    public void testParseEmptyHtml() {
        // Test parsing empty HTML
        String emptyHtml = "";
        Document document = Parser.parse(emptyHtml, BASE_URI);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragmentWithEmptyBodyHtml() {
        // Test parsing a fragment with an empty body
        String emptyBodyHtml = "";
        Document document = Parser.parseBodyFragment(emptyBodyHtml, BASE_URI);
        assertNotNull(document);
    }

    // Add more test cases for edge cases and error scenarios

}