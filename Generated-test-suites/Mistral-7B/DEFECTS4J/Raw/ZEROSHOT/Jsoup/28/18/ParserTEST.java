package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser.
 */
public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parser.parseInput(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.outerHtml());
        assertEquals("<h1>Hello World</h1>", doc.body().html());
        assertEquals(1, doc.childNodes().size());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        parser.setTrackErrors(1);
        String html = "<html><body><h1>Hello World</h1><script>alert('Test')</script></body></html>";
        List<ParseError> errors = parser.getErrors();
        Document doc = parser.parseInput(html, "http://example.com");
        assertNotNull(doc);
        assertEquals(1, errors.size());
        assertEquals("Error parsing document: Unexpected tag name: script", errors.get(0).getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithNullHtml() {
        parser.parseInput(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithNullBaseUri() {
        parser.parseInput("<html><body><h1>Hello World</h1></body></html>", null);
    }

    @Test
    public void testGetTreeBuilder() {
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertSame(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithZero() {
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithNegativeNumber() {
        parser.setTrackErrors(-1);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertSame(ParseErrorList.noTracking(), errors);
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.outerHtml());
        assertEquals("<h1>Hello World</h1>", doc.body().html());
        assertEquals(1, doc.childNodes().size());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello</p><p>World</p></div>";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://example.com");
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertEquals("<div>", nodes.get(0).html());
        assertEquals("<p>Hello</p>", nodes.get(1).html());
        assertEquals("<p>World</p>", nodes.get(2).html());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello</p><p>World</p></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(doc);
        assertEquals("<html><head></head><body><div><p>Hello</p><p>World</p></div></body></html>", doc.html());
        assertEquals("<div>", doc.body().child(0).html());
        assertEquals("<p>Hello</p>", doc.body().child(0).child(0));
        assertEquals("<p>World</p>", doc.body().child(0).child(1));
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello</p><p>World</p></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        assertNotNull(doc);
        assertEquals("<html><head></head><body><div><p>Hello</p><p>World</p></div></body></html>", doc.html());
        assertEquals("<div>", doc.body().child(0).html());
        assertEquals("<p>Hello</p>", doc.body().child(0).child(0));
        assertEquals("<p>World</p>", doc.body().child(0).child(1));
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(XmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testParseInputWithBaseUriNull() {
        String html = "<img src='test.html' />";
        Document doc = parser.parseInput(html, null);
        assertNull(doc.baseUri());
    }
}