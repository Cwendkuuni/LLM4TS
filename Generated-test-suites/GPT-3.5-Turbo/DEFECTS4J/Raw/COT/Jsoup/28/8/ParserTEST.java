
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals(html, doc.toString());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder1 = new HtmlTreeBuilder();
        TreeBuilder treeBuilder2 = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder1);

        parser.setTreeBuilder(treeBuilder2);

        Assert.assertEquals(treeBuilder2, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        parser.setTrackErrors(10);

        Assert.assertTrue(parser.isTrackErrors());
        Assert.assertEquals(10, parser.getErrors().size());
    }

    @Test
    public void testGetErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        Assert.assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals(html, doc.toString());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Test</h1>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertNotNull(nodeList);
        Assert.assertEquals(fragmentHtml, nodeList.toString());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.toString());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.toString());
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
