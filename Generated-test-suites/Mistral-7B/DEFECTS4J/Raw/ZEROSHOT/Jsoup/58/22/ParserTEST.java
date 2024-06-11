package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser class.
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
        String html = "<html><head></head><body><p>Test</p></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);

        assertEquals("Test", document.body().text());
        assertNull(parser.getErrors().size());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><head></head><body><script>alert('Error')</script></body></html>";
        String baseUri = "http://example.com";

        parser.setTrackErrors(1);
        Document document = parser.parseInput(html, baseUri);

        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        Assert.assertEquals("Malformed HTML: Unexpected <script> tag", errors.get(0).toString());
    }

    @Test
    public void testParseInputWithMaxErrorsReached() {
        String html = "<html><head></head><body>";
        String baseUri = "http://example.com";

        parser.setTrackErrors(1);
        parser.parseInput(html, baseUri); // first parse

        String errorMessage = "Malformed HTML: unexpected end of input";
        parser.parseInput(html, baseUri); // second parse

        List<ParseError> errors = parser.getErrors();
        assertEquals(2, errors.size());
        Assert.assertEquals("Malformed HTML: unexpected end of input", errors.get(1).toString());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder expectedTreeBuilder = treeBuilder;
        TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

        assertSame(expectedTreeBuilder, actualTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

        assertSame(newTreeBuilder, actualTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithMaxErrorsGreaterThanZero() {
        parser.setTrackErrors(3);
        assertTrue(parser.isTrackErrors());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTrackErrorsWithNegativeNumber() {
        parser.setTrackErrors(-1);
    }

    @Test
    public void testGetErrors() {
        List<ParseError> expectedErrors = new ArrayList<>();
        ParseError error = new ParseError();
        expectedErrors.add(error);
        parser.setErrors(expectedErrors);

        List<ParseError> actualErrors = parser.getErrors();

        assertSame(expectedErrors, actualErrors);
    }

    @Test
    public void testSettings() {
        ParseSettings expectedSettings = treeBuilder.defaultSettings();
        ParseSettings actualSettings = parser.settings();

        assertSame(expectedSettings, actualSettings);
    }

    @Test
    public void testSettingsWithCustomSettings() {
        ParseSettings customSettings = new ParseSettings();
        parser.settings(customSettings);
        ParseSettings actualSettings = parser.settings();

        assertSame(customSettings, actualSettings);
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html><head></head><body><p>Test</p></body></html>", "http://example.com");
        assertEquals("Test", document.body().text());
        assertNull(Parser.getErrors().size());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Test</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertEquals("p", node.nodeName());
        assertEquals("Test", node.text());
        assertSame(context, node.parent());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><name>Test</name></xml>";
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertEquals("name", node.nodeName());
        assertEquals("Test", node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals("Test", document.body().text());
        assertNull(Parser.getErrors().size());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;p&gt;Test&amp;amp;&#39;Quotes&quot;";
        String unescapedString = Parser.unescapeEntities(string, false);
        assertEquals("<p>Test&amp;'Quotes\"", unescapedString);

        String unescapedAttributeString = Parser.unescapeEntities(string, true);
        assertEquals("<p>Test&apos;Quotes&quot;", unescapedAttributeString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<p>Test</p>", "http://example.com");
        assertEquals("Test", document.body().text());
        assertNull(Parser.getErrors().size());
    }
}