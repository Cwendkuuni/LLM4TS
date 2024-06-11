package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());

        String html = "<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("Title", doc.title());
        assertEquals("Heading", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());

        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = new HtmlTreeBuilder();

        Parser newParser = parser.setTreeBuilder(treeBuilder);

        assertEquals(treeBuilder, newParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());

        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());

        Parser newParser = parser.setTrackErrors(5);

        assertTrue(newParser.isTrackErrors());
        assertEquals(5, newParser.getTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());

        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Title", doc.title());
        assertEquals("Heading", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
        assertEquals("Paragraph 1", ((Element) nodes.get(0)).text());
        assertEquals("Paragraph 2", ((Element) nodes.get(1)).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Paragraph 1", doc.body().getElementsByTag("p").get(0).text());
        assertEquals("Paragraph 2", doc.body().getElementsByTag("p").get(1).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Paragraph 1", doc.body().getElementsByTag("p").get(0).text());
        assertEquals("Paragraph 2", doc.body().getElementsByTag("p").get(1).text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();

        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();

        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}