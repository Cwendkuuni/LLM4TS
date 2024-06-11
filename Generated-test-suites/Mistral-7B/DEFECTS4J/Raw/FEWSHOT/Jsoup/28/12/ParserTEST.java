package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseError;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParserInstance() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser);
    }

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        assertTrue(doc.body().tagName().equals("body"));
        assertTrue(doc.head() == null);
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
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertTrue(parser.getTreeBuilder() == treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors() == false);
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors() == true);
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = new ArrayList<>();
        errors.add(new ParseError("", 0, 0, ParseError.MalformedTagStartState.VALUE, ""));
        parser.setErrors(errors);
        List<ParseError> getErrors = parser.getErrors();
        assertNotNull(getErrors);
        assertTrue(getErrors.size() == 1);
        assertTrue(getErrors.get(0).getMessage().equals(""));
    }

    @Test
    public void testParse() {
        Parser parser = Parser.htmlParser();
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parser.parse(html);
        assertNotNull(doc);
        assertTrue(doc.body().tagName().equals("body"));
        assertTrue(doc.head() == null);
    }

    @Test
    public void testParseFragment() {
        Parser parser = Parser.htmlParser();
        String fragmentHtml = "<h1>Hello World</h1>";
        Element context = Jsoup.parseBodyFragment("<p>").firstChild();
        String baseUri = "http://example.com";
        List<Node> nodes = parser.parseFragment(fragmentHtml, context, baseUri);
        Node node = nodes.get(0);
        assertTrue(node.tagName().equals("h1"));
        assertTrue(node.text().equals("Hello World"));
    }

    @Test
    public void testParseBodyFragment() {
        Parser parser = Parser.htmlParser();
        String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertTrue(doc.head() == null);
        Element body = doc.body();
        Node node = body.childNodes().get(0);
        assertTrue(node.tagName().equals("h1"));
        assertTrue(node.text().equals("Hello World"));
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Parser parser = Parser.htmlParser();
        String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parser.parseBodyFragmentRelaxed(bodyHtml);
        assertTrue(doc.head() == null);
        Element body = doc.body();
        Node node = body.childNodes().get(0);
        assertTrue(node.tagName().equals("h1"));
        assertTrue(node.text().equals("Hello World"));
    }
}