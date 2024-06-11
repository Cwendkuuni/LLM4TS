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
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Hello, World!", doc.select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser updatedParser = parser.setTreeBuilder(treeBuilder);

        assertEquals(treeBuilder, updatedParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser updatedParser = parser.setTrackErrors(5);

        assertTrue(updatedParser.isTrackErrors());
        assertEquals(5, updatedParser.getErrors().size());
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false);
        Parser updatedParser = parser.settings(settings);

        assertEquals(settings, updatedParser.settings());
    }

    @Test
    public void testParse() {
        String html = "<div><p>Hello, World!</p></div>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Hello, World!", doc.select("p").text());
    }

    @Test
    public void testParseFragment_Element() {
        String fragmentHtml = "<p>Hello, World!</p>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("https://www.example.com", ((Element) nodes.get(0)).baseUri());
        assertEquals("Hello, World!", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<user>John Doe</user>";
        String baseUri = "https://www.example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("https://www.example.com", ((Element) nodes.get(0)).baseUri());
        assertEquals("John Doe", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Lorem ipsum</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Lorem ipsum", doc.select("p").text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "Hello &quot;World&quot;";
        String unescapedString = Parser.unescapeEntities(escapedString, true);

        assertEquals("Hello \"World\"", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Hello, World!", doc.select("p").text());
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