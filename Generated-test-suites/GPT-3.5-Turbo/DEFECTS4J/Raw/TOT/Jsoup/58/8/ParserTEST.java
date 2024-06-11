import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;
    private ParseSettings settings;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        settings = treeBuilder.defaultSettings();
    }

    @Test
    public void testParseInput() {
        // Typical use case
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello, World!</h1>\n </body>\n</html>",
                doc.outerHtml());
    }

    @Test
    public void testGetTreeBuilder() {
        // Typical use case
        TreeBuilder currentTreeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(treeBuilder, currentTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        // Typical use case
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        Parser modifiedParser = parser.setTreeBuilder(newTreeBuilder);

        Assert.assertEquals(newTreeBuilder, modifiedParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        // Typical use case
        int maxErrors = 5;
        Parser parserWithErrors = parser.setTrackErrors(maxErrors);

        Assert.assertTrue(parserWithErrors.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        // Typical use case
        int maxErrors = 5;
        Parser modifiedParser = parser.setTrackErrors(maxErrors);

        Assert.assertEquals(maxErrors, modifiedParser.getErrors().size());
    }

    @Test
    public void testGetErrors() {
        // Typical use case
        int maxErrors = 5;
        Parser parserWithErrors = parser.setTrackErrors(maxErrors);

        List<ParseError> errors = parserWithErrors.getErrors();

        Assert.assertEquals(maxErrors, errors.size());
    }

    @Test
    public void testSettings() {
        // Typical use case
        ParseSettings newSettings = new ParseSettings(false);
        Parser modifiedParser = parser.settings(newSettings);

        Assert.assertEquals(newSettings, modifiedParser.settings());
    }

    @Test
    public void testParse() {
        // Typical use case
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello, World!</h1>\n </body>\n</html>",
                doc.outerHtml());
    }

    @Test
    public void testParseFragment() {
        // Typical use case
        String fragmentHtml = "<b>Hello, World!</b>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertEquals("<b>Hello, World!</b>", context.html());
        Assert.assertEquals("http://example.com", nodes.get(0).baseUri());
    }

    @Test
    public void testParseXmlFragment() {
        // Typical use case
        String fragmentXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>hello world</root>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        Assert.assertEquals("http://example.com", nodes.get(0).baseUri());
        Assert.assertEquals("<root>hello world</root>", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case
        String bodyHtml = "<b>Hello, World!</b>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <b>Hello, World!</b>\n </body>\n</html>",
                doc.outerHtml());
    }

    @Test
    public void testUnescapeEntities() {
        // Typical use case
        String escapedString = "&lt;b&gt;Hello, World!&lt;/b&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, false);

        Assert.assertEquals("<b>Hello, World!</b>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Typical use case
        String bodyHtml = "<b>Hello, World!</b>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <b>Hello, World!</b>\n </body>\n</html>",
                doc.outerHtml());
    }

    @Test
    public void testHtmlParser() {
        // Typical use case
        Parser htmlParser = Parser.htmlParser();
        Assert.assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        // Typical use case
        Parser xmlParser= Parser.xmlParser();
        Assert.assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}