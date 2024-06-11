import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    private static Parser parser;

    @BeforeClass
    public static void setUp() {
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = parser.parseInput(html, baseUri);

        Assert.assertEquals("https://example.com", document.baseUri());
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(TreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
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
        parser.setTrackErrors(3);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false, false);
        parser.settings(settings);
        Assert.assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("https://example.com", document.baseUri());
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World!</h1>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("Hello World!", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<h1>Hello World!</h1>";
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("Hello World!", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals("https://example.com", document.baseUri());
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "Hello &lt;World&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, false);

        Assert.assertEquals("Hello <World>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertEquals("https://example.com", document.baseUri());
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        Assert.assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        Assert.assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}