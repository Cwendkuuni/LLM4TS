import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://example.com";
        
        Document document = parser.parseInput(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>\n   Hello, World!\n  </h1>\n </body>\n</html>", document.html());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        Assert.assertNotNull(treeBuilder);
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();

        Parser returnedParser = parser.setTreeBuilder(treeBuilder);

        Assert.assertNotNull(returnedParser);
        Assert.assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();

        Assert.assertFalse(trackErrors);

        parser.setTrackErrors(10);

        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser returnedParser = parser.setTrackErrors(5);

        Assert.assertNotNull(returnedParser);
        Assert.assertTrue(parser.isTrackErrors());
        Assert.assertEquals(5, parser.getErrors().size());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();

        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.isEmpty());

        parser.setTrackErrors(3);
        errors = parser.getErrors();

        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = parser.settings();

        Assert.assertNotNull(settings);

        ParseSettings newSettings = new ParseSettings(true, true);
        Parser returnedParser = parser.settings(newSettings);

        Assert.assertNotNull(returnedParser);
        Assert.assertSame(newSettings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://example.com";
        
        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>\n   Hello, World!\n  </h1>\n </body>\n</html>", document.html());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello, World!</h1>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("<h1>\n Hello, World!\n</h1>", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><item>1</item><item>2</item><item>3</item></root>";
        String baseUri = "https://example.com";
        
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("<root> <item>\n 1\n</item> <item>\n 2\n</item> <item>\n 3\n</item> </root>", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>\n   Hello, World!\n  </h1>\n </body>\n</html>", document.html());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;h1&gt;Hello, World!&lt;/h1&gt;";
        
        String unescapedString = Parser.unescapeEntities(escapedString, false);
        Assert.assertEquals("<h1>Hello, World!</h1>", unescapedString);
    }

    @Test
    @Deprecated
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>\n   Hello, World!\n  </h1>\n </body>\n</html>", document.html());
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