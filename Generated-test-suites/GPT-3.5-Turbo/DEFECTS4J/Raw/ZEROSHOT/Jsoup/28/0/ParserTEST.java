package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser updatedParser = parser.setTreeBuilder(treeBuilder);
        Assert.assertEquals(treeBuilder, updatedParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Assert.assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(3);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        Assert.assertTrue(parser.isTrackErrors());
        Assert.assertEquals(5, parser.getErrors().size());

        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());
        Assert.assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Header</h1><p>Paragraph</p></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Header", doc.select("h1").text());
        Assert.assertEquals("Paragraph", doc.select("p").text());
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Header</h1><p>Paragraph</p></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Header", doc.select("h1").text());
        Assert.assertEquals("Paragraph", doc.select("p").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><h2>Sub-Header</h2><p>Sub-Paragraph</p></div>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";

        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("Sub-Header", nodeList.get(0).select("h2").text());
        Assert.assertEquals("Sub-Paragraph", nodeList.get(0).select("p").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Header 2</h2><p>Paragraph 2</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Header 2", doc.select("h2").text());
        Assert.assertEquals("Paragraph 2", doc.select("p").text());
    }

    @After
    public void tearDown() {
        parser = null;
    }
}