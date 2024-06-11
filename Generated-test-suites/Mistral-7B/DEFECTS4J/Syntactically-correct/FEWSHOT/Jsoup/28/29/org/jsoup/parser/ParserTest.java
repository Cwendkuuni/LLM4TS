package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.HtmlElement;
import org.jsoup.nodes.XmlElement;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParserInstance() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser);
    }

    @Test
    public void testParserSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testParserIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", null);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        String malformedHtml = "<html<p>";

        List<ParseError> errors = parser.parseErrors(malformedHtml, null);
        assertEquals(1, errors.size());
        assertEquals("Error parsing start tag : html", errors.get(0).getMessage());

        Document doc = parser.parseInput(malformedHtml, null);
        assertNull(doc);
    }

    @Test
    public void testParseFragment() {
        String fragment = "<p>This is a test</p>";
        List<Node> nodes = Parser.parseFragment(fragment, null, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        assertEquals("p", node.tagName());
        assertEquals("This is a test", node.text());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>This is a test</p>", null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Node node = doc.childNodes().get(0);
        assertTrue(node instanceof Element);
        assertEquals("p", node.tagName());
        assertEquals("This is a test", node.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>This is a test</p>", null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Node node = doc.childNodes().get(0);
        assertTrue(node instanceof Element);
        assertEquals("p", node.tagName());
        assertEquals("This is a test", node.text());
    }

    @Test
    public void testHtmlParser() {
        Document doc = Parser.htmlParser().parse("<html><body><h1>Hello World</h1></body></html>", null);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testXmlParser() {
        Document doc = Parser.xmlParser().parse("<xml><tag>Hello World</tag></xml>", null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Node node = doc.childNodes().get(0);
        assertTrue(node instanceof Element);
        assertEquals("tag", node.tagName());
        assertEquals("Hello World", node.text());
    }

    @Test
    public void testTreeBuilderParse() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Document doc = treeBuilder.parse("<html><body><h1>Hello World</h1></body></html>", null);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());

        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        doc = newTreeBuilder.parse("<html><body><h1>Hello World</h1></body></html>", null);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }
}