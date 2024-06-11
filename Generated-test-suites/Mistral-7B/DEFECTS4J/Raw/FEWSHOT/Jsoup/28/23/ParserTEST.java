package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Jsoup.parse;
import static org.jsoup.nodes.Jsoup.parseBodyFragment;
import static org.jsoup.nodes.Jsoup.parseFragment;
import static org.jsoup.nodes.Jsoup.parseHtml;
import static org.jsoup.nodes.Jsoup.parseXml;
import static org.jsoup.nodes.Jsoup.validate;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = parse(html);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.select("h1").text());

        // Test with base URI
        String baseUri = "https://example.com/";
        Document docWithBase = parse(html, baseUri);
        assertEquals(baseUri, docWithBase.baseUri());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World</h1>";
        List<Node> nodes = parseFragment(fragmentHtml);
        Node node = nodes.get(0);
        assertEquals("h1", node.tagName());
        assertEquals("Hello World", node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parseBodyFragment(bodyHtml, null);
        assertEquals(1, doc.numberOfChildren());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseInvalidHTML() {
        String invalidHtml = "<html><body><script>alert('Hello World')</script></body></html>";
        try {
            parse(invalidHtml);
            fail("Expected a ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            parseBodyFragment(invalidHtml, null);
            fail("Expected a ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            parseFragment(invalidHtml, null, null);
            fail("Expected a ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParseXML() {
        String xml = "<root><element>Hello World</element></root>";
        Document doc = parseXml(xml);
        assertEquals("root", doc.tagName());
        Element element = (Element) doc.select("element").get(0);
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parseBodyFragmentRelaxed(bodyHtml, null);
        assertEquals(1, doc.numberOfChildren());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testTrackParseErrors() {
        int maxErrors = 3;
        Parser parser = new Parser(new HtmlTreeBuilder()) {
            @Override
            public ParseErrorList getErrors() {
                return new ParseErrorList() {
                    private int errorCount = 0;

                    @Override
                    public void add(ParseError error) {
                        errorCount++;
                    }

                    @Override
                    public List<ParseError> errors() {
                        List<ParseError> errors = new ArrayList<>();
                        while (errorCount > 0) {
                            errors.add(new ParseError("Error " + errorCount, ""));
                            errorCount--;
                        }
                        return errors;
                    }
                };
            }
        };
        String htmlWithErrors = "<html><body><script>alert('Hello World')</script></body></html>";

        Document doc = parser.parseInput(htmlWithErrors, null);
        List<ParseError> errors = parser.getErrors();
        assertEquals(maxErrors, errors.size());
    }
}