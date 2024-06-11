package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals("p", doc.body().getElementsByTag("p").first().tagName());
        assertEquals("Hello, World!", doc.body().getElementsByTag("p").first().text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(treeBuilder instanceof TreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = new TreeBuilder();
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
        Parser newParser = parser.setTrackErrors(10);
        assertTrue(newParser.isTrackErrors());
        assertEquals(10, newParser.maxErrors);
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertNull(parser.getErrors());
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals("p", doc.body().getElementsByTag("p").first().tagName());
        assertEquals("Hello, World!", doc.body().getElementsByTag("p").first().text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello, World!</p>";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodeList.size());
        assertEquals("p", nodeList.get(0).nodeName());
        assertEquals("Hello, World!", nodeList.get(0).childNodes().get(0).toString());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("body", doc.tagName());
        assertEquals("p", doc.getElementsByTag("p").first().tagName());
        assertEquals("Hello, World!", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals("p", doc.body().getElementsByTag("p").first().tagName());
        assertEquals("Hello, World!", doc.body().getElementsByTag("p").first().text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}