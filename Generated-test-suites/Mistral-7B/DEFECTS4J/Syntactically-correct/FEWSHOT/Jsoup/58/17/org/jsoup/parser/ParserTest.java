package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, null);
        assertEquals(html, document.html());
        assertEquals(1, document.numHeadElements());
        assertEquals(1, document.numBodyElements());
        assertEquals("Hello World", document.select("h1").text());

        html = "<html><head><title>Test</title></head><body></body></html>";
        document = parser.parseInput(html, null);
        assertEquals(html, document.html());
        assertEquals(1, document.numHeadElements());
        assertNotNull(document.title());
        assertEquals("Test", document.title().text());
        assertEquals(0, document.numBodyElements());

        // Test error tracking
        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        document = parser.parseInput("<html><body><script>", null);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Tag script not allowed in document", errors.get(0).getMessage());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotSame(newTreeBuilder, treeBuilder);
        assertSame(newTreeBuilder, parser.getTreeBuilder());
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
        assertEquals(10, parser.getMaxErrors());

        parser.setTrackErrors(0);
        assertEquals(0, parser.getMaxErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());

        parser.setTrackErrors(5);
        errors = parser.parseInput("<html><body><script>", null).getErrors();
        assertEquals(1, errors.size());
        assertEquals("Tag script not allowed in document", errors.get(0).getMessage());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        assertEquals(ParseSettings.DEFAULT, settings);

        settings = new ParseSettings();
        settings.setOmitScriptAndStyle();
        parser.settings(settings);
        Document document = parser.parseInput("<html><head><script><![CDATA[alert('Hello');]]></script><title>Test</title></head><body></body></html>", null);
        assertEquals(0, document.numHeadElements());
        assertNull(document.title());
        assertEquals(0, document.numBodyElements());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html><head></head><body><h1>Hello World</h1></body></html>", null);
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", document.html());
        assertEquals(1, document.numHeadElements());
        assertEquals(1, document.numBodyElements());
        assertEquals("Hello World", document.select("h1").text());

        document = Parser.parse("<html><head><title>Test</title></head><body></body></html>", null);
        assertEquals("<html><head><title>Test</title></head><body></body></html>", document.html());
        assertEquals(1, document.numHeadElements());
        assertNotNull(document.title());
        assertEquals("Test", document.title().text());
        assertEquals(0, document.numBodyElements());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World</h1>";
        Element context = Element.parse("<div></div>");
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        Node node = nodes.get(0);
        assertEquals(Element.class, node.getClass());
        assertEquals("h1", node.tagName());
        assertEquals("Hello World", node.text());
        assertEquals(1, nodes.size());
        context.appendChild(node);
        assertEquals(1, context.numChildren());
        assertEquals(node, context.children().get(0));
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><element>Hello World</element></xml>";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, null);
        Node node = nodes.get(0);
        assertEquals(Element.class, node.getClass());
        assertEquals("element", node.tagName());
        assertEquals("Hello World", node.text());
        assertEquals(1, nodes.size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        Document document = Parser.parseBodyFragment(bodyHtml, null);
        assertEquals("<html><head></head><body><div><p>Hello World</p></div></body></html>", document.html());
        assertEquals(1, document.numHeadElements());
        assertEquals(1, document.numBodyElements());
        assertEquals("Hello World", document.select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        assertEquals("<html><body><div><p>Hello World</p></div></body>", document.html());
        assertEquals(0, document.numHeadElements());
        assertEquals(1, document.numBodyElements());
        assertEquals("Hello World", document.select("p").text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;p&gt;Hello World&lt;/p&gt;";
        String unescapedString = Parser.unescapeEntities(string, false);
        assertEquals("<p>Hello World</p>", unescapedString);

        string = "&lt;script language=JavaScript&gt;alert('Hello');&lt;/script&gt;";
        unescapedString = Parser.unescapeEntities(string, true);
        assertEquals("<script language=\"JavaScript\">alert('Hello');</script>", unescapedString);
    }
}