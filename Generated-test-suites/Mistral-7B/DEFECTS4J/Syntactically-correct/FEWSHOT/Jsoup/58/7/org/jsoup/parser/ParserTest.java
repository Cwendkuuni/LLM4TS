package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, null);

        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.getElementsByTag("h1").text());
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src='test.html' />";
        String baseUri = "http://example.com/";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        Element img = doc.getElementsByTag("img").first();
        assertEquals("test.html", img.attr("src"));
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<script>alert('Hello World');</script>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        List<ParseError> errors = parser.parseInput(html, null).errors();

        assertEquals(5, errors.size());
    }

    @Test(expected = ParseError.class)
    public void testParseInputWithMalformedHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput("<html>", null);
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(null);
        assertEquals(null, parser.getTreeBuilder());

        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(null);
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(null);
        assertFalse(parser.isTrackErrors());

        parser = new Parser(null);
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(null);
        parser.setTrackErrors(5);
        assertEquals(5, parser.maxErrors);

        parser = new Parser(null);
        parser.setTrackErrors(0);
        assertEquals(0, parser.maxErrors);
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(null);
        parser.setTrackErrors(5);
        List<ParseError> errors = parser.parseInput("<script>alert('Hello World');</script>", null).errors();

        assertEquals(5, errors.size());
        assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
    }

    @Test
    public void testParseStatic() {
        Document doc = Parser.parse(" <html><head><title>Test</title></head><body><h1>Hello World</h1></body></html> ", null);

        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.getElementsByTag("h1").text());
    }

    @Test
    public void testParseFragment() {
        String html = "<div><p>Hello World</p></div>";
        Element context = Jsoup.parse("<html/>").body();
        List<Node> nodes = Parser.parseFragment(html, context, null);

        assertEquals(2, nodes.size());
        assertEquals("div", nodes.get(0).tagName());
        assertEquals("p", nodes.get(1).tagName());
    }

    @Test
    public void testParseXmlFragment() {
        String xml = "<person><name>John</name><age>30</age></person>";
        List<Node> nodes = Parser.parseXmlFragment(xml, null);

        assertEquals(2, nodes.size());
        assertEquals("person", nodes.get(0).tagName());
        assertEquals("name", nodes.get(1).tagName());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<body><p>Hello World</p></body>";
        Document doc = Parser.parseBodyFragment(html, null);

        assertEquals("", doc.head().toString());
        assertEquals("Hello World", doc.body().getElementsByTag("p").text());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String unescaped = Parser.unescapeEntities(html, false);

        assertEquals("<p>Hello & World</p>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<body><p>Hello World</p></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, null);

        assertEquals("", doc.head().toString());
        assertEquals("Hello World", doc.body().getElementsByTag("p").text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", null);

        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.getElementsByTag("h1").text());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        List<Node> nodes = parser.parseFragment("<person><name>John</name><age>30</age></person>", null);

        assertEquals(2, nodes.size());
        assertEquals("person", nodes.get(0).tagName());
        assertEquals("name", nodes.get(1).tagName());
    }
}