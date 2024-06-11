import org.junit.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.*;

import java.util.List;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello, world!</h1>\n </body>\n</html>", document.html());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        Assert.assertFalse(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        int maxErrors = 10;
        Parser newParser = parser.setTrackErrors(maxErrors);
        Assert.assertTrue(newParser.isTrackErrors());
        Assert.assertEquals(maxErrors, newParser.getErrors().size());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        Parser newParser = parser.settings(settings);
        Assert.assertEquals(settings, newParser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello, world!</h1>\n </body>\n</html>", document.html());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello, world!</div>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertEquals("<div>Hello, world!</div>", nodes.get(0).toString());
        Assert.assertEquals(context, nodes.get(0).parent());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<div>Hello, world!</div>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        Assert.assertEquals("<div>Hello, world!</div>", nodes.get(0).toString());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, world!</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello, world!</h1>\n </body>\n</html>", document.html());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "Hello &amp; world!";
        String unescapedString = Parser.unescapeEntities(escapedString, false);
        Assert.assertEquals("Hello & world!", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, world!</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello, world!</h1>\n </body>\n</html>", document.html());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        Assert.assertNotNull(htmlParser);
        Assert.assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        Assert.assertNotNull(xmlParser);
        Assert.assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}