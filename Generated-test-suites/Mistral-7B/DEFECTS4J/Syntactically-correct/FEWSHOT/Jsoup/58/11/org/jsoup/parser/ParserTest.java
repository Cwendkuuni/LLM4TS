package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());

        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        Element title = doc.title();
        assertNotNull(title);
        assertEquals("Test", title.text());
        Element body = doc.body();
        assertNotNull(body);
        List<Node> nodes = body.children();
        assertTrue(nodes.size() > 0);
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element p = (Element) node;
        assertEquals("p", p.tagName());
        assertEquals("Hello World!", p.text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals("Error parsing start tag on line 1: html:1:1 Unparsed start tag html\n", error.getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, null);
    }

    @Test
    public void testParseInputWithEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", null);
        assertNotNull(doc);
        assertTrue(doc.body().children().isEmpty());
    }

    @Test
    public void testParseInputWithErrorTrackingDisabled() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);

        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><a href='/foo/bar'></a></body></html>";
        String baseUri = "https://example.com/";
        Parser parser = new Parser(new HtmlTreeBuilder());

        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        Element a = doc.select("a").first();
        assertNotNull(a);
        assertEquals("/foo/bar", a.absUrl("."));
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals("Error parsing start tag on line 1: html:1:1 Unparsed start tag html\n", error.getMessage());
    }

    @Test
    public void testGetErrorsWithEmptyList() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParseStatic() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Document doc = Parser.parse(html, null);
        assertNotNull(doc);
        Element title = doc.title();
        assertNotNull(title);
        assertEquals("Test", title.text());
        Element body = doc.body();
        assertNotNull(body);
        List<Node> nodes = body.children();
        assertTrue(nodes.size() > 0);
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element p = (Element) node;
        assertEquals("p", p.tagName());
        assertEquals("Hello World!", p.text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World!</p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        assertTrue(nodes.size() > 0);
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element p = (Element) node;
        assertEquals("p", p.tagName());
        assertEquals("Hello World!", p.text());
    }

    @Test
    public void testParseFragmentWithErrors() {
        String fragmentHtml = "<p>Hello <b>World!</b>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals("Error parsing start tag on line 1: p:1:1 Unmatched tag p starting with b\n", error.getMessage());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><element>Hello World!</element></xml>";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, null);
        assertTrue(nodes.size() > 0);
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("element", element.tagName());
        assertEquals("Hello World!", element.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "https://example.com/";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        List<Node> nodes = body.children();
        assertTrue(nodes.size() > 0);
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element p = (Element) node;
        assertEquals("p", p.tagName());
        assertEquals("Hello World!", p.text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;p&gt;Hello &amp; World!&lt;/p&gt;";
        String unescaped = Parser.unescapeEntities(string, false);
        assertEquals("<p>Hello & World!</p>", unescaped);
    }

    @Test
    public void testUnescapeEntitiesInAttribute() {
        String string = "<a href='&lt;http://example.com&gt;'>Link</a>";
        String unescaped = Parser.unescapeEntities(string, true);
        assertEquals("<a href='<http://example.com>'>Link</a>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxedDeprecated() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "https://example.com/";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        List<Node> nodes = body.children();
        assertTrue(nodes.size() > 0);
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element p = (Element) node;
        assertEquals("p", p.tagName());
        assertEquals("Hello World!", p.text());
    }
}