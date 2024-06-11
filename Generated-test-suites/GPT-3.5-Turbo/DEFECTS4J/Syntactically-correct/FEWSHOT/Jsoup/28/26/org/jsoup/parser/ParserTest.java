package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private static final String HTML = "<html><body><p>Hello, World!</p></body></html>";
    private static final String BASE_URI = "http://www.example.com";

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);

        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testParseInput() {
        Document doc = parser.parseInput(HTML, BASE_URI);

        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals("p", doc.body().child(0).tagName());
        assertEquals("Hello, World!", doc.body().child(0).text());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse(HTML, BASE_URI);

        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals("p", doc.body().child(0).tagName());
        assertEquals("Hello, World!", doc.body().child(0).text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Hello, World!</p>", context, BASE_URI);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("p", ((Element)nodes.get(0)).tagName());
        assertEquals("Hello, World!", ((Element)nodes.get(0)).text());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Hello, World!</p>", BASE_URI);

        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals("p", doc.body().child(0).tagName());
        assertEquals("Hello, World!", doc.body().child(0).text());
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