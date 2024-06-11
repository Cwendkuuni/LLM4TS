import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Typical use case
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        Document document = parser.parseInput(html, baseUri);
        
        Assert.assertEquals("Test", document.head().selectFirst("title").text());
        Assert.assertEquals("Hello World", document.body().selectFirst("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        // Typical use case
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        Assert.assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        // Typical use case
        Parser parser = new Parser(new HtmlTreeBuilder());
        HtmlTreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        
        parser.setTreeBuilder(htmlTreeBuilder);
        
        Assert.assertEquals(htmlTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        // Typical use case
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        boolean trackErrors = parser.isTrackErrors();
        
        Assert.assertFalse(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        // Typical use case
        Parser parser = new Parser(new HtmlTreeBuilder());
        int maxErrors = 5;
        
        parser.setTrackErrors(maxErrors);
        
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        // Error scenario
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        
        parser.parseInput("<tag", "https://example.com"); // Invalid input
        
        List<ParseError> errors = parser.getErrors();
        
        Assert.assertEquals(1, errors.size());
    }

    @Test
    public void testSettings() {
        // Typical use case
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings newSettings = new ParseSettings(true);
        
        parser.settings(newSettings);
        
        Assert.assertEquals(newSettings, parser.settings());
    }

    @Test
    public void testParse() {
        // Typical use case
        String html = "<div><p>Hello World</p></div>";
        String baseUri = "https://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Hello World", document.selectFirst("p").text());
    }

    @Test
    public void testParseFragment() {
        // Typical use case
        String fragmentHtml = "<p>Hello World</p>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("Hello World", nodeList.get(0).toString());
    }

    @Test
    public void testParseXmlFragment() {
        // Typical use case
        String fragmentXml = "<root><child>Hello World</child></root>";
        String baseUri = "https://example.com";
        
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("Hello World", nodeList.get(0).toString());
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "https://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertEquals("Hello World", document.body().selectFirst("p").text());
    }

    @Test
    public void testUnescapeEntities() {
        // Typical use case
        String escapedString = "&lt;p&gt;Hello World&lt;/p&gt;";
        boolean inAttribute = false;
        
        String unescapedString = Parser.unescapeEntities(escapedString, inAttribute);
        
        Assert.assertEquals("<p>Hello World</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Deprecated method, testing with same parameters as `parse`
        String bodyHtml = "<div><p>Hello World</p></div>";
        String baseUri = "https://example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertEquals("Hello World", document.selectFirst("p").text());
    }

    @Test
    public void testHtmlParser() {
        // Typical use case
        Parser parser = Parser.htmlParser();
        
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        // Typical use case
        Parser parser = Parser.xmlParser();
        
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}