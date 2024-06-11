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
        String html = "<html><head></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com/";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", doc.html());
        assertEquals("<html><head></head><body>", doc.body().html(), "Body element should not contain the closing tag");
        assertEquals("Hello", doc.select("h1").text());

        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><head></head><body><h1>Hello</h1><script>alert('Hello World')</script></body></html>";
        String baseUri = "http://example.com/";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        Document doc = parser.parseInput(html, baseUri);

        assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", doc.html());
        assertEquals("<html><head></head><body>", doc.body().html(), "Body element should not contain the closing tag");
        assertEquals("Hello", doc.select("h1").text());

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.SCRIPT_IN_BODY, error.getErrorCode());
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><head></head><body><h1>Hello</h1><script>alert('Hello World')</script></body><script>alert('Hello World')</script></html>";
        String baseUri = "http://example.com/";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(2);

        Document doc = parser.parseInput(html, baseUri);

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullHtml() {
        String baseUri = "http://example.com/";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, baseUri);
    }

    @Test
    public void testParseInputEmptyString() {
        String baseUri = "http://example.com/";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", baseUri);
        assertEquals("", doc.html());
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
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithMaxErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        parser.setTrackErrors(10);
        assertEquals(10, parser.getMaxErrors());
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com/";
        Document doc = Parser.parse(html, baseUri);

        assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", doc.html());
        assertEquals("<html><head></head><body>", doc.body().html(), "Body element should not contain the closing tag");
        assertEquals("Hello", doc.select("h1").text());

        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com/";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals("p", node.tagName());
        assertEquals("Hello World", node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><p>Hello World</p></body>";
        String baseUri = "http://example.com/";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", doc.html());
        assertEquals("<p>Hello World</p>", doc.body().html());
        assertEquals("Hello World", doc.select("p").text());

        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseBodyFragmentWithErrors() {
        String bodyHtml = "<body><p>Hello World</p><script>alert('Hello World')</script></body>";
        String baseUri = "http://example.com/";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", doc.html());
        assertEquals("<p>Hello World</p>", doc.body().html());
        assertEquals("Hello World", doc.select("p").text());

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.SCRIPT_IN_BODY, error.getErrorCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullHtml() {
        String baseUri = "http://example.com/";
        Parser.parseBodyFragment(null, baseUri);
    }

    @Test
    public void testParseBodyFragmentEmptyString() {
        String baseUri = "http://example.com/";
        Document doc = Parser.parseBodyFragment("", baseUri);
        assertEquals("", doc.html());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><p>Hello World</p></body>";
        String baseUri = "http://example.com/";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", doc.html());
        assertEquals("<p>Hello World</p>", doc.body().html());
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(XmlTreeBuilder.class, treeBuilder.getClass());
    }
}