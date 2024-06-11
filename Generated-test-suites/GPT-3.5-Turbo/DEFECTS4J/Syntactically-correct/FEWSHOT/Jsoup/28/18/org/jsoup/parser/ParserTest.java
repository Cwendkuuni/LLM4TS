package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        Element body = doc.body();
        assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("Hello, World!", h1.text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);

        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
        assertEquals(10, parser.getErrors().size());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);

        assertNotNull(parser.getErrors());
        assertEquals(5, parser.getErrors().size());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        Element body = doc.body();
        assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("Hello, World!", h1.text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello, World!</h1>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element h1 = (Element) nodes.get(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello, World!", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        Element body = doc.body();
        assertNull(doc.head());
        assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("Hello, World!", h1.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        Element body = doc.body();
        assertNull(doc.head());
        assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("Hello, World!", h1.text());
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