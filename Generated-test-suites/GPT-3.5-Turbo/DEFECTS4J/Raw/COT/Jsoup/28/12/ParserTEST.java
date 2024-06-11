import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Heading</h1></body></html>";
        String baseUri = "http://example.com/";
        Document document = parser.parseInput(html, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Heading", document.getElementsByTag("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        TreeBuilder updatedTreeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(treeBuilder, updatedTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        Assert.assertFalse(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        int maxErrors = 10;
        parser.setTrackErrors(maxErrors);
        boolean trackErrors = parser.isTrackErrors();
        Assert.assertTrue(trackErrors);
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
    }

    @Test
    public void testStaticParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Heading</h1></body></html>";
        String baseUri = "http://example.com/";
        Document document = Parser.parse(html, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Heading", document.getElementsByTag("h1").text());
    }

    @Test
    public void testStaticParseFragment() {
        String fragmentHtml = "<h1>Heading</h1>";
        Element context = new Element("div");
        String baseUri = "http://example.com/";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertNotNull(nodes);
        Assert.assertEquals("Heading", nodes.get(0).text());
    }

    @Test
    public void testStaticParseBodyFragment() {
        String bodyHtml = "<h1>Heading</h1>";
        String baseUri = "http://example.com/";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Heading", document.getElementsByTag("h1").text());
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