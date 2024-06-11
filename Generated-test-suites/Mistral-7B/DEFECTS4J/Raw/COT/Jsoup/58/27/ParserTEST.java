package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Parser.
 */
public class ParserTest {

    /**
     * Test constructor Parser.
     */
    @Test
    public void testParser() {
        Parser parser = new Parser(null);
        assertNull(parser.getTreeBuilder());
        assertNull(parser.getErrors());
        assertEquals(Parser.DEFAULT_MAX_ERRORS, parser.getMaxErrors());
        assertTrue(parser.isTrackErrors());
        assertNull(parser.getSettings());
    }

    /**
     * Test method parseInput.
     */
    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        Document document = parser.parseInput(html, baseUri);
        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(0, errors.size());
    }

    /**
     * Test method parseInput with errors.
     */
    @Test
    public void testParseInputWithErrors() {
        String html = "<html><head><title>Test</title></head><body><script>alert('Hello')</script></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        Document document = parser.parseInput(html, baseUri);
        assertNotNull(document);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.SYNTAX_ERROR, error.getTag());
        assertEquals("Error parsing beginning of '<script>'", error.getMessage());
    }

    /**
     * Test method getTreeBuilder.
     */
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    /**
     * Test method setTreeBuilder.
     */
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotSame(newTreeBuilder, treeBuilder);
        assertEquals(XmlTreeBuilder.class, treeBuilder.getClass());
    }

    /**
     * Test method isTrackErrors.
     */
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    /**
     * Test method setTrackErrors.
     */
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        List<ParseError> errors = parser.getErrors();
        assertEquals(5, errors.size());
        for (ParseError error : errors) {
            assertNotNull(error);
        }
    }

    /**
     * Test method getErrors.
     */
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    /**
     * Test method settings.
     */
    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        assertNotNull(settings);
        assertEquals(HtmlTreeBuilder.defaultSettings(), settings);
    }

    /**
     * Test method settings.
     */
    @Test
    public void testSettingsWithCustomSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings customSettings = new ParseSettings();
        parser.settings(customSettings);
        ParseSettings settings = parser.settings();
        assertNotSame(customSettings, settings);
        assertEquals(customSettings, settings);
    }

    /**
     * Test static method parse.
     */
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document document = Parser.parse(html);
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    /**
     * Test static method parseFragment.
     */
    @Test
    public void testParseFragment() {
        String html = "<p>Hello World</p>";
        List<Node> nodes = Parser.parseFragment(html, null, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertNotNull(node);
        assertEquals(Element.class, node.getClass());
        Element element = (Element) node;
        assertEquals("p", element.tagName());
        assertEquals("Hello World", element.text());
    }

    /**
     * Test static method parseXmlFragment.
     */
    @Test
    public void testParseXmlFragment() {
        String xml = "<root><element>Hello World</element></root>";
        List<Node> nodes = Parser.parseXmlFragment(xml, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertNotNull(node);
        assertEquals(Element.class, node.getClass());
        Element element = (Element) node;
        assertEquals("element", element.tagName());
        assertEquals("Hello World", element.text());
    }

    /**
     * Test static method parseBodyFragment.
     */
    @Test
    public void testParseBodyFragment() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document document = Parser.parseBodyFragment(html, null);
        assertNotNull(document);
        Element body = document.body();
        List<Node> nodes = body.childNodes();
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertNotNull(node);
        assertEquals(Element.class, node.getClass());
        Element element = (Element) node;
        assertEquals("p", element.tagName());
        assertEquals("Hello World", element.text());
    }

    /**
     * Test static method unescapeEntities.
     */
    @Test
    public void testUnescapeEntities() {
        String html = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String unescaped = Parser.unescapeEntities(html, false);
        assertNotNull(unescaped);
        assertEquals("<p>Hello & World</p>", unescaped);

        String attribute = "class='btn btn-primary'";
        String unescapedAttribute = Parser.unescapeEntities(attribute, true);
        assertNotNull(unescapedAttribute);
        assertEquals("class='btn btn-primary'", unescapedAttribute);
    }
}