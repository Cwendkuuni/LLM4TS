package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("<html lang=\"en\">Hello World!</html>", "http://example.com");
        assertEquals("<html lang=\"en\"><head></head><body>Hello World!</body></html>", document.outerHtml());
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
        Parser newParser = parser.setTreeBuilder(new XmlTreeBuilder());
        TreeBuilder treeBuilder = newParser.getTreeBuilder();
        assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(0, parser.setTrackErrors(10).maxErrors);
        assertEquals(10, parser.setTrackErrors(10).maxErrors);
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());
        parser.setTrackErrors(10);
        assertEquals(10, parser.getErrors().size());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(true);
        assertEquals(settings, parser.settings(settings).settings());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html lang=\"en\">Hello World!</html>", "http://example.com");
        assertEquals("<html lang=\"en\"><head></head><body>Hello World!</body></html>", document.outerHtml());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Hello</p><p>World</p>", context, "http://example.com");
        assertEquals("<p>Hello</p><p>World</p>", context.html());
        assertEquals(2, nodes.size());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<p>Hello</p><p>World</p>", "http://example.com");
        assertEquals("<p>Hello</p><p>World</p>", nodes.toString());
        assertEquals(2, nodes.size());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<p>Hello</p><p>World</p>", "http://example.com");
        assertEquals("<html><head></head><body><p>Hello</p><p>World</p></body></html>", document.outerHtml());
    }

    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;p&gt;Hello&lt;/p&gt;", false);
        assertEquals("<p>Hello</p>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<p>Hello</p><p>World</p>", "http://example.com");
        assertEquals("<html lang=\"en\"><head></head><body><p>Hello</p><p>World</p></body></html>", document.outerHtml());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}