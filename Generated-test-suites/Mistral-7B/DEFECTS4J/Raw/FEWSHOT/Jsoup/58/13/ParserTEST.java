package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.NodeList;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

public class ParserTest {

    @Test
    public void testNewParser() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser);
        assertTrue(parser instanceof Parser);
        assertEquals(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.toString());
        assertSame(new HtmlTreeBuilder(), parser.getTreeBuilder());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(1, doc.childNodeSize());
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());
        assertNull(parser.getErrors());

        // test error tracking
        html = "<html><body><script>alert('Test')</script></body></html>";
        parser = new Parser(new HtmlTreeBuilder());
        ParseError error = new ParseError("Scripting is disabled", 1, 22);
        errors = ParseErrorList.tracking(1);
        doc = parser.parseInput(html, null, errors);
        assertEquals(1, errors.size());
        assertEquals(error, errors.get(0));
        assertSame(new HtmlTreeBuilder(), parser.getTreeBuilder());
        assertNull(doc);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertEquals(1, parser.getMaxErrors());

        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        assertEquals(0, parser.getMaxErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput("<html><body></body></html>", null, errors);
        List<ParseError> errorsList = parser.getErrors();
        assertEquals(0, errorsList.size());

        parser = new Parser(new HtmlTreeBuilder());
        errors = ParseErrorList.tracking(1);
        parser.parseInput("<html><body><script>alert('Test')</script></body></html>", null, errors);
        errorsList = parser.getErrors();
        assertEquals(1, errorsList.size());
        ParseError error = errorsList.get(0);
        assertEquals("Scripting is disabled", error.getMessage());
        assertEquals(1, error.getLine());
        assertEquals(22, error.getColumn());
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Document doc = Parser.parse(html, null);
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.toString());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(1, doc.childNodeSize());
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<ul><li>Test 1</li><li>Test 2</li></ul>";
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        Node firstNode = nodes.get(0);
        assertNotNull(firstNode);
        assertEquals(Element.class, firstNode.getClass());
        assertTrue(firstNode instanceof ListItem);
        assertEquals("<li>Test 1</li>", firstNode.toString());

        Node secondNode = nodes.get(1);
        assertNotNull(secondNode);
        assertEquals(Element.class, secondNode.getClass());
        assertTrue(secondNode instanceof ListItem);
        assertEquals("<li>Test 2</li>", secondNode.toString());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><tag attr=\"value\">Test</tag></xml>";
        ParseErrorList errors = ParseErrorList.tracking(1);
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, null, errors);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertNotNull(node);
        assertEquals(Element.class, node.getClass());
        assertTrue(node instanceof Element);
        assertEquals("<tag attr=\"value\">Test</tag>", node.toString());

        ParseError error = errors.get(0);
        assertEquals("No parser available for XML", error.getMessage());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test 1</p><p>Test 2</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodeSize());
        assertEquals(1, doc.body().childNodeSize());
        Node node = doc.body().child(0);
        assertTrue(node instanceof TextNode);
        assertEquals("<p>Test 1</p>", node.parent().outerHtml());
        assertEquals("Test 1", node.text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;test&gt; &amp; &apos; &quot; &eacute; &egrave; &lsquo; &rsquo;";
        String unescapedString = Parser.unescapeEntities(string, false);
        assertEquals("<test>", unescapedString);
        string = "&lt;test&gt; &amp; &apos; &quot; &eacute; &egrave; &lsquo; &rsquo;";
        unescapedString = Parser.unescapeEntities(string, true);
        assertEquals("<test>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<html><body><p>Test 1</p><p>Test 2</p></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, null);
        assertNotNull(doc);
        assertEquals(1, doc.childNodeSize());
        assertEquals(1, doc.body().childNodeSize());
        Node node = doc.body().child(0);
        assertTrue(node instanceof TextNode);
        assertEquals("<p>Test 1</p>", node.parent().outerHtml());
        assertEquals("Test 1", node.text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        assertTrue(parser instanceof Parser);
        assertEquals(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        assertTrue(parser instanceof Parser);
        assertEquals(new XmlTreeBuilder(), parser.getTreeBuilder());
    }
}