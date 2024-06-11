package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;

    /**
     * Initialize Parser and TreeBuilder before each test
     */
    @Before
    public void setup() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    /**
     * Test constructor: Parser(TreeBuilder treeBuilder)
     */
    @Test
    public void testParserConstructor() {
        Assert.assertNotNull(parser);
        Assert.assertSame(treeBuilder, parser.getTreeBuilder());
    }

    /**
     * Test parseInput method with valid HTML
     */
    @Test
    public void testParseInputValidHtml() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = parser.parseInput(html, "http://test.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Page", doc.title().text());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    /**
     * Test parseInput method with invalid HTML
     */
    @Test
    public void testParseInputInvalidHtml() {
        String html = "<html<title"; // missing '>' in title tag
        List<ParseError> errors = new ArrayList<>();
        Document doc = parser.parseInput(html, "http://test.com", null, 1);
        Assert.assertNull(doc);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing starting tag : html<title", errors.get(0).getMessage());
    }

    /**
     * Test getErrors method
     */
    @Test
    public void testGetErrors() {
        String invalidHtml = "<html<title"; // missing '>' in title tag
        List<ParseError> errors = new ArrayList<>();
        Document doc = parser.parseInput(invalidHtml, "http://test.com", errors, 1);
        Assert.assertNull(doc);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing starting tag : html<title", errors.get(0).getMessage());
        Assert.assertEquals(errors, parser.getErrors());
    }

    /**
     * Test isTrackErrors method
     */
    @Test
    public void testIsTrackErrors() {
        Assert.assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    /**
     * Test setTrackErrors method
     */
    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());
    }

    /**
     * Test parse static method
     */
    @Test
    public void testParseStaticMethod() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Page", doc.title().text());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    /**
     * Test parseFragment static method
     */
    @Test
    public void testParseFragmentStaticMethod() {
        String fragmentHtml = "<div>Test fragment</div>";
        Element context = new Element("body");
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://test.com");
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("Test fragment", nodes.get(0).text());
    }
}