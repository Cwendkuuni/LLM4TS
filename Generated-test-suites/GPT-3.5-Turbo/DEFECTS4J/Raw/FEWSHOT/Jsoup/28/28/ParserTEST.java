package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new TreeBuilderStub());
    }

    @Test
    public void testParseInput() {
        Document doc = parser.parseInput("<html></html>", "http://example.com");
        assertEquals("<html></html>", doc.html());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(treeBuilder instanceof TreeBuilderStub);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilderStub();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html></html>", "http://example.com");
        assertEquals("<html></html>", doc.html());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodeList = Parser.parseFragment("<div></div>", new Element("div"), "http://example.com");
        assertEquals("<div></div>", nodeList.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Test</p>", "http://example.com");
        assertEquals("<body><p>Test</p></body>", doc.html());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Test</p>", "http://example.com");
        assertEquals("<html><head></head><body><p>Test</p></body></html>", doc.html());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}

class TreeBuilderStub extends TreeBuilder {
    @Override
    protected void initialiseParse(String html, String baseUri, ParseErrorList errors) {
    }

    @Override
    protected Document parse(String input, String baseUri, ParseErrorList errors) {
        return new Document("");
    }

    @Override
    protected List<Node> parseFragment(String inputFragment, Element context, String baseUri, ParseErrorList errors) {
        return List.of(new Element("div"));
    }
}