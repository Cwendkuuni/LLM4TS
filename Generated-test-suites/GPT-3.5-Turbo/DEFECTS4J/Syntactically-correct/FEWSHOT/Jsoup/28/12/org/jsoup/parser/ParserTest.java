package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;
    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testConstructor() {
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testGetTreeBuilder() {
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new TreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);

        assertSame(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);

        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getTrackErrors());
    }

    @Test
    public void testGetErrors() {
        assertNotNull(parser.getErrors());
        assertEquals(0, parser.getErrors().size());

        parser.setTrackErrors(5);
        assertTrue(parser.getErrors() instanceof ParseErrorList);
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element(Tag.valueOf("div"), "");
        String fragmentHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(fragmentHtml, nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("", doc.head().html());
        assertEquals(bodyHtml, doc.body().html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("", doc.head().html());
        assertEquals(bodyHtml, doc.body().html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testHtmlParser() {
        assertTrue(Parser.htmlParser().getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        assertTrue(Parser.xmlParser().getTreeBuilder() instanceof XmlTreeBuilder);
    }

    // Custom test cases
    @Test
    public void testSetTrackErrors_zero() {
        parser.setTrackErrors(0);

        assertFalse(parser.isTrackErrors());
        assertEquals(0, parser.getTrackErrors());
    }

    @Test
    public void testSetTrackErrors_negative() {
        parser.setTrackErrors(-5);

        assertFalse(parser.isTrackErrors());
        assertEquals(0, parser.getTrackErrors());
    }

    @Test
    public void testSetTrackErrors_positive() {
        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
        assertEquals(10, parser.getTrackErrors());
    }

    @Test
    public void testSetTrackErrors_multiple() {
        parser.setTrackErrors(5);
        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
        assertEquals(10, parser.getTrackErrors());
    }

    @Test
    public void testGetErrors_trackErrors() {
        parser.setTrackErrors(5);
        
        assertNotNull(parser.getErrors());
        assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testGetErrors_noTrackErrors() {
        assertNotNull(parser.getErrors());
        assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testParse_noBaseUri() {
        String html = "<html><head></head><body></body></html>";
        Document doc = Parser.parse(html, null);

        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertNull(doc.baseUri());
    }

    @Test
    public void testParseFragment_noContext() {
        String fragmentHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, null, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(fragmentHtml, nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment_noBaseUri() {
        String bodyHtml = "<p>Test</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);

        assertNotNull(doc);
        assertEquals("", doc.head().html());
        assertEquals(bodyHtml, doc.body().html());
        assertNull(doc.baseUri());
    }

    @Test
    public void testParseBodyFragmentRelaxed_noBaseUri() {
        String bodyHtml = "<p>Test</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);

        assertNotNull(doc);
        assertEquals("", doc.head().html());
        assertEquals(bodyHtml, doc.body().html());
        assertNull(doc.baseUri());
    }
}