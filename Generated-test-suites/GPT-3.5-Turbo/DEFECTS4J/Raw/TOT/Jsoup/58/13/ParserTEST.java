import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput_typicalCase() {
        // typical case - html and baseUri provided
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, baseUri);
        Assert.assertEquals("Title", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseInput_emptyHtml() {
        // edge case - empty html provided
        String html = "";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, baseUri);
        Assert.assertEquals("", document.html());
    }

    @Test
    public void testParseInput_emptyBaseUri() {
        // edge case - empty baseUri provided
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, baseUri);
        Assert.assertEquals("Title", document.getElementsByTag("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        // typical case - treeBuilder set and retrieved
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        // typical case - treeBuilder set and retrieved
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        TreeBuilder newTreeBuilder = parser.getTreeBuilder();
        Assert.assertSame(treeBuilder, newTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        // typical case - maxErrors greater than 0
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);
        boolean trackErrors = parser.isTrackErrors();
        Assert.assertTrue(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        // typical case - maxErrors set and retrieved
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        int maxErrors = parser.getMaxErrors();
        Assert.assertEquals(5, maxErrors);
    }

    @Test
    public void testGetErrors() {
        // typical case - errors retrieved after parsing
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        // typical case - settings set and retrieved
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(false, false);
        parser.settings(settings);
        ParseSettings newSettings = parser.settings();
        Assert.assertEquals(settings, newSettings);
    }

    @Test
    public void testParse_HTML() {
        // typical case - HTML parsed into Document
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = Parser.parse(html, baseUri);
        Assert.assertEquals("Title", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseFragment() {
        // typical case - HTML fragment parsed into list of nodes
        String fragmentHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        Element context = new Element(Tag.valueOf("body"), "");
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertEquals(3, nodes.size());
    }

    @Test
    public void testParseFragment_withErrorList() {
        // typical case - HTML fragment parsed into list of nodes with error list
        String fragmentHtml = "<div><p>Paragraph 1<p>Paragraph 2</p></div>";
        Element context = new Element(Tag.valueOf("body"), "");
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertEquals(3, nodes.size());
    }

    @Test
    public void testParseXmlFragment() {
        // typical case - XML fragment parsed into list of nodes
        String fragmentXml = "<root><child>Text</child></root>";
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        Assert.assertEquals(1, nodes.size());
    }

    @Test
    public void testParseBodyFragment() {
        // typical case - HTML fragment parsed into Document body
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Paragraph 1Paragraph 2", document.body().html());
    }

    @Test
    public void testUnescapeEntities() {
        // typical case - HTML entities unescaped from string
        String string = "&lt;p&gt;Paragraph&lt;/p&gt;";
        boolean inAttribute = false;
        String unescapedString = Parser.unescapeEntities(string, inAttribute);
        Assert.assertEquals("<p>Paragraph</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // deprecated case - HTML parsed into Document (same as parse)
        String bodyHtml = "<body><h1>Title</h1></body>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertEquals("Title", document.getElementsByTag("h1").text());
    }

    @Test
    public void testHtmlParser() {
        // typical case - new HTML parser created
        Parser parser = Parser.htmlParser();
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        // typical case - new XML parser created
        Parser parser = Parser.xmlParser();
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}