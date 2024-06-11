import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello, World!", document.body().select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        TreeBuilder newTreeBuilder = new TreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Assert.assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(10);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        ParseSettings settings = new ParseSettings(false, true);
        parser.settings(settings);
        Assert.assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello, World!", document.body().select("h1").text());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello, World!</div>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("div", nodes.get(0).nodeName());
        Assert.assertEquals("Hello, World!", nodes.get(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><tag>Hello, World!</tag></root>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("root", nodes.get(0).nodeName());
        Assert.assertEquals("Hello, World!", nodes.get(0).select("tag").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello, World!</div>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Hello, World!", document.body().select("div").text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "Hello &amp; World!";
        boolean inAttribute = false;
        String unescapedString = Parser.unescapeEntities(escapedString, inAttribute);
        Assert.assertEquals("Hello & World!", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Hello, World!</div>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertEquals("Hello, World!", document.body().select("div").text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}