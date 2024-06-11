import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.HtmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        Document document = parser.parseInput(html, baseUri);

        Assert.assertEquals(html, document.outerHtml());
        Assert.assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        TreeBuilder treeBuilder = parser.getTreeBuilder();

        Assert.assertNotNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();

        Parser updatedParser = parser.setTreeBuilder(newTreeBuilder);

        Assert.assertEquals(newTreeBuilder, updatedParser.getTreeBuilder());
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

        Parser updatedParser = parser.setTrackErrors(5);

        Assert.assertTrue(updatedParser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();

        Assert.assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings newSettings = new ParseSettings(true, true);

        Parser updatedParser = parser.settings(newSettings);

        Assert.assertEquals(newSettings, updatedParser.settings());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals(html, document.outerHtml());
        Assert.assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello, World!</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals(fragmentHtml, context.html());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>";
        String baseUri = "http://example.com";

        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);

        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals(fragmentXml, nodeList.get(0).outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals(bodyHtml, document.body().html());
        Assert.assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;p&gt;Hello, World!&lt;/p&gt;";
        boolean inAttribute = false;

        String unescapedString = Parser.unescapeEntities(escapedString, inAttribute);

        Assert.assertEquals("<p>Hello, World!</p>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertEquals(bodyHtml, document.outerHtml());
        Assert.assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();

        Assert.assertNotNull(parser.getTreeBuilder());
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();

        Assert.assertNotNull(parser.getTreeBuilder());
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}