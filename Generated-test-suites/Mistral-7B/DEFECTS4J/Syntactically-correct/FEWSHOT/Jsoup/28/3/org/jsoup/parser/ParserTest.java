package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.Jsoup.parse;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", doc.html());

        // test with error tracking
        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        html = "<html><head></head><body><p>Hello <strong>World</strong></p></body></html>";
        doc = parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("<p>Hello <strong>World</strong></p>", doc.body().html());
        assertEquals("<p>Hello <strong>World</strong></p>", errors.get(0).getMessage());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(XmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());

        parser.setTrackErrors(1);
        String html = "<html><head></head><body><p>Hello <strong>World</strong></p></body></html>";
        parser.parseInput(html, null);
        errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("<p>Hello <strong>World</strong></p>", errors.get(0).getNode().html());
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html);
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", doc.html());

        html = "<html><head></head><body><p>Hello <strong>World</strong></p></body></html>";
        doc = Parser.parse(html);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("<p>Hello <strong>World</strong></p>", doc.body().html());
        assertEquals("<p>Hello <strong>World</strong></p>", errors.get(0).getMessage());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = Element.parse("<div></div>");
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Node node = nodes.get(0);
        assertEquals("<p>Hello World</p>", node.html());
        assertEquals("<div><p>Hello World</p></div>", context.html());
    }

    @Test   
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://www.example.com");
        assertEquals("<html><head></head><body>" + bodyHtml + "</body></html>", doc.html());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "https://www.example.com");
        assertEquals("<html><head></head><body>" + bodyHtml + "</body></html>", doc.html());
        // parseBodyFragmentRelaxed is deprecated, so we cannot test its errors handling
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