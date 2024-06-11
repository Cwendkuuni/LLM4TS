package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("<html><head></head><body></body></html>", "https://www.example.com");
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", document.outerHtml());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
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
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html><head></head><body></body></html>", "https://www.example.com");
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", document.outerHtml());
    }

    @Test
    public void testParseFragment() {
        Element context = Jsoup.parse("<div></div>").select("div").first();
        List<Node> nodes = Parser.parseFragment("<span></span><a></a>", context, "https://www.example.com");
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("span", ((Element) nodes.get(0)).tagName());
        assertTrue(nodes.get(1) instanceof Element);
        assertEquals("a", ((Element) nodes.get(1)).tagName());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<span></span><a></a>", "https://www.example.com");
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("span", ((Element) nodes.get(0)).tagName());
        assertTrue(nodes.get(1) instanceof Element);
        assertEquals("a", ((Element) nodes.get(1)).tagName());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<span></span><a></a>", "https://www.example.com");
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <span></span><a></a>\n </body>\n</html>", document.outerHtml());
    }

    @Test
    public void testUnescapeEntities() {
        String unescapedString = Parser.unescapeEntities("&lt;div&gt;&nbsp;&lt;/div&gt;", false);
        assertEquals("<div> </div>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<span></span><a></a>", "https://www.example.com");
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <span></span><a></a>\n </body>\n</html>", document.outerHtml());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}