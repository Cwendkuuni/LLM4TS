package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><div>Hello World</div></body></html>";
        Document doc = Parser.parse(html, null);

        Element body = doc.body();
        Element div = body.child(0);
        assertSame(div, doc.selectFirst("div"));
        assertEquals("Hello World", div.text());
    }

    @Test
    public void testParseWithTrackingErrors() {
        String html = "<html><body><script>alert('Hello World')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        ParseErrorList errors = parser.parseInput(html, null).errors();
        assertNotNull(errors);
        assertEquals("Unexpected '<script>' tag", errors.get(0).getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, null);
    }

    @Test
    public void testParseFragment() {
        String html = "<div>Hello World</div>";
        Element context = new Element("html");
        List<Node> nodes = Parser.parseFragment(html, context, null);
        assertEquals(1, nodes.size());
        assertEquals(Element.class, nodes.get(0).getClass());
        assertEquals("div", nodes.get(0).tagName());
        assertEquals("Hello World", nodes.get(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        String xml = "<book><title>Java Programming for Dummies</title></book>";
        List<Node> nodes = Parser.parseXmlFragment(xml, null);
        assertEquals(2, nodes.size());
        assertSame(Element.class, nodes.get(0).getClass());
        assertEquals("book", nodes.get(0).tagName());
        assertSame(Element.class, nodes.get(1).getClass());
        assertEquals("title", nodes.get(1).tagName());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<body><p>Hello World</p></body>";
        Document doc = Parser.parseBodyFragment(html, null);
        Element body = doc.body();
        Element p = body.child(0);
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String unescaped = Parser.unescapeEntities(html, false);
        assertEquals("<p>Hello & World</p>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, null);
        assertNotNull(doc);
    }

    @Test
    public void testParseWithSettings() {
        ParseSettings settings = new ParseSettings();
        settings.setOmitScriptAndStyle();
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.settings(settings);
        String html = "<html><body><script>alert('Hello World')</script></body></html>";
        Document doc = parser.parseInput(html, null);
        assertNull(doc.body().child(0));
    }

    @Test
    public void testParseWithMaxErrors() {
        String html = "<html><body><script>alert('Hello World')</script><script>alert('Hello Again')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(2);
        ParseErrorList errors = parser.parseInput(html, null).errors();
        assertEquals(2, errors.size());
        assertEquals("Unexpected '<script>' tag", errors.get(0).getMessage());
        assertEquals("Unexpected '<script>' tag", errors.get(1).getMessage());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(new HtmlTreeBuilder(), treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        TreeBuilder newTreeBuilder = parser.getTreeBuilder();
        assertSame(new XmlTreeBuilder(), newTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        int maxErrors = parser.getMaxErrors();
        assertEquals(1, maxErrors);
        parser.setTrackErrors(2);
        maxErrors = parser.getMaxErrors();
        assertEquals(2, maxErrors);
        parser.setTrackErrors(0);
        maxErrors = parser.getMaxErrors();
        assertEquals(0, maxErrors);
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = new ArrayList<>();
        errors.add(new ParseError("Unexpected '<script>' tag", 1, 10, "input.html"));
        parser.parseInput("input.html", null).errors(errors);
        List<ParseError> parsedErrors = parser.getErrors();
        assertEquals(1, parsedErrors.size());
        assertEquals(new ParseError("Unexpected '<script>' tag", 1, 10, "input.html"), parsedErrors.get(0));
    }

    @Test
    public void testSetTreeBuilderNull() {
        Parser parser = new Parser(null);
        assertNull(parser.getTreeBuilder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTreeBuilderInvalid() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(new Object());
    }

    @Test
    public void testParseWithSettingsAndTrackErrors() {
        ParseSettings settings = new ParseSettings();
        settings.setOmitScriptAndStyle();
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.settings(settings);
        parser.setTrackErrors(1);
        String html = "<html><body><script>alert('Hello World')</script></body></html>";
        Document doc = parser.parseInput(html, null);
        assertNull(doc.body().child(0));
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Unexpected '<script>' tag", errors.get(0).getMessage());
    }
}