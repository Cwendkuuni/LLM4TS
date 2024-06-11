package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for Parser.java
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
    public void testParseInputWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);

        assertEquals("Test", document.title());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseInputWithoutBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document document = parser.parseInput(html);

        assertEquals("", document.title());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder expectedTreeBuilder = new HtmlTreeBuilder();
        TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

        assertSame(expectedTreeBuilder, actualTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

        assertSame(newTreeBuilder, actualTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsAndParseInput() {
        String html = "<html><body></body></html>";
        parser.setTrackErrors(1);
        List<ParseError> errors = parser.parseInput(html, null).errors();

        assertFalse(parser.isTrackErrors());
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testGetErrors() {
        String html = "<html><body><script>alert('Error');</script></body></html>";
        List<ParseError> expectedErrors = new ArrayList<>();
        expectedErrors.add(new ParseError("Error", "Error", null, null, null, 0, 0));
        parser.setTrackErrors(1);
        List<ParseError> actualErrors = parser.parseInput(html, null).errors();

        assertSame(expectedErrors, actualErrors);
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><script>alert('Error');</script></body></html>";
        List<ParseError> errors = new ArrayList<>();
        errors.add(new ParseError("Error", "Error", null, null, null, 0, 0));
        ParseErrorList errorList = new ParseErrorList(errors);

        Document document = parser.parseInput(html, null, errorList, parser.settings());

        assertEquals("", document.body().text());
        assertTrue(document.errors().size() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testParseInputWithNullHtml() {
        parser.parseInput(null, null);
        fail("Expected NullPointerException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithEmptyHtml() {
        parser.parseInput("", null);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParseStaticHtml() {
        Document document = Parser.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");

        assertEquals("Test", document.title());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseStaticHtmlFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Test</p>", context, null);

        assertEquals(1, nodes.size());
        Element actualElement = (Element) nodes.get(0);
        assertEquals("p", actualElement.tagName());
        assertEquals("Test", actualElement.text());
        assertEquals(context, actualElement.parent());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<root><element>Test</element></root>", null);

        assertEquals(1, nodes.size());
        Element actualElement = (Element) nodes.get(0);
        assertEquals("element", actualElement.tagName());
        assertEquals("Test", actualElement.text());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<body><h1>Hello World</h1></body>", null);

        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;p&gt;<i>&amp;helloworld;</i>&gt;";
        String expectedString = "<p><i>helloworld</i></p>";
        String actualString = Parser.unescapeEntities(html, false);

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragment("<body><h1>Hello World</h1></body>", null);

        assertEquals("Hello World", document.body().text());
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
}