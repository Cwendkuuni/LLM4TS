import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;
    private String html;
    private String baseUri;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        html = "<html><body><h1>Test</h1></body></html>";
        baseUri = "http://example.com";
    }

    @Test
    public void testGetTreeBuilder() {
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
    }

    @Test
    public void testSettings() {
        ParseSettings settings = parser.settings();
        Assert.assertNotNull(settings);
    }

    @Test
    public void testParseInput() {
        Document document = parser.parseInput(html, baseUri);
        Assert.assertNotNull(document);
        Element body = document.body();
        Assert.assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        Assert.assertNotNull(h1);
        Assert.assertEquals("Test", h1.text());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse(html, baseUri);
        Assert.assertNotNull(document);
        Element body = document.body();
        Assert.assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        Assert.assertNotNull(h1);
        Assert.assertEquals("Test", h1.text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Test</p>", context, baseUri);
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        Assert.assertTrue(node instanceof Element);
        Element element = (Element) node;
        Assert.assertEquals("p", element.tagName());
        Assert.assertEquals("Test", element.text());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<node>Test</node>", baseUri);
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        Assert.assertTrue(node instanceof Element);
        Element element = (Element) node;
        Assert.assertEquals("node", element.tagName());
        Assert.assertEquals("Test", element.text());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<h1>Test</h1>", baseUri);
        Assert.assertNotNull(document);
        Element body = document.body();
        Assert.assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        Assert.assertNotNull(h1);
        Assert.assertEquals("Test", h1.text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;script&gt;alert('test');&lt;/script&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, true);
        Assert.assertEquals("<script>alert('test');</script>", unescapedString);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<h1>Test</h1>", baseUri);
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        Assert.assertNotNull(htmlParser);
        Assert.assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        Assert.assertNotNull(xmlParser);
        Assert.assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}