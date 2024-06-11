package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

/**
 * ParserTest.java - JUnit 4 test class for {@link Parser} class.
 */
public class ParserTest {

    @Test
    public void testParserInstance() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser);
    }

    @Test
    public void testParseInputHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parser.parseInput(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputXml() {
        Parser parser = new Parser(new XmlTreeBuilder());
        String xml = "<xml><data>Hello World</data></xml>";
        Document doc = parser.parseInput(xml, "http://example.com");
        assertNotNull(doc);
        assertEquals("<xml><data>Hello World</data></xml>", doc.html());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String invalidHtml = "<html><body><h1><<script>alert('hello')</script></h1></body></html>";
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput(invalidHtml, "http://example.com", errors);
        assertNotNull(doc);
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals(ParseError.TagMismatch, error.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1></body></html>";
        parser.parseInput(html, null);
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(newTreeBuilder == treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        assertNotNull(settings);
    }

    @Test
    public void testSettingsWithCustomParseSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings customSettings = new ParseSettings();
        parser.settings(customSettings);
        ParseSettings settings = parser.settings();
        assertTrue(customSettings == settings);
    }

    @Test
    public void testParseFragment() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String fragmentHtml = "<div>Hello World</div>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals("<div>Hello World</div>", node.html());
    }

    @Test
    public void testParseFragmentWithTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String fragmentHtml = "<div><script>alert('hello')</script></div>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        ParseErrorList errors = ParseErrorList.tracking(1);
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com", errors);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals("<div></div>", node.html()); // The script tag is ignored.
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals(ParseError.ScriptOrStyle, error.getType());
    }

    @Test
    public void testParseXmlFragment() {
        Parser parser = new Parser(new XmlTreeBuilder());
        String fragmentXml = "<xml><data>Hello World</data></xml>";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, "http://example.com");
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals("<data>Hello World</data>", node.html());
    }

    @Test
    public void testParseBodyFragment() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;Hello&amp; World&gt;";
        String unescapedString = Parser.unescapeEntities(string, false);
        assertEquals("<Hello World>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxedDeprecated() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
        // Deprecated since jsoup-1.14.3, recommended to use parser.parseBodyFragment(bodyHtml, "http://example.com") instead.
    }
}