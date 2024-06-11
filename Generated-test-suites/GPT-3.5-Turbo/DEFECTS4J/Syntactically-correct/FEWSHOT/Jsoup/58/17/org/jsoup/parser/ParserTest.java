package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testGetTreeBuilder() {
        assertSame(parser.getTreeBuilder(), parser.treeBuilder);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        assertEquals(doc.title(), "");
        assertEquals(doc.baseUri(), baseUri);
        assertEquals(doc.select("h1").text(), "Test");
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertSame(treeBuilder, parser.getTreeBuilder());
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
        assertEquals(parser.maxErrors, 5);

        parser.setTrackErrors(0);
        assertEquals(parser.maxErrors, 0);
    }

    @Test
    public void testGetErrors() {
        assertNull(parser.getErrors());

        parser.setTrackErrors(2);
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(errors.size(), 0);
    }

    @Test
    public void testSettings() {
        assertNotNull(parser.settings());
        assertEquals(parser.settings(), parser.settings);

        ParseSettings settings = new ParseSettings(true, true);
        parser.settings(settings);
        assertSame(settings, parser.settings());
    }

    @Test
    public void testParseString() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertEquals(doc.title(), "");
        assertEquals(doc.baseUri(), baseUri);
        assertEquals(doc.select("h1").text(), "Test");
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Test</h1>";
        Element context = new Element(Tag.valueOf("body"), "");
        String baseUri = "http://example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertFalse(nodeList.isEmpty());
        assertEquals(nodeList.get(0).nodeName(), "h1");
        assertEquals(nodeList.get(0).text(), "Test");
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertFalse(nodeList.isEmpty());
        assertEquals(nodeList.get(0).nodeName(), "h1");
        assertEquals(nodeList.get(0).text(), "Test");
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals(doc.title(), "");
        assertEquals(doc.baseUri(), baseUri);
        assertEquals(doc.select("h1").text(), "Test");
    }

    @Test
    public void testUnescapeEntities() {
        String string = "This is an &lt;example&gt; string.";
        String unescaped = Parser.unescapeEntities(string, false);

        assertEquals(unescaped, "This is an <example> string.");
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertEquals(doc.title(), "");
        assertEquals(doc.baseUri(), baseUri);
        assertEquals(doc.select("h1").text(), "Test");
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertSame(htmlParser.getTreeBuilder(), new HtmlTreeBuilder());
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertSame(xmlParser.getTreeBuilder(), new XmlTreeBuilder());
    }
}