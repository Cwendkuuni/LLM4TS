package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());

        // Test case 1
        Document doc = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
        assertTrue(doc instanceof Document);
        assertEquals("Hello World", doc.select("h1").text());

        // Test case 2
        doc = parser.parseInput("<html><body><p>Paragraph 1</p><p>Paragraph 2</p></body></html>", "http://example.com");
        assertTrue(doc instanceof Document);
        assertEquals("Paragraph 1", doc.select("p").first().text());
        assertEquals("Paragraph 2", doc.select("p").last().text());

        // Test case 3
        doc = parser.parseInput("", "http://example.com");
        assertTrue(doc instanceof Document);
        assertTrue(doc.children().isEmpty());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());

        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(treeBuilder instanceof TreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());

        TreeBuilder treeBuilder = new TreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());

        assertTrue(!parser.isTrackErrors());

        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());

        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());

        assertNull(parser.getErrors());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());

        ParseSettings settings = new ParseSettings(false, false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
        assertTrue(doc instanceof Document);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        context.html("<p>Paragraph 1</p><p>Paragraph 2</p>");

        List<Node> nodeList = Parser.parseFragment("<p>Paragraph 3</p><p>Paragraph 4</p>", context, "http://example.com");
        assertEquals("Paragraph 3", nodeList.get(0).toString());
        assertEquals("Paragraph 4", nodeList.get(1).toString());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodeList = Parser.parseXmlFragment("<p>Paragraph 1</p><p>Paragraph 2</p>", "http://example.com");
        assertEquals("Paragraph 1", nodeList.get(0).toString());
        assertEquals("Paragraph 2", nodeList.get(1).toString());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Paragraph 1</p><p>Paragraph 2</p>", "http://example.com");
        assertEquals("Paragraph 1", doc.select("p").first().text());
        assertEquals("Paragraph 2", doc.select("p").last().text());
    }

    @Test
    public void testUnescapeEntities() {
        String unescapedString = Parser.unescapeEntities("&lt;p&gt;Paragraph&lt;/p&gt;", true);
        assertEquals("<p>Paragraph</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Paragraph 1</p><p>Paragraph 2</p>", "http://example.com");
        assertEquals("Paragraph 1", doc.select("p").first().text());
        assertEquals("Paragraph 2", doc.select("p").last().text());
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