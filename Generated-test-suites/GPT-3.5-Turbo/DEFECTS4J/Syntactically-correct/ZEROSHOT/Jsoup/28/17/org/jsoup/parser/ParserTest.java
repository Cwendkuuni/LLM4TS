import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Test case 1
        String html1 = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri1 = "https://example.com";
        Parser parser1 = new Parser(new HtmlTreeBuilder());
        Document document1 = parser1.parseInput(html1, baseUri1);
        Assert.assertEquals("Hello World", document1.select("h1").text());

        // Test case 2
        String html2 = "<html><body><h1>Test</h1></body></html>";
        String baseUri2 = "https://example.com";
        Parser parser2 = new Parser(new HtmlTreeBuilder());
        Document document2 = parser2.parseInput(html2, baseUri2);
        Assert.assertEquals("Test", document2.select("h1").text());
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
        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = Parser.parse(html, baseUri);
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Test</p></div>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("p", nodes.get(0).nodeName());
        Assert.assertEquals("Test", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Test", document.body().select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Deprecated method, should be the same as parse()
        String bodyHtml = "<div><p>Test</p></div>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertEquals("Test", document.body().select("p").text());
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