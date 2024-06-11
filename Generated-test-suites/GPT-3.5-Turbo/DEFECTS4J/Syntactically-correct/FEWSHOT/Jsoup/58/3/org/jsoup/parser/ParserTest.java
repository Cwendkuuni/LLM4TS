package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput() {
        // Test case with valid input and base URI
        Document document = parser.parseInput("<html><body><h1>Hello World!</h1></body></html>", "https://www.example.com");
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("Hello World!", document.select("h1").first().text());

        // Test case with null input and valid base URI
        document = parser.parseInput(null, "https://www.example.com");
        assertEquals("https://www.example.com", document.baseUri());
        assertTrue(document.body().children().isEmpty());

        // Test case with empty input and valid base URI
        document = parser.parseInput("", "https://www.example.com");
        assertEquals("https://www.example.com", document.baseUri());
        assertTrue(document.body().children().isEmpty());

        // Test case with valid input and null base URI
        document = parser.parseInput("<html><body><h1>Hello World!</h1></body></html>", null);
        assertEquals("", document.baseUri());
        assertEquals("Hello World!", document.select("h1").first().text());

        // Test case with null input and null base URI
        document = parser.parseInput(null, null);
        assertEquals("", document.baseUri());
        assertTrue(document.body().children().isEmpty());

        // Test case with empty input and null base URI
        document = parser.parseInput("", null);
        assertEquals("", document.baseUri());
        assertTrue(document.body().children().isEmpty());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(treeBuilder instanceof TreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);
        assertEquals(10, parser.getErrors().size());

        parser.setTrackErrors(20);
        assertEquals(20, parser.getErrors().size());

        parser.setTrackErrors(0);
        assertTrue(parser.getErrors().isEmpty());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());

        parser.setTrackErrors(10);
        assertEquals(10, parser.getErrors().size());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false, false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html><body><h1>Hello World!</h1></body></html>", "https://www.example.com");
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Test</p>", context, "https://www.example.com");
        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals("Test", nodes.get(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<tag>Hello World!</tag>", "https://www.example.com");
        assertEquals(1, nodes.size());

        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
        assertEquals("Hello World!", element.text());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<p>Test</p>", "https://www.example.com");
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("Test", document.body().select("p").first().text());
    }

    @Test
    public void testUnescapeEntities() {
        String unescapedString = Parser.unescapeEntities("&lt;p&gt;Test&lt;/p&gt;", false);
        assertEquals("<p>Test</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<p>Test</p>", "https://www.example.com");
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("Test", document.body().select("p").first().text());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertTrue(htmlParser.getTreeBuilder() instanceof TreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertTrue(xmlParser.getTreeBuilder() instanceof TreeBuilder);
    }
}