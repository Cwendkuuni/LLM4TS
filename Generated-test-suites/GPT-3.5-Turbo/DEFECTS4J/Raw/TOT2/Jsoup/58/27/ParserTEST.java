package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;
import org.junit.Assert;
import java.util.List;

public class ParserTest {

    @Test
    public void testParserConstructor() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Document document = parser.parseInput("<html></html>", "http://example.com");
        Assert.assertNotNull(document);
    }

    @Test
    public void testParserSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(treeBuilder);
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testParserSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html></html>", "http://example.com");
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = Parser.parseFragment("<div></div>", new Element("div"), "http://example.com");
        Assert.assertNotNull(nodes);
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<root></root>", "http://example.com");
        Assert.assertNotNull(nodes);
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<div></div>", "http://example.com");
        Assert.assertNotNull(document);
    }

    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;div&gt;", true);
        Assert.assertEquals("<div>", unescaped);
    }

    @Test
    public void testDeprecatedMethod() {
        Document document = Parser.parseBodyFragmentRelaxed("<html></html>", "http://example.com");
        Assert.assertNotNull(document);
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}