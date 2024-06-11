package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", doc.html());
        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><head></head><body><p>Hello, <script>alert('XSS')</script> World!</p></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", doc.html());
        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", doc.outerHtml());

        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing tag Script", error.getMessage());
        assertEquals(1, error.getLine());
        assertEquals(7, error.getColumn());
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><head></head><body><p>Hello, <script>alert('XSS')</script> World!</p><p>Hello, <script>alert('XSS')</script> World!</p></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(2);

        Document doc = parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();

        assertNotNull(doc);
        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", doc.html());
        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", doc.outerHtml());

        assertEquals(2, errors.size());
        ParseError error1 = errors.get(0);
        assertEquals("Error parsing tag Script", error1.getMessage());
        assertEquals(1, error1.getLine());
        assertEquals(7, error1.getColumn());

        ParseError error2 = errors.get(1);
        assertEquals("Error parsing tag Script", error2.getMessage());
        assertEquals(1, error2.getLine());
        assertEquals(7, error2.getColumn());
    }

    @Test
    public void testParseInputWithNoTrackErrors() {
        String html = "<html><head></head><body><p>Hello, <script>alert('XSS')</script> World!</p></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", doc.html());
        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", doc.outerHtml());

        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(new HtmlTreeBuilder(), treeBuilder);
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
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://example.com";

        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", doc.html());
        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello, <strong>World!</strong></div>";
        Element context = new Element("body");
        String baseUri = "https://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals("div", node.tagName());
        assertEquals("<div><strong>World!</strong></div>", node.outerHtml());

        Element div = (Element) node;
        assertEquals("Hello, ", div.text());

        assertSame(context, div.parent());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello, <strong>World!</strong></div>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head></head><body><div>Hello, <strong>World!</strong></div></body></html>", doc.html());
        assertEquals("<html><head></head><body><div>Hello, <strong>World!</strong></div></body></html>", doc.outerHtml());

        Element div = doc.body().get(0).child(0);
        assertEquals("Hello, ", div.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Hello, <strong>World!</strong></div>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head></head><body><div>Hello, <strong>World!</strong></div></body></html>", doc.html());
        assertEquals("<html><head></head><body><div>Hello, <strong>World!</strong></div></body></html>", doc.outerHtml());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(new HtmlTreeBuilder(), treeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(new XmlTreeBuilder(), treeBuilder);
    }
}