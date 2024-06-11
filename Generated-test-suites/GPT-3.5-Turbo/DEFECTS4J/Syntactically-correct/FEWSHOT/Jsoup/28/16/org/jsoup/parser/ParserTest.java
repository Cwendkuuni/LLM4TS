package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document document = parser.parseInput(html, baseUri);

        Assert.assertEquals("Test", document.body().select("h1").first().text());
        Assert.assertEquals(1, document.body().select("h1").size());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);

        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);

        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);

        Assert.assertEquals(5, parser.getTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Test</h2></body></html>";
        String baseUri = "http://www.example.com";
        parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();

        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected end tag (h2)", errors.get(0).getMessage());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Test", document.body().select("h1").first().text());
        Assert.assertEquals(1, document.body().select("h1").size());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        String fragmentHtml = "<p>Example</p>";
        String baseUri = "http://www.example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("p", nodes.get(0).nodeName());
        Assert.assertEquals("Example", ((Element)nodes.get(0)).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Example</p>";
        String baseUri = "http://www.example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals("Example", document.body().select("p").first().text());
        Assert.assertEquals(1, document.body().select("p").size());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Example</p>";
        String baseUri = "http://www.example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertEquals("Example", document.body().select("p").first().text());
        Assert.assertEquals(1, document.body().select("p").size());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        Assert.assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }
}