package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;

import java.util.List;

public class ParserTest {

    //Test parseInput method with valid input
    @Test
    public void testParseInputWithValidInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "");
        Assert.assertEquals(1, document.select("h1").size());
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    //Test getTreeBuilder method
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
    }

    //Test setTreeBuilder method
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    //Test isTrackErrors method
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        boolean trackErrors = parser.isTrackErrors();
        Assert.assertFalse(trackErrors);
    }

    //Test setTrackErrors method
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);
        Assert.assertTrue(parser.isTrackErrors());
        Assert.assertEquals(10, parser.getErrors().size());
    }

    //Test settings method
    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings parseSettings = new ParseSettings(true);
        parser.settings(parseSettings);
        Assert.assertEquals(parseSettings, parser.settings());
    }

    //Test parse method with valid input
    @Test
    public void testParseWithValidInput() {
        Document document = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "");
        Assert.assertEquals(1, document.select("h1").size());
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    //Test parseFragment method with valid input
    @Test
    public void testParseFragmentWithValidInput() {
        Element context = new Element("div");
        List<Node> nodeList = Parser.parseFragment("<h1>Hello World</h1>", context, "");
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("Hello World", nodeList.get(0).toString());
    }

    //Test parseXmlFragment method with valid input
    @Test
    public void testParseXmlFragmentWithValidInput() {
        List<Node> nodeList = Parser.parseXmlFragment("<h1>Hello World</h1>", "");
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("Hello World", nodeList.get(0).toString());
    }

    //Test parseBodyFragment method with valid input
    @Test
    public void testParseBodyFragmentWithValidInput() {
        Document document = Parser.parseBodyFragment("<h1>Hello World</h1>", "");
        Assert.assertEquals(1, document.select("h1").size());
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    //Test unescapeEntities method with valid input
    @Test
    public void testUnescapeEntitiesWithValidInput() {
        String unescapedString = Parser.unescapeEntities("&amp;&lt;&gt;&quot;&apos;", true);
        Assert.assertEquals("&<>\"'", unescapedString);
    }

    //Test parseBodyFragmentRelaxed method with valid input
    @Test
    public void testParseBodyFragmentRelaxedWithValidInput() {
        Document document = Parser.parseBodyFragmentRelaxed("<html><body><h1>Hello World</h1></body></html>", "");
        Assert.assertEquals(1, document.select("h1").size());
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    //Test htmlParser method
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    //Test xmlParser method
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

}