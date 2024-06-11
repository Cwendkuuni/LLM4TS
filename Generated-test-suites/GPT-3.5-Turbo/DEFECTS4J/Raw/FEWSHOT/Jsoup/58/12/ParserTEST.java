package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().select("h1").text());
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
        Parser updatedParser = parser.setTreeBuilder(treeBuilder);

        assertNotNull(updatedParser);
        assertEquals(treeBuilder, updatedParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        int maxErrors = 5;
        Parser updatedParser = parser.setTrackErrors(maxErrors);

        assertNotNull(updatedParser);
        assertTrue(updatedParser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        Parser updatedParser = parser.settings(settings);

        assertNotNull(updatedParser);
        assertEquals(settings, updatedParser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello World!</p></div>";

        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element div = (Element) nodes.get(0);
        assertEquals(1, div.children().size());
        assertTrue(div.child(0) instanceof Element);
        Element p = (Element) div.child(0);
        assertEquals("Hello World!", p.text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><node>Hello World!</node></root>";
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element root = (Element) nodes.get(0);
        assertEquals(1, root.children().size());
        assertTrue(root.child(0) instanceof Element);
        Element node = (Element) root.child(0);
        assertEquals("Hello World!", node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.title());
        assertEquals("Hello World!", doc.body().select("p").text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;div&gt;Hello World!&lt;/div&gt;";
        boolean inAttribute = false;

        String unescapedString = Parser.unescapeEntities(escapedString, inAttribute);

        assertEquals("<div>Hello World!</div>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.title());
        assertEquals("Hello World!", doc.body().select("p").text());
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