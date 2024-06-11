package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
		parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        Document doc = parser.parseInput("<html><body>Hello World!</body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("<html><head></head><body>Hello World!</body></html>", doc.toString());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body>Hello World!</body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("<html><head></head><body>Hello World!</body></html>", doc.toString());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodeList = Parser.parseFragment("<div>Hello World!</div>", null, "http://example.com");
        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
        Node node = nodeList.get(0);
        assertTrue(node instanceof Element);
        assertEquals("div", ((Element) node).tagName());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<span>Hello World!</span>", "http://example.com");
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals(1, body.children().size());
        Element span = body.child(0);
        assertNotNull(span);
        assertEquals("span", span.tagName());
        assertEquals("Hello World!", span.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<span>Hello World!</span>", "http://example.com");
        assertNotNull(doc);
        assertEquals("<span>Hello World!</span>", doc.toString());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertNotNull(htmlParser);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertNotNull(xmlParser);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}