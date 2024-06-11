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
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><title>Test</title><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.select("h1").text());
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

        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);

        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testParse() {
        String html = "<html><title>Test</title><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h2>Fragment</h2>";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://example.com");

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("h2", ((Element) nodes.get(0)).tagName());
        assertEquals("Fragment", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Body Fragment</h2>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Body Fragment", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h2>Relaxed Body Fragment</h2>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Relaxed Body Fragment", doc.body().text());
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