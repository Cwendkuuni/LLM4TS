
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = parser.parseInput(html, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
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
        Parser result = parser.setTreeBuilder(treeBuilder);
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
        Assert.assertEquals(parser, result);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        boolean trackErrors = parser.isTrackErrors();
        Assert.assertFalse(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        int maxErrors = 10;
        Parser result = parser.setTrackErrors(maxErrors);
        Assert.assertTrue(parser.isTrackErrors());
        Assert.assertEquals(maxErrors, parser.maxErrors);
        Assert.assertEquals(parser, result);
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(false, false);
        Parser result = parser.settings(settings);
        Assert.assertEquals(settings, parser.settings);
        Assert.assertEquals(parser, result);
    }

    @Test
    public void testStaticParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = Parser.parse(html, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Test</p></div>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<div><p>Test</p></div>";
        String baseUri = "https://example.com";
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(bodyHtml, document.body().html());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;p&gt;Test&lt;/p&gt;";
        boolean inAttribute = false;
        String result = Parser.unescapeEntities(string, inAttribute);
        Assert.assertEquals("<p>Test</p>", result);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(bodyHtml, document.html());
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
