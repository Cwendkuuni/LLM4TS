package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

public class ParserTest {

    @Test
    public void testParser() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser);
        assertSame(parser.getClass(), parser.getTreeBuilder().getClass());
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());
        assertNull(parser.settings());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, null);
        assertNotNull(document);
        Element body = document.body();
        Element h1 = body.child(0);
        assertEquals("Hello", h1.text());
    }

    @Test
    public void testParseInputTrackErrors() {
        String html = "<html><body><h1>Hello</h1><script>alert('World')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document document = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Encountered an unexpected token \"\". Was expecting: <START_TAG>", error.getMessage());
    }

    @Test
    public void testParseInputNoTrackErrors() {
        String html = "<html><body><h1>Hello</h1><script>alert('World')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        Document document = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertSame(parser.getTreeBuilder().getClass(), treeBuilder.getClass());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithNegativeValue() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(-1);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testParseStaticMethods() {
        String html = "<html><body><h1>Hello</h1></body></html>";
        Document document = Parser.parse(html, null);
        assertNotNull(document);
        Element body = document.body();
        Element h1 = body.child(0);
        assertEquals("Hello", h1.text());

        String fragmentHtml = "<div>Hello</div>";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, null, null);
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
        Node node = nodes.get(0);
        assertEquals(Node.ELEMENT_NODE, node.nodeType());
        assertEquals("div", node.tagName());
        TextNode textNode = (TextNode) node.child(0);
        assertEquals("Hello", textNode.text());

        String xmlFragment = "<root><child>Hello</child></root>";
        nodes = Parser.parseXmlFragment(xmlFragment, null);
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
        node = nodes.get(0);
        assertEquals(Node.ELEMENT_NODE, node.nodeType());
        assertEquals("root", node.tagName());
        textNode = (TextNode) node.child(0);
        assertEquals("Hello", textNode.text());

        String bodyHtml = "<body><script>alert('World')</script></body>";
        document = Parser.parseBodyFragment(bodyHtml, null);
        assertNotNull(document);
        Element script = document.body().child(0);
        assertNotNull(script);
        assertEquals("script", script.tagName());

        String fragmentHtmlWithScript = "<div><script>alert('World')</script>Hello</div>";
        nodes = Parser.parseFragment(fragmentHtmlWithScript, null, null);
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
        node = nodes.get(0);
        assertEquals(Node.ELEMENT_NODE, node.nodeType());
        assertEquals("div", node.tagName());
        script = (Element) node.child(0);
        assertNotNull(script);
        assertEquals("script", script.tagName());
        textNode = (TextNode) node.child(1);
        assertEquals("Hello", textNode.text());
    }

}