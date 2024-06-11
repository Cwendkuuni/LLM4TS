import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import java.util.List;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput_typical() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testParseInput_emptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("", document.toString());
    }

    @Test
    public void testParseInput_emptyBaseUri() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "";
        Document document = parser.parseInput(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testGetTreeBuilder_typical() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        Assert.assertNotNull(treeBuilder);
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder_typical() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);

        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors_false() {
        boolean isTrackErrors = parser.isTrackErrors();

        Assert.assertFalse(isTrackErrors);
    }

    @Test
    public void testSetTrackErrors_typical() {
        parser.setTrackErrors(5);

        boolean isTrackErrors = parser.isTrackErrors();

        Assert.assertTrue(isTrackErrors);
    }

    @Test
    public void testGetErrors_typical() {
        List<ParseError> errors = parser.getErrors();

        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testSettings_typical() {
        ParseSettings settings = new ParseSettings(true, true);
        parser.settings(settings);

        ParseSettings currentSettings = parser.settings();

        Assert.assertEquals(settings, currentSettings);
    }

    @Test
    public void testParse_typical() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testParseFragment_typical() {
        String fragmentHtml = "<h1>Hello World!</h1>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
    }

    @Test
    public void testParseXmlFragment_typical() {
        String fragmentXml = "<root>Hello World!</root>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
    }

    @Test
    public void testParseBodyFragment_typical() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testUnescapeEntities_typical() {
        String escapedString = "&lt;h1&gt;Hello World!&lt;/h1&gt;";
        boolean inAttribute = false;
        String unescapedString = Parser.unescapeEntities(escapedString, inAttribute);

        Assert.assertEquals("<h1>Hello World!</h1>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed_typical() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }
}