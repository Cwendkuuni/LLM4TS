
package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParseInput() {
        // Given
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://www.example.com";

        // When
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        // Then
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("Hello World!", doc.body().select("h1").first().text());
    }

    @Test
    public void testGetTreeBuilder() {
        // Given
        Parser parser = new Parser(new HtmlTreeBuilder());

        // When
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        // Then
        Assert.assertNotNull(treeBuilder);
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        // Given
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();

        // When
        Parser updatedParser = parser.setTreeBuilder(treeBuilder);

        // Then
        Assert.assertEquals(treeBuilder, updatedParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        // Given
        Parser parser = new Parser(new HtmlTreeBuilder());

        // When
        boolean trackErrors = parser.isTrackErrors();

        // Then
        Assert.assertFalse(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        // Given
        Parser parser = new Parser(new HtmlTreeBuilder());

        // When
        Parser updatedParser = parser.setTrackErrors(5);

        // Then
        Assert.assertTrue(updatedParser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        // Given
        Parser parser = new Parser(new HtmlTreeBuilder());

        // When
        List<ParseError> errors = parser.getErrors();

        // Then
        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testParse() {
        // Given
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://www.example.com";

        // When
        Document doc = Parser.parse(html, baseUri);

        // Then
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("Hello World!", doc.body().select("h1").first().text());
    }

    @Test
    public void testParseFragment() {
        // Given
        String fragmentHtml = "<div><p>Test Fragment</p></div>";
        Element context = new Element("body");
        String baseUri = "http://www.example.com";

        // When
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        // Then
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("div", nodes.get(0).nodeName());
        Assert.assertEquals("Test Fragment", ((Element) nodes.get(0)).select("p").first().text());
    }

    @Test
    public void testParseBodyFragment() {
        // Given
        String bodyHtml = "<p>Test Body Fragment</p>";
        String baseUri = "http://www.example.com";

        // When
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Then
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("Test Body Fragment", doc.body().select("p").first().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Given
        String bodyHtml = "<p>Test Body Fragment</p>";
        String baseUri = "http://www.example.com";

        // When
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        // Then
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("Test Body Fragment", doc.body().select("p").first().text());
    }

    @Test
    public void testHtmlParser() {
        // When
        Parser parser = Parser.htmlParser();

        // Then
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        // When
        Parser parser = Parser.xmlParser();

        // Then
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}
