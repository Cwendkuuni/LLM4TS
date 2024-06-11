package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser.TreeBuilder;
import org.jsoup.parser.util.ParseError;
import org.jsoup.parser.util.ParseErrorList;
import org.jsoup.parser.util.ParseSettings;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertNull(parser.parseInput(null, null));
        assertNull(parser.parseInput("", ""));
        assertNotNull(parser.parseInput("<html><body>Hello World!</body></html>", "http://example.com"));
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Parser newParser = parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, newParser.getTreeBuilder());
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
        Parser newParser = parser.setTrackErrors(0);
        assertFalse(newParser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertNull(parser.getErrors());
    }
    
    @Test
    public void testSettings() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        ParseSettings settings = new ParseSettings(true, true);
        Parser newParser = parser.settings(settings);
        assertEquals(settings, newParser.settings());
    }
    
    @Test
    public void testParse() {
        assertNull(Parser.parse(null, null));
        assertNull(Parser.parse("", ""));
        assertNotNull(Parser.parse("<html><body>Hello World!</body></html>", "http://example.com"));
    }
    
    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        assertNull(Parser.parseFragment(null, null, null));
        assertNull(Parser.parseFragment("", context, ""));
        assertNotNull(Parser.parseFragment("<p>Hello World!</p>", context, "http://example.com"));
    }
    
    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodeList = Parser.parseFragment(null, null, null, null);
        assertTrue(nodeList.isEmpty());
        nodeList = Parser.parseFragment("", context, "", null);
        assertTrue(nodeList.isEmpty());
        nodeList = Parser.parseFragment("<p>Hello World!</p>", context, "http://example.com", null);
        assertFalse(nodeList.isEmpty());
    }
    
    @Test
    public void testParseXmlFragment() {
        assertNull(Parser.parseXmlFragment(null, null));
        assertNull(Parser.parseXmlFragment("", ""));
        assertNotNull(Parser.parseXmlFragment("<p>Hello World!</p>", "http://example.com"));
    }
    
    @Test
    public void testParseBodyFragment() {
        assertNull(Parser.parseBodyFragment(null, null));
        assertNull(Parser.parseBodyFragment("", ""));
        assertNotNull(Parser.parseBodyFragment("<p>Hello World!</p>", "http://example.com"));
    }
    
    @Test
    public void testUnescapeEntities() {
        assertNull(Parser.unescapeEntities(null, true));
        assertEquals("&lt;div&gt;", Parser.unescapeEntities("&lt;div&gt;", true));
        assertEquals("<div>", Parser.unescapeEntities("&lt;div&gt;", false));
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        assertNull(Parser.parseBodyFragmentRelaxed(null, null));
        assertNull(Parser.parseBodyFragmentRelaxed("", ""));
        assertNotNull(Parser.parseBodyFragmentRelaxed("<p>Hello World!</p>", "http://example.com"));
    }
    
    @Test
    public void testHtmlParser() {
        assertNotNull(Parser.htmlParser());
    }
    
    @Test
    public void testXmlParser() {
        assertNotNull(Parser.xmlParser());
    }
}