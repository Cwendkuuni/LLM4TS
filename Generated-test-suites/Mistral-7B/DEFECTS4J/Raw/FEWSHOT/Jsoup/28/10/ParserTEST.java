package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

public class ParserTest {

    @Test
    public void testParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com/";

        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(10);
        Document doc = parser.parseInput(html, baseUri, errors);

        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(1, doc.numberOfChildElements());
        assertEquals(1, doc.numberOfTextNodes());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><p>Hello <script>World</script></p></body></html>";
        String baseUri = "http://example.com/";

        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput(html, baseUri, errors);

        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(1, doc.numberOfChildElements());
        assertEquals(1, doc.numberOfTextNodes());
        assertFalse(errors.isEmpty());
        assertEquals("<script>", errors.get(0).getTagName());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(treeBuilder, parser.treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(newTreeBuilder, treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder(), 5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<html><body><p>Hello <script>World</script></p></body></html>";
        String baseUri = "http://example.com/";

        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput(html, baseUri, errors);

        assertEquals(1, errors.size());
        assertSame("script", errors.get(0).getTagName());
    }

    @Test
    public void testParse() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com/";

        Document doc = Parser.parse(html, baseUri);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(1, doc.numberOfChildElements());
        assertEquals(1, doc.numberOfTextNodes());
    }

    @Test
    public void testParseFragment() {
        String html = "<p>Hello World</p>";
        Element context = new Element("html");
        String baseUri = "http://example.com/";

        List<Node> nodes = Parser.parseFragment(html, context, baseUri);
        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).tagName());
        assertEquals("Hello World", nodes.get(0).text());

        // test with context
        context = new Element("div");
        context.appendChild(new Element("p").text("I'm a context element"));

        nodes = Parser.parseFragment(html, context, baseUri);
        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).tagName());
        assertEquals("Hello World", nodes.get(0).text());
        assertEquals("I'm a context element", context.text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<body><p>Hello World</p></body>";
        String baseUri = "http://example.com/";

        Document doc = Parser.parseBodyFragment(html, baseUri);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(1, doc.numberOfChildElements());
        assertEquals(1, doc.numberOfTextNodes());
        assertEquals("p", doc.body().tagName());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<body><p>Hello World</p></body>";
        String baseUri = "http://example.com/";

        Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(1, doc.numberOfChildElements());
        assertEquals(1, doc.numberOfTextNodes());
        assertEquals("p", doc.body().tagName());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testUnescapeHtmlEntities() {
        String input = "&amp; &lt; &gt; &apos; &quot;";
        String expected = "&amp; &lt; &gt; ' \"";
        String output = Parser.unescapeHtmlEntities(input);
        assertEquals(expected, output);
    }
}