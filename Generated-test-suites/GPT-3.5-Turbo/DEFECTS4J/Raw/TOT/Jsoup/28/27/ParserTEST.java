package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";

        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("Hello World", doc.getElementsByTag("p").first().text());
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

        TreeBuilder newTreeBuilder = parser.getTreeBuilder();
        assertNotNull(newTreeBuilder);
        assertTrue(newTreeBuilder instanceof XmlTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();

        assertTrue(!trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);

        boolean trackErrors = parser.isTrackErrors();
        assertTrue(trackErrors);
    }

    @Test
    public void testGetErrors() {
        parser.setTrackErrors(3);
        parser.parseInput("<h1>Heading</h1>", "http://example.com");

        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Unnecessary closing tag h1", errors.get(0).getErrorMessage());
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("Hello World", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Fragment</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
        assertEquals("p", nodeList.get(0).nodeName());
        assertEquals("Fragment", nodeList.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Body Fragment</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("Body Fragment", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Body Fragment</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("Body Fragment", doc.getElementsByTag("p").first().text());
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