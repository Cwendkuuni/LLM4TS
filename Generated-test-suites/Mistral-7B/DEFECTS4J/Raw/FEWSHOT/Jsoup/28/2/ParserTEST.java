package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
        assertEquals("Test", doc.title().text());

        // Test with errors tracking
        Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
        parserWithErrors.setTrackErrors(5);
        Document docWithErrors = parserWithErrors.parseInput(html + "<script>alert('Hello')</script>", "http://example.com");
        List<ParseError> errors = parserWithErrors.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Error parsing startTag: Unexpected 'S' tag name: script\n", errors.get(0).getMessage());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(XmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithMaxErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());

        String htmlWithError = "<html><head><title>Test</title></head><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
        errors = parser.getErrors();
        assertEquals(1, errors.size());
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
        assertEquals("Test", doc.title().text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p><b>Hello</b> <i>World</i></p>";
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
        assertEquals(3, nodes.size());
        Node node1 = nodes.get(0);
        assertEquals(Element.Tag.P, node1.tagName());
        Node node2 = nodes.get(1);
        assertEquals(Element.Tag.B, node2.tagName());
        Node node3 = nodes.get(2);
        assertEquals(Element.Tag.I, node3.tagName());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p><b>Hello</b> <i>World</i></p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(doc);
        assertTrue(doc.body().childNodeSize() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithNullBaseUri() {
        String bodyHtml = "<p><b>Hello</b> <i>World</i></p>";
        Parser.parseBodyFragment(bodyHtml, null);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p><b>Hello</b> <i>World</i></p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml);
        assertNotNull(doc);
        assertTrue(doc.body().childNodeSize() > 0);
    }
}