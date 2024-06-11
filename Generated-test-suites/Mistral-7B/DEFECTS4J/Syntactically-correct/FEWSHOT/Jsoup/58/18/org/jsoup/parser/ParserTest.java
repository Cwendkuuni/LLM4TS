package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><div id='test'>Hello World</div></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = new ArrayList<>();

        Document doc = parser.parseInput(html, baseUri);
        assertEquals("Hello World", doc.getElementById("test").text());
        assertEquals(errors.size(), 0);

        // Test with errors
        parser.setTrackErrors(1);
        html = "<html><body><div id='test'>Hello World</div><script>alert('XSS')</script></body></html>";
        errors.clear();
        doc = parser.parseInput(html, baseUri);
        assertEquals(errors.size(), 1);
        ParseError error = errors.get(0);
        assertEquals("Unexpected char 60 found", error.getMessage());
        assertEquals("script", error.getLine().toString());
        assertEquals(1, error.getLineNumber());
        assertEquals(18, error.getColumnNumber());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertEquals(1, parser.getMaxErrors());

        parser.setTrackErrors(0);
        assertEquals(0, parser.getMaxErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());

        String html = "<html><body><div id='test'>Hello World</div><script>alert('XSS')</script></body></html>";
        parser.setTrackErrors(1);
        errors.clear();
        parser.parseInput(html, "http://example.com");
        assertEquals(errors.size(), 1);
        ParseError error = errors.get(0);
        assertEquals("Unexpected char 60 found", error.getMessage());
        assertEquals("script", error.getLine().toString());
        assertEquals(1, error.getLineNumber());
        assertEquals(18, error.getColumnNumber());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings();
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body><div id='test'>Hello World</div></body></html>", "http://example.com");
        assertEquals("Hello World", doc.getElementById("test").text());
    }

    @Test
    public void testParseFragment() {
        String html = "<html><body><div id='test'>Hello World</div></body></html>";
        Element context = new Element("html");
        Document doc = Document.createShell("http://example.com");
        List<Node> nodes = Parser.parseFragment(html, context, "http://example.com");
        Element test = (Element) nodes.get(0);
        doc.body().appendChild(test);
        assertEquals("Hello World", test.text());
        assertEquals(1, doc.body().childNodeSize());
    }

    @Test
    public void testParseXmlFragment() {
        String xml = "<xml><root><child>Hello World</child></root></xml>";
        List<Node> nodes = Parser.parseXmlFragment(xml, "http://example.com");
        assertEquals(1, nodes.size());
        Element child = (Element) nodes.get(0);
        assertEquals("Hello World", child.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div id='test'>Hello World</div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals("Hello World", doc.getElementById("test").text());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&amp;lt;p&gt;Hello &amp;#x26; World!&lt;/p&gt;";
        String unescaped = Parser.unescapeEntities(html, false);
        assertEquals("<p>Hello World!</p>", unescaped);
    }
}