import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInput() {
        // Typical use case
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = parser.parseInput(html, baseUri);
        // Verify the parsed document
        Assert.assertEquals("Hello World!", doc.body().child(0).text());

        // Edge case - empty HTML
        html = "";
        baseUri = "https://www.example.com";
        doc = parser.parseInput(html, baseUri);
        // Verify the parsed document
        Assert.assertEquals(0, doc.body().children().size());

        // Error scenario - invalid HTML
        html = "<html><body><h1>Hello World!</h2>";
        baseUri = "https://www.example.com";
        doc = parser.parseInput(html, baseUri);
        // Verify the parse errors
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected closing tag (h2). Expected closing tag (h1).", errors.get(0).getMessage());
    }

    @Test
    public void testGetTreeBuilder() {
        // Typical use case
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        // Verify the tree builder
        Assert.assertNotNull(treeBuilder);

        // Edge case - set custom tree builder
        TreeBuilder customTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(customTreeBuilder);
        treeBuilder = parser.getTreeBuilder();
        // Verify the custom tree builder
        Assert.assertEquals(customTreeBuilder, treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        // Typical use case
        Assert.assertFalse(parser.isTrackErrors());

        // Edge case - set maximum errors greater than 0
        parser.setTrackErrors(5);
        Assert.assertTrue(parser.isTrackErrors());

        // Edge case - set maximum errors to 0
        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        // Typical use case - no errors
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());

        // Typical use case - with errors
        String html = "<html><body><h1>Hello World!</h2>";
        String baseUri = "https://www.example.com";
        parser.parseInput(html, baseUri);
        errors = parser.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected closing tag (h2). Expected closing tag (h1).", errors.get(0).getMessage());
    }

    @Test
    public void testParse() {
        // Typical use case
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        // Verify the parsed document
        Assert.assertEquals("Hello World!", doc.body().child(0).text());

        // Edge case - empty HTML
        html = "";
        baseUri = "https://www.example.com";
        doc = Parser.parse(html, baseUri);
        // Verify the parsed document
        Assert.assertEquals(0, doc.body().children().size());
    }

    @Test
    public void testParseFragment() {
        // Typical use case
        String fragmentHtml = "<h1>Hello World!</h1>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        // Verify the parsed nodes
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("h1", nodes.get(0).nodeName());

        // Edge case - empty fragment HTML
        fragmentHtml = "";
        context = new Element("div");
        baseUri = "https://www.example.com";
        nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        // Verify the parsed nodes
        Assert.assertEquals(0, nodes.size());
        Assert.assertEquals(0, context.children().size());

        // Error scenario - invalid fragment HTML
        fragmentHtml = "<h1>Hello World!</h2>";
        context = new Element("div");
        baseUri = "https://www.example.com";
        nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        // Verify the parse errors
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected closing tag (h2). Expected closing tag (h1).", errors.get(0).getMessage());
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        // Verify the parsed document
        Assert.assertEquals("Hello World!", doc.body().child(0).text());

        // Edge case - empty body HTML
        bodyHtml = "";
        baseUri = "https://www.example.com";
        doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        // Verify the parsed document
        Assert.assertEquals(0, doc.body().children().size());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Typical use case
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        // Verify the parsed document
        Assert.assertEquals("Hello World!", doc.body().child(0).text());

        // Edge case - empty body HTML
        bodyHtml = "";
        baseUri = "https://www.example.com";
        doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        // Verify the parsed document
        Assert.assertEquals(0, doc.body().children().size());
    }

    @Test
    public void testHtmlParser() {
        // Typical use case
        Parser htmlParser = Parser.htmlParser();
        TreeBuilder treeBuilder = htmlParser.getTreeBuilder();
        // Verify the HTML parser and its default tree builder
        Assert.assertNotNull(htmlParser);
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        // Typical use case
        Parser xmlParser = Parser.xmlParser();
        TreeBuilder treeBuilder = xmlParser.getTreeBuilder();
        // Verify the XML parser and its default tree builder
        Assert.assertNotNull(xmlParser);
        Assert.assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }
}