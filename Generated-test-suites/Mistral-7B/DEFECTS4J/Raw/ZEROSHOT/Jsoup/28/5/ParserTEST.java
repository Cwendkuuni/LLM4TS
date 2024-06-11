package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;
    private Document document;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("<html><head/><body><p>Hello World!</p></body></html>", document.html());
    }

    @Test
    public void testGetTreeBuilder() {
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsAndGetErrors() {
        List<ParseError> errors = new ArrayList<>();
        parser.setTrackErrors(1);
        parser.parseInput("<html><head><title></title></head><body/></html>", "");
        errors.addAll(parser.getErrors());
        assertEquals(1, errors.size());
        assertNotNull(errors.get(0));
        assertEquals("Error parsing start tag : unexpected 't' (possibly mis-matched nesting)", errors.get(0).getMessage());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html><body><p>Hello World!</p></body></html>", "");
        assertNotNull(document);
        assertEquals("<html><head/><body><p>Hello World!</p></body></html>", document.html());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = Parser.parseFragment("<p>Hello World!</p>", null, "");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).tagName());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<body><p>Hello World!</p></body>", "");
        assertNotNull(document);
        assertNull(document.head());
        Element body = document.body();
        assertNotNull(body);
        List<Node> nodes = body.childNodes();
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        Parser.parse(null, "");
    }
}