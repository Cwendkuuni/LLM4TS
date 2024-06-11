package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><head></head><body><div>Hello World</div></body></html>";
        Document doc = parser.parseInput(html, null);
        assertEquals(html, doc.html());

        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());

        parser.setTrackErrors(5);
        errors = parser.getErrors();
        assertFalse(errors.isEmpty());

        String invalidHtml = "<html><body></body><body>";
        parser.parseInput(invalidHtml, null);
        errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing start tag : body", error.getMessage());
        assertEquals(1, error.getLine());
        assertEquals(1, error.getColumn());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(null);
        assertEquals(null, parser.getTreeBuilder());

        parser.setTreeBuilder(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(3);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertEquals(5, parser.getMaxErrors());

        parser.setTrackErrors(-1);
        assertEquals(0, parser.getMaxErrors());
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body><div>Hello World</div></body></html>";
        Document doc = Parser.parse(html, null);
        assertEquals(html, doc.html());

        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());
    }

    @Test
    public void testParseFragment() {
        String html = "<div>Hello World</div>";
        Element context = Element.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(html, context, null);
        Node node = nodes.get(0);
        assertEquals("div", node.tagName());
        assertEquals("Hello World", node.text());

        errors = parser.getErrors();
        assertEquals(0, errors.size());
    }

    @Test
    public void testParseXmlFragment() {
        String xml = "<xml><name>John Doe</name></xml>";
        List<Node> nodes = Parser.parseXmlFragment(xml, null);
        Node node = nodes.get(0);
        assertEquals("name", node.tagName());
        assertEquals("John Doe", node.text());

        errors = parser.getErrors();
        assertEquals(0, errors.size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><div>Hello World</div></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        assertEquals("<html><head></head><body><div>Hello World</div></body></html>", doc.html());

        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;div&gt;Hello &amp; World&lt;/div&gt;";
        String unescaped = Parser.unescapeEntities(html, false);
        assertEquals("<div>Hello & World</div>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><div>Hello World</div></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        assertEquals("<html><head></head><body><div>Hello World</div></body></html>", doc.html());

        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());
    }
}