package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput() {
        // Test parsing valid HTML
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("Hello World", doc.body().selectFirst("h1").text());

        // Test parsing invalid HTML
        html = "<html><body><h1>Hello World</h2></body></html>";
        doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("Hello World", doc.body().selectFirst("h1").text());
        assertEquals(1, parser.getErrors().size());
        assertEquals("Unexpected closing tag (h2)", parser.getErrors().get(0).getMessage());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
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

        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(2);
        assertEquals(2, parser.maxErrors);

        parser.setTrackErrors(0);
        assertEquals(0, parser.maxErrors);
    }

    @Test
    public void testGetErrors() {
        assertEquals(0, parser.getErrors().size());

        parser.setTrackErrors(3);
        assertEquals(0, parser.getErrors().size());

        String html = "<html><body><h1>Hello World</h2></body></html>";
        String baseUri = "http://example.com";
        parser.parseInput(html, baseUri);
        assertEquals(1, parser.getErrors().size());
        assertEquals("Unexpected closing tag (h2)", parser.getErrors().get(0).getMessage());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("Hello World", doc.body().selectFirst("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello <b>World</b></div>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
        Element div = (Element) nodeList.get(0);
        assertNotNull(div);
        assertEquals("div", div.tagName());
        assertEquals("Hello ", div.text());
        assertEquals("World", div.selectFirst("b").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "Hello <b>World</b>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("Hello World", doc.body().html());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "Hello <b>World</b>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("Hello World", doc.body().html());
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