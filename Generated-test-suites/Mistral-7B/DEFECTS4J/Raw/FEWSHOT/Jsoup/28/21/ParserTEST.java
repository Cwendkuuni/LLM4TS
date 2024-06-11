package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.ParseError;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><div>Hello</div></body></html>";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com/", doc.baseUri());
        assertEquals("http://example.com/html", doc.location().toString());
        assertEquals("html", doc.tagName());
        assertEquals(2, doc.childNodes().size());

        Element head = doc.head();
        assertEquals(0, head.childNodes().size());

        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals("div", body.childNodes().get(0).tagName());
        assertEquals("Hello", body.childNodes().get(0).text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><head></head><body><div>Hello<script>alert('world');</script></div></body></html>";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com/", doc.baseUri());
        assertEquals("http://example.com/html", doc.location().toString());
        assertEquals("html", doc.tagName());
        assertEquals(2, doc.childNodes().size());

        Element head = doc.head();
        assertEquals(0, head.childNodes().size());

        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals("div", body.childNodes().get(0).tagName());
        assertEquals("Hello", body.childNodes().get(0).text());

        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.SYNTAX_ERR, error.getType());
        assertEquals(6, error.getLine());
        assertEquals(9, error.getColumn());
        assertEquals("Expecting ';' or '>'", error.getMessage());
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><head></head><body><div><script>for(i=0;i<100000;i++){}</script></div></body></html>";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(5);
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com/", doc.baseUri());
        assertEquals("http://example.com/html", doc.location().toString());
        assertEquals("html", doc.tagName());
        assertEquals(2, doc.childNodes().size());

        Element head = doc.head();
        assertEquals(0, head.childNodes().size());

        Element body = doc.body();
        assertEquals("body", body.tagName());
        Element div = body.childNodes().get(0);
        assertEquals("div", div.tagName());
        List<Node> scriptNodes = div.childNodes().stream()
                .filter(n -> "script".equals(n.tagName()))
                .collect(Collectors.toList());
        assertEquals(1, scriptNodes.size());
        Node scriptNode = scriptNodes.get(0);
        assertEquals("script", scriptNode.tagName());
        List<Node> nodes = scriptNode.childNodes();
        assertEquals(1, nodes.size());
        assertEquals("for", nodes.get(0).tagName());
        assertEquals(5, parser.getErrors().size());
    }

    @Test
    public void testParseInputWithNoTrackErrors() {
        String html = "<html><head></head><body><div><script>for(i=0;i<100000;i++){}</script></div></body></html>";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com/", doc.baseUri());
        assertEquals("http://example.com/html", doc.location().toString());
        assertEquals("html", doc.tagName());
        assertEquals(2, doc.childNodes().size());

        Element head = doc.head();
        assertEquals(0, head.childNodes().size());

        Element body = doc.body();
        assertEquals("body", body.tagName());
        Element div = body.childNodes().get(0);
        assertEquals("div", div.tagName());
        List<Node> children = div.children();
        assertEquals(0, children.size());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = Parser.htmlParser();
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotSame(treeBuilder, parser.getTreeBuilder());
        assertSame(newTreeBuilder, treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = Parser.htmlParser();
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(10);
        assertEquals(10, parser.getMaxErrors());
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body><div>Hello</div></body></html>";
        String baseUri = "http://example.com/";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com/", doc.baseUri());
        assertEquals("http://example.com/html", doc.location().toString());
        assertEquals("html", doc.tagName());
        assertEquals(2, doc.childNodes().size());

        Element head = doc.head();
        assertEquals(0, head.childNodes().size());

        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals("div", body.childNodes().get(0).tagName());
        assertEquals("Hello", body.childNodes().get(0).text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello</div>";
        Element context = new Element("html");
        String baseUri = "http://example.com/";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertEquals(1, nodes.size());
        Element node = (Element) nodes.get(0);
        assertEquals("div", node.tagName());
        assertEquals("Hello", node.text());
        assertEquals(context, node.parent());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><div>Hello</div></body>";
        String baseUri = "http://example.com/";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com/", doc.baseUri());
        assertEquals("http://example.com/", doc.location().toString());
        assertEquals("body", doc.tagName());
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(1, body.childNodes().size());
        Element div = (Element) body.childNodes().get(0);
        assertEquals("div", div.tagName());
        assertEquals("Hello", div.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><div>Hello</div></body>";
        String baseUri = "http://example.com/";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com/", doc.baseUri());
        assertEquals("http://example.com/", doc.location().toString());
        assertEquals("html", doc.tagName());
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(1, body.childNodes().size());
        Element div = (Element) body.childNodes().get(0);
        assertEquals("div", div.tagName());
        assertEquals("Hello", div.text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertSame(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertSame(new XmlTreeBuilder(), parser.getTreeBuilder());
    }
}