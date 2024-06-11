package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;

public class ParserTest {

    @Test
    public void testNewParser() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser);
    }

    @Test
    public void testNewHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        assertInstanceOf(HtmlTreeBuilder.class, parser.getTreeBuilder());
    }

    @Test
    public void testNewXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        assertInstanceOf(XmlTreeBuilder.class, parser.getTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><p>Hello world</p></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, null);

        assertEquals("<html><head></head><body><p>Hello world</p></body></html>", doc.html());
        assertEquals("<html><head></head><body><p>Hello world</p></body></html>", doc.outerHtml());

        Element body = doc.body();
        assertEquals("Hello world", body.text().toString());

        List<Node> children = body.children();
        assertEquals(1, children.size());
        Element p = (Element) children.get(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello world", p.text().toString());
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src=\"image.png\" alt=\"Image\"/>";
        Document doc = Parser.parse(html, "http://example.com");
        Element img = doc.selectFirst("img");
        assertEquals("image.png", img.attr("src"));
    }

    @Test
    public void testParseInputTrackingErrors() {
        String invalidHtml = "<html><body><script>throw new Error('Test')</script>";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        List<ParseError> errors = parser.parseInput(invalidHtml, null).getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character '<' (code 60) (line 3, column 1)", error.getMessage());
        assertEquals("line 3, column 1", error.getLocation());
    }

    @Test
    public void testParseInputDisableTrackingErrors() {
        String invalidHtml = "<html><body><script>throw new Error('Test')</script>";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);

        List<ParseError> errors = parser.parseInput(invalidHtml, null).getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseInputNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertThrows(IllegalArgumentException.class, () -> parser.parseInput(null, null));
    }

    @Test
    public void testParseInputEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", null);
        assertEquals("", doc.html());
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testParseInputEmptyStringAsBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", "");
        assertEquals("", doc.html());
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello world</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        assertEquals("<html><head></head><body><p>Hello world</p></body></html>", doc.html());
        assertEquals("<html><head></head><body>" + bodyHtml + "</body></html>", doc.outerHtml());
        assertEquals("Hello world", doc.body().text().toString());
    }

    @Test
    public void testParseBodyFragmentNullBodyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertThrows(IllegalArgumentException.class, () -> parser.parseBodyFragment(null, null));
    }

    @Test
    public void testParseBodyFragmentEmptyBodyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseBodyFragment("", null);
        assertEquals("<html><head></head><body></body></html>", doc.html());
        assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello world</p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        Node node = nodes.get(0);
        assertEquals("p", node.tagName());
        assertEquals("Hello world", node.text().toString());
        Element parsedNode = (Element) node;
        assertEquals(context, parsedNode.parent());
    }

    @Test
    public void testParseFragmentNullFragmentHtml() {
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(null, context, null);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseFragmentEmptyFragmentHtml() {
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment("", context, null);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseFragmentWithBaseUri() {
        String fragmentHtml = "<img src=\"image.png\" alt=\"Image\"/>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        Document doc = Jsoup.parse("<html><head></head><body><base href=\"http://example.com\"></body></html>", null);
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, doc.body().toString());
        Element img = (Element) nodes.get(0);
        assertEquals("image.png", img.attr("src"));
    }

    @Test
    public void testParseFragmentTrackingErrors() {
        String fragmentHtml = "<script>throw new Error('Test')</script>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        List<ParseError> errors = Parser.parseFragment(fragmentHtml, context, null).getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character '<' (code 60) (line 1, column 1)", error.getMessage());
        assertEquals("line 1, column 1", error.getLocation());
    }

    @Test
    public void testParseFragmentDisableTrackingErrors() {
        String fragmentHtml = "<script>throw new Error('Test')</script>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);

        List<ParseError> errors = Parser.parseFragment(fragmentHtml, context, null).getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseFragmentNullContext() {
        String fragmentHtml = "<p>Hello world</p>";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, null, null);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseFragmentNullBaseUri() {
        String fragmentHtml = "<img src=\"image.png\" alt=\"Image\"/>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        Element img = (Element) nodes.get(0);
        assertNull(img.attr("src"));
    }

    @Test
    public void testUnescapeHtmlEntities() {
        String htmlEscaped = "&lt;p&gt;Hello &amp; world!&lt;/p&gt;";
        String unescaped = Parser.unescapeHtmlEntities(htmlEscaped);
        assertEquals("<p>Hello world!</p>", unescaped);
    }

    @Test
    public void testUnescapeHtmlEntitiesNullString() {
        assertNull(Parser.unescapeHtmlEntities(null));
    }

    @Test
    public void testUnescapeHtmlEntitiesEmptyString() {
        assertEquals("", Parser.unescapeHtmlEntities(""));
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Parser newParser = parser.setTreeBuilder(new XmlTreeBuilder());
        assertInstanceOf(XmlTreeBuilder.class, newParser.getTreeBuilder());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Parser newParser = parser.setTrackErrors(2);
        assertTrue(newParser.isTrackErrors());
        assertEquals(2, newParser.getMaxErrors());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
        Parser newParser = parser.setTrackErrors(1);
        assertTrue(newParser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><body><script>throw new Error('Test')</script>", null);
        Parser newParser = parser.setTrackErrors(1);
        List<ParseError> errors = newParser.parseInput("<html><body><script>throw new Error('Test')</script>", null).getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character '<' (code 60) (line 2, column 0)", error.getMessage());
        assertEquals("line 2, column 0", error.getLocation());
    }

    @Test
    public void testGetErrorsEmptyList() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.parseInput("", null).getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testGetErrorsNullList() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNull(parser.parseInput(null, null).getErrors());
    }
}