package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";

        Document document = parser.parseInput(html, baseUri);

        // Check if the parsed document is not null and has the correct root element
        assertNotNull(document);
        assertEquals("html", document.tagName());

        // Check if the parsed document has the correct title
        Element titleElement = document.title();
        assertNotNull(titleElement);
        assertEquals("Test Page", titleElement.text());

        // Check if the parsed document has the correct body text
        Element bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("Hello World", bodyElement.text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><head><title>Test Page</title></head><body><script>alert('Hello')</script></body></html>";
        String baseUri = "http://example.com";
        int maxErrors = 1;

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(maxErrors);

        Document document = parser.parseInput(html, baseUri);

        // Check if the parsed document is not null
        assertNotNull(document);

        // Check if there is an error in the parsed document
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing start tag '<script>'", error.getMessage());
    }

    @Test
    public void testParseInputWithNoTrackErrors() {
        String html = "<html><head><title>Test Page</title></head><body><script>alert('Hello')</script></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);

        Document document = parser.parseInput(html, baseUri);

        // Check if the parsed document is not null
        assertNotNull(document);

        // Check if there are no errors in the parsed document
        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseInputWithEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());

        Document document = parser.parseInput(html, baseUri);

        // Check if the parsed document is null
        assertNull(document);
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src='image.png' />";
        String baseUri = "http://example.com/testpage";

        Parser parser = new Parser(new HtmlTreeBuilder());

        Document document = parser.parseInput(html, baseUri);

        // Check if the image element has the correct source attribute
        Element imgElement = document.select("img").first();
        assertNotNull(imgElement);
        assertEquals("image.png", imgElement.attr("src"));
        assertEquals("http://example.com/testpage/image.png", imgElement.absUrl("src"));
    }
}