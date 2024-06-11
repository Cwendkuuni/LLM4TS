package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parse.ParseError;
import org.jsoup.parse.ParseErrorList;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><h1><script>alert('Hello World')</script></h1></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(2);
        Document doc = parser.parseInput(html, baseUri);

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());

        ParseError firstError = errors.get(0);
        assertEquals("Tag 'script' not allowed in content.", firstError.getMessage());
        assertEquals(0, firstError.getLine());
        assertEquals(7, firstError.getColumn());

        ParseError secondError = errors.get(1);
        assertEquals("Expected '<', but was 's'.", secondError.getMessage());
        assertEquals(0, secondError.getLine());
        assertEquals(13, secondError.getColumn());

        assertNull(doc.body().child(1));
    }

    @Test
    public void testParseInputWithoutErrors() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        Document doc = parser.parseInput(html, baseUri);

        List<ParseError> errors = parser.getErrors();
        assertNull(errors);

        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(new HtmlTreeBuilder(), treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);

        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(newTreeBuilder, treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(2);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithZero() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);

        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";

        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        String baseUri = "https://example.com";
        Parser.parse(null, baseUri);
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello World</div>";
        Element context = new Element("html");
        String baseUri = "https://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Node node = nodes.get(0);

        assertEquals(Element.class, node.getClass());
        assertEquals("div", node.tagName());
        assertEquals("Hello World", node.text());

        Element div = (Element) node;
        assertEquals("div", div.tagName());
        assertEquals("", div.classes());
        assertEquals(1, div.childNodeSize());
        assertEquals("Hello World", div.child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello World</div>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Element body = doc.body();
        List<Node> nodes = body.childNodes();

        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals("div", node.tagName());
        assertEquals("Hello World", node.text());
    }

    @Deprecated
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Hello World</div>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("<html><head></head><body><div>Hello World</div></body></html>", doc.html());
    }

    @Test
    public void testHtmlParser() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";

        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testXmlParser() {
        String xml = "<root><element>Test</element></root>";
        String baseUri = "https://example.com";

        Parser parser = Parser.xmlParser();
        Document doc = parser.parseInput(xml, baseUri);

        assertNotNull(doc);
        assertEquals("<root><element>Test</element></root>", doc.html());
    }
}