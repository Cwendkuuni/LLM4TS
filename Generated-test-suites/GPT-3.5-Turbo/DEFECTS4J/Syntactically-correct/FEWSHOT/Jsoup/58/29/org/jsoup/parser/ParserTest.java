package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.CharacterReader;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testConstructor() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertNotNull(parser);
        assertSame(treeBuilder, parser.getTreeBuilder());
        assertFalse(parser.isTrackErrors());
        assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Document doc = parser.parseInput("<html><body><p>Test</p></body></html>", "");
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("Test", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertSame(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
        assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testGetErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertTrue(parser.getErrors().isEmpty());
        parser.setTrackErrors(10);
        assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testSettings() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        ParseSettings settings = new ParseSettings(false, false);
        parser.settings(settings);
        assertSame(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body><p>Test</p></body></html>", "");
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("Test", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Test</p>", context, "");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Test", nodes.get(0).text());
    }

    @Test
    public void testParseFragmentWithErrorsTracking() {
        Element context = new Element("div");
        ParseErrorList errorList = ParseErrorList.tracking(5);
        List<Node> nodes = Parser.parseFragment("<p>Test</p><div><span>Test</div>", context, "", errorList);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertEquals("Test", nodes.get(0).text());
        assertEquals("Test", nodes.get(1).text());
        assertEquals(1, errorList.size());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<p>Test</p>");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Test", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Test</p>", "");
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("Test", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testUnescapeEntities() {
        String escaped = "&lt;p&gt;Test&lt;/p&gt;";
        String unescaped = Parser.unescapeEntities(escaped, false);
        assertEquals("<p>Test</p>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Test</p>", "");
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("Test", doc.getElementsByTag("p").first().text());
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