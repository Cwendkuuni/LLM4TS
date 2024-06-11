package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        String html = "<html><head></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = parser.parseInput(html, baseUri);

        assertEquals("Hello, World!", doc.select("h1").text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        String html = "<html><head></head><body><h1>Hello, World!</h1><script>";
        String baseUri = "http://example.com";

        List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();

        assertEquals(1, errors.size());
        assertEquals("Unexpected char: < (char: 60, line: 4, pos: 11)", errors.get(0).getMessage());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        assertEquals(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);

        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);

        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        parser.setTrackErrors(5);

        List<ParseError> errors = new ArrayList<>();

        try {
            parser.parseInput("<script>", "http://example.com");
        } catch (ParseException e) {
            errors.addAll(e.getErrors());
        }

        assertEquals(5, errors.size());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        List<ParseError> errors = new ArrayList<>();
        parser.setTrackErrors(5);

        Document doc = parser.parseInput("<script>", "http://example.com");

        assertEquals(errors, parser.getErrors());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><head></head><body><h1>Hello, World!</h1></body></html>", "http://example.com");

        assertEquals("Hello, World!", doc.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = Parser.parseFragment("<div class=\"foo\">Hello, World!</div>", null, "http://example.com");

        assertEquals(1, nodes.size());
        assertEquals("Hello, World!", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<html><body><div class=\"foo\">Hello, World!</div></body></html>", "http://example.com");

        assertEquals("Hello, World!", doc.body().select("div.foo").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<html><body><div class=\"foo\">Hello, World!</div></body></html>", "http://example.com");

        assertEquals("Hello, World!", doc.body().select("div.foo").text());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseBodyFragmentRelaxedDeprecated() {
        Document doc = Parser.parseBodyFragmentRelaxed("<html><body><div class=\"foo\">Hello, World!</div></body></html>", "http://example.com");
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();

        assertEquals(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();

        assertEquals(new XmlTreeBuilder(), parser.getTreeBuilder());
    }
}