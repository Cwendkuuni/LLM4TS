package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;

public class ParserTest {

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = parser.parseInput(html, "http://example.com");

        assertNotNull(doc);
        assertNotNull(doc.body());
        assertTrue(doc.body().childNodes().size() > 0);
        assertEquals("<p>Hello World</p>", doc.body().childNodes().get(0).outerHtml());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithMaxErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        assertTrue(parser.getErrors().size() > 0);
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.size() == 0);

        String invalidHtml = "</html><body>";
        errors = parser.getErrors();
        assertTrue(errors.size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, "http://example.com");
    }

    @Test
    public void testParseInputWithEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", "http://example.com");
        assertNotNull(doc);
        assertTrue(doc.body().childNodes().size() == 0);
    }

    @Test
    public void testParseInputWithMaxErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(3);
        String invalidHtml = "<html><body></body><script><script/><script<script";
        Document doc = parser.parseInput(invalidHtml, "http://example.com");

        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.size() == 3);
        assertEquals("Encountered an error at line 3, column 12. <script<script", errors.get(1).getMessage());
    }

    @Test
    public void testParseInputWithMaxErrorsAndTrackErrorsDisabled() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        String invalidHtml = "<html><body></body><script><script/><script<script";
        Document doc = parser.parseInput(invalidHtml, "http://example.com");

        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.size() == 0);
    }

    @Test
    public void testParseInputWithBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<img src='image.png' alt='Image' />";
        Document doc = parser.parseInput(html, "http://example.com");
        Element img = doc.select("img").first();
        String src = img.attr("src");
        assertEquals("http://example.com/image.png", src);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals("<html><head></head><body>" + bodyHtml + "</body></html>", doc.outerHtml());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");

        assertEquals(1, nodes.size());
        Element parsedNode = (Element) nodes.get(0);
        assertEquals("p", parsedNode.tagName());
        assertEquals("Hello World", parsedNode.text());

        context.appendChild(parsedNode);
        assertEquals("div<p>Hello World</p>", context.outerHtml());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentWithNullContext() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String fragmentHtml = "<p>Hello World</p>";
        parser.parseFragment(fragmentHtml, null, "http://example.com");
    }

    @Test
    public void testParseFragmentWithEmptyFragmentHtml() {
        String fragmentHtml = "";
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");

        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseInputWithHtmlWithScriptElement() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><p>Hello World</p><script src='script.js'></script></body></html>";
        Document doc = parser.parseInput(html, "http://example.com");

        Element script = doc.selectFirst("script");
        String src = script.attr("src");
        assertEquals("script.js", src);
    }
}