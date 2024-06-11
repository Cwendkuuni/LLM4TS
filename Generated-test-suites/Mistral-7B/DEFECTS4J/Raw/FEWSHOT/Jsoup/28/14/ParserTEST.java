package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = ParseErrorList.tracking(5);
        Document doc = parser.parseInput(html, baseUri, errors);

        assertEquals(0, errors.size());
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><h1><script>alert('XSS');</script></h1></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = ParseErrorList.tracking(5);
        Document doc = parser.parseInput(html, baseUri, errors);

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Tag 'script' not allowed in content: 'h1'", error.getMessage());
        assertEquals("<html><head></head><body><h1></h1></body></html>", doc.html());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(XmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        boolean isTrackErrors = parser.isTrackErrors();
        assertFalse(isTrackErrors);

        parser.setTrackErrors(1);
        isTrackErrors = parser.isTrackErrors();
        assertTrue(isTrackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        int maxErrors = parser.getMaxErrors();
        assertEquals(1, maxErrors);

        parser.setTrackErrors(0);
        maxErrors = parser.getMaxErrors();
        assertEquals(0, maxErrors);
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());

        String html = "<html><body><h1><script>alert('XSS');</script></h1></body></html>";
        List<ParseError> newErrors = ParseErrorList.tracking(5);
        Document doc = parser.parseInput(html, null, newErrors);

        assertEquals(1, newErrors.size());
        ParseError error = newErrors.get(0);
        assertEquals("Tag 'script' not allowed in content: 'h1'", error.getMessage());
        errors = parser.getErrors();
        assertEquals(newErrors, errors);
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = new Element("div");
        Document doc = Parser.parse(fragmentHtml, context, "https://example.com");
        Element p = (Element) doc.body().child(0);
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");
        Element p = doc.body().child(0);
        assertEquals("Hello World", p.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullBaseUri() {
        String bodyHtml = "<p>Hello World</p>";
        Parser.parseBodyFragment(bodyHtml, null);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com");
        Element p = doc.body().child(0);
        assertEquals("Hello World", p.text());
    }

    @Test(expected = DeprecationException.class)
    public void testParseBodyFragmentRelaxedDeprecated() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com");
    }
}