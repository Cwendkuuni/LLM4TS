package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><body><p>Hello World</p></body></html>", "https://www.example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Element body = doc.body();
        Assert.assertNotNull(body);
        List<Element> paragraphs = body.getElementsByTag("p");
        Assert.assertEquals(1, paragraphs.size());
        Assert.assertEquals("Hello World", paragraphs.get(0).text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser newParser = parser.setTreeBuilder(treeBuilder);
        Assert.assertSame(parser, newParser);
        Assert.assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(10);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Parser newParser = parser.setTrackErrors(5);
        Assert.assertSame(parser, newParser);
        Assert.assertTrue(parser.isTrackErrors());
        Assert.assertEquals(5, parser.getErrors().size());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertEquals(0, parser.getErrors().size());
        parser.setTrackErrors(5);
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body><p>Hello World</p></body></html>", "https://www.example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Element body = doc.body();
        Assert.assertNotNull(body);
        List<Element> paragraphs = body.getElementsByTag("p");
        Assert.assertEquals(1, paragraphs.size());
        Assert.assertEquals("Hello World", paragraphs.get(0).text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Hello World</p>", context, "https://www.example.com");
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("Hello World", nodes.get(0).toString());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Hello World</p>", "https://www.example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Element body = doc.body();
        Assert.assertNotNull(body);
        List<Element> paragraphs = body.getElementsByTag("p");
        Assert.assertEquals(1, paragraphs.size());
        Assert.assertEquals("Hello World", paragraphs.get(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Hello World</p>", "https://www.example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Element body = doc.body();
        Assert.assertNotNull(body);
        List<Element> paragraphs = body.getElementsByTag("p");
        Assert.assertEquals(1, paragraphs.size());
        Assert.assertEquals("Hello World", paragraphs.get(0).text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
        Assert.assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }
}