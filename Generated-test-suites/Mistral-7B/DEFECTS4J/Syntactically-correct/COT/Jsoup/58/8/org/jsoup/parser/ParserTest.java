package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    private static final String HTML_SAMPLE = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "<meta charset=\"UTF-8\">\n" +
            "<title>Test Page</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1 id=\"title\">Hello World</h1>\n" +
            "<p id=\"paragraph\">This is a test.</p>\n" +
            "</body>\n" +
            "</html>";

    private static final String XML_SAMPLE = "<root><element1>Content 1</element1><element2><subelement>Content 2</subelement></element2></root>";

    private static final String INVALID_HTML = "<html><script>alert('Hello World');</script>";

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    /**
     * Test for Parser constructor.
     */
    @Test
    public void testParserConstructor() {
        assertNotNull(parser);
    }

    /**
     * Test for parseInput method.
     */
    @Test
    public void testParseInput() {
        Document document = parser.parseInput(HTML_SAMPLE, "http://example.com");
        assertNotNull(document);
        assertEquals("Test Page", document.title().text());
        Element title = document.getElementById("title");
        assertEquals("Hello World", title.text());
        Element paragraph = document.getElementById("paragraph");
        assertEquals("This is a test.", paragraph.text());
        assertTrue(document.body().children().size() == 2);
    }

    /**
     * Test for parseInput method with invalid HTML.
     */
    @Test(expected = org.jsoup.parser.ParseException.class)
    public void testParseInputInvalidHtml() {
        parser.parseInput(INVALID_HTML, "http://example.com");
    }

    /**
     * Test for parseInput method with error tracking enabled.
     */
    @Test
    public void testParseInputTrackErrors() {
        parser.setTrackErrors(1);
        Document document = parser.parseInput(HTML_SAMPLE, "http://example.com");
        assertNotNull(document);
        assertEquals("Test Page", document.title().text());
        Element title = document.getElementById("title");
        assertEquals("Hello World", title.text());
        Element paragraph = document.getElementById("paragraph");
        assertEquals("This is a test.", paragraph.text());
        assertTrue(document.body().children().size() == 2);

        List<ParseError> errors = parser.getErrors();
        assertFalse(errors.isEmpty());
        assertEquals(1, errors.size());
        assertNotNull(errors.get(0).getMessage());
    }

    /**
     * Test for parseInput method with error tracking disabled.
     */
    @Test
    public void testParseInputNoTrackErrors() {
        parser.setTrackErrors(0);
        Document document = parser.parseInput(HTML_SAMPLE, "http://example.com");
        assertNotNull(document);
        assertEquals("Test Page", document.title().text());
        Element title = document.getElementById("title");
        assertEquals("Hello World", title.text());
        Element paragraph = document.getElementById("paragraph");
        assertEquals("This is a test.", paragraph.text());
        assertTrue(document.body().children().size() == 2);

        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());
    }

    /**
     * Test for getTreeBuilder method.
     */
    @Test
    public void testGetTreeBuilder() {
        assertSame(parser.getTreeBuilder(), parser.treeBuilder);
    }

    /**
     * Test for setTreeBuilder method.
     */
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertSame(parser.getTreeBuilder(), newTreeBuilder);

        TreeBuilder oldTreeBuilder = parser.getTreeBuilder();
        parser.setTreeBuilder(parser.treeBuilder);
        assertNotSame(parser.getTreeBuilder(), oldTreeBuilder);
    }

    /**
     * Test for isTrackErrors method.
     */
    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    /**
     * Test for setTrackErrors method.
     */
    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    /**
     * Test for getErrors method.
     */
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());

        parser.parseInput(INVALID_HTML, "http://example.com");
        errors = parser.getErrors();
        assertFalse(errors.isEmpty());
    }

    /**
     * Test for parse method.
     */
    @Test
    public void testParse() {
        Document document = Parser.parse(HTML_SAMPLE);
        assertNotNull(document);
        assertEquals("Test Page", document.title().text());
        Element title = document.getElementById("title");
        assertEquals("Hello World", title.text());
        Element paragraph = document.getElementById("paragraph");
        assertEquals("This is a test.", paragraph.text());
        assertTrue(document.body().children().size() == 2);
    }

    /**
     * Test for parseFragment method.
     */
    @Test
    public void testParseFragment() {
        List<Node> nodes = Parser.parseFragment(HTML_SAMPLE, null, "http://example.com");
        assertFalse(nodes.isEmpty());
        assertEquals(6, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
        assertTrue(nodes.get(2) instanceof TextNode);
        assertTrue(nodes.get(5) instanceof TextNode);
    }

    /**
     * Test for parseXmlFragment method.
     */
    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment(XML_SAMPLE, "http://example.com");
        assertFalse(nodes.isEmpty());
        assertEquals(5, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(2) instanceof Element);
        assertTrue(nodes.get(2) instanceof Element);
        assertTrue(nodes.get(4) instanceof TextNode);
    }

    /**
     * Test for parseBodyFragment method.
     */
    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment(HTML_SAMPLE, "http://example.com");
        assertNotNull(document);
        assertEquals("Test Page", document.title().text());
        Element title = document.getElementById("title");
        assertEquals("Hello World", title.text());
        Element paragraph = document.getElementById("paragraph");
        assertEquals("This is a test.", paragraph.text());
        assertTrue(document.body().children().size() == 2);
    }

    /**
     * Test for unescapeEntities method.
     */
    @Test
    public void testUnescapeEntities() {
        String input = "&lt;p&gt;This is a &amp; test.&lt;/p&gt;";
        String output = Parser.unescapeEntities(input, false);
        assertEquals("<p>This is a test.</p>", output);
    }

    /**
     * Test for parseBodyFragmentRelaxed method.
     * Deprecated, use parseBodyFragment or parseFragment instead.
     */
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed(HTML_SAMPLE, "http://example.com");
        assertNotNull(document);
        assertEquals("Test Page", document.title().text());
        Element title = document.getElementById("title");
        assertEquals("Hello World", title.text());
        Element paragraph = document.getElementById("paragraph");
        assertEquals("This is a test.", paragraph.text());
        assertTrue(document.body().children().size() == 2);
    }

    /**
     * Test for htmlParser method.
     */
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        assertSame(parser.treeBuilder, new HtmlTreeBuilder());
    }

    /**
     * Test for xmlParser method.
     */
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        assertSame(parser.treeBuilder, new XmlTreeBuilder());
    }
}