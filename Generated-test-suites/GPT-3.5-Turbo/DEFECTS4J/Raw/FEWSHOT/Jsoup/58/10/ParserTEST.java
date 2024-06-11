package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("<html><head><title>Test</title></head><body>Hello World</body></html>", "http://example.com/");
        assertNotNull(document);
        Element head = document.head();
        Element body = document.body();
        assertNotNull(head);
        assertNotNull(body);
        assertNotNull(head.selectFirst("title"));
        assertNotNull(body.selectFirst("body"));
        assertEquals("Test", head.selectFirst("title").text());
        assertEquals("Hello World", body.text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
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
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNull(parser.getErrors());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(true, true);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html><head><title>Test</title></head><body>Hello World</body></html>", "http://example.com/");
        assertNotNull(document);
        Element head = document.head();
        Element body = document.body();
        assertNotNull(head);
        assertNotNull(body);
        assertNotNull(head.selectFirst("title"));
        assertNotNull(body.selectFirst("body"));
        assertEquals("Test", head.selectFirst("title").text());
        assertEquals("Hello World", body.text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Test</p><span>Test 2</span>", context, "http://example.com/");
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals("span", nodes.get(1).nodeName());
        assertEquals("Test", ((Element) nodes.get(0)).text());
        assertEquals("Test 2", ((Element) nodes.get(1)).text());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<node>Test</node>", "http://example.com/");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("node", nodes.get(0).nodeName());
        assertEquals("Test", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<p>Test</p><span>Test 2</span>", "http://example.com/");
        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        assertEquals("p", body.child(0).nodeName());
        assertEquals("span", body.child(1).nodeName());
        assertEquals("Test", ((Element) body.child(0)).text());
        assertEquals("Test 2", ((Element) body.child(1)).text());
    }

    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;div&gt;Test&lt;/div&gt;", true);
        assertEquals("<div>Test</div>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<p>Test</p><span>Test 2</span>", "http://example.com/");
        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        assertEquals("p", body.child(0).nodeName());
        assertEquals("span", body.child(1).nodeName());
        assertEquals("Test", ((Element) body.child(0)).text());
        assertEquals("Test 2", ((Element) body.child(1)).text());
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