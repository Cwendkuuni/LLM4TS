package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Xml;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TreeBuilder;

public class ParserTest {

    @Test
    public void testNewParser() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser);
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
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder returnedTreeBuilder = parser.getTreeBuilder();
        assertNotNull(returnedTreeBuilder);
        assertTrue(returnedTreeBuilder == newTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><div>Hello</div></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.isTrackErrors() ? ParseErrorList.tracking(5) : ParseErrorList.noTracking();
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        assertEquals(html, doc.html());
        List<ParseError> parseErrors = parser.getErrors();
        assertNull(parseErrors);

        parser.setTrackErrors(2);
        errors = ParseErrorList.tracking(2);
        doc = parser.parseInput(html, null, errors, parser.settings());
        parseErrors = parser.getErrors();
        assertNotNull(parseErrors);
        assertEquals(2, parseErrors.size());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello</div>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<Node> nodes = Parser.parseFragment(fragmentHtml, null, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals(Element.class, node.getClass());
        assertEquals("div", node.tagName());
        assertEquals("Hello", node.text());

        Element context = Element.parse("<html><body></body></html>").body();
        nodes = Parser.parseFragment(fragmentHtml, context, null);
        node = (Element) nodes.get(0);
        context.appendChild(node);
        assertEquals("div", context.child(0).tagName());
        assertEquals("Hello", context.child(0).text());
        assertEquals(1, context.child(0).childNodeSize());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><tag>Hello</tag></xml>";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals(Xml.class, node.getClass());
        assertEquals("tag", node.tagName());
        assertEquals("Hello", node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<html><body><div>Hello</div></body></html>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        assertNotNull(doc);
        assertNull(doc.head());
        assertEquals(1, doc.childNodeSize());
        Element body = doc.body();
        assertEquals(1, body.childNodeSize());
        assertEquals("div", body.child(0).tagName());
        assertEquals("Hello", body.child(0).text());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "<html>&amp;hello;&lt;world&gt;</html>";
        String text = Parser.unescapeEntities(html, false);
        assertEquals("<html>andhello<world></html>", text);
        text = Parser.unescapeEntities(html, true);
        assertEquals("<html>&amp;amp;and&lt;hello&gt;&lt;world&gt;</html>", text);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<html><body><div>Hello</div></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        assertNotNull(doc);
        assertNull(doc.head());
        assertEquals(1, doc.childNodeSize());
        Element body = doc.body();
        assertEquals(1, body.childNodeSize());
        assertEquals("div", body.child(0).tagName());
        assertEquals("Hello", body.child(0).text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }
}