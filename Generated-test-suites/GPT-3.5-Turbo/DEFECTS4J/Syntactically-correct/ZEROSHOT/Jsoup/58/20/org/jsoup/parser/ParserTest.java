import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        Document document = parser.parseInput("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>", "http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder1 = new TreeBuilder();
        TreeBuilder treeBuilder2 = new TreeBuilder();
        Parser parser = new Parser(treeBuilder1);
        parser.setTreeBuilder(treeBuilder2);
        Assert.assertEquals(treeBuilder2, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(10);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        Parser parser = new Parser(new TreeBuilder());
        parser.settings(settings);
        Assert.assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>", "http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Test</p>", context, "http://example.com");
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("p", nodes.get(0).nodeName());
        Assert.assertEquals("Test", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<p>Test</p>", "http://example.com");
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("p", nodes.get(0).nodeName());
        Assert.assertEquals("Test", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<h1>Hello World!</h1>", "http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void testUnescapeEntities() {
        String unescapedString = Parser.unescapeEntities("&lt;div&gt;Test&lt;/div&gt;", true);
        Assert.assertEquals("<div>Test</div>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<h1>Hello World!</h1>", "http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Assert.assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        Assert.assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}