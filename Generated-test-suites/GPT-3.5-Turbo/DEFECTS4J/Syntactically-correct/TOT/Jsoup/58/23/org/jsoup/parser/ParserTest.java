import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParserTest {
    private Parser parser;
    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";

        Document document = parser.parseInput(html, baseUri);

        assertEquals("https://example.com", document.baseUri());
        assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder returnedTreeBuilder = parser.getTreeBuilder();

        assertEquals(treeBuilder, returnedTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();

        parser.setTreeBuilder(newTreeBuilder);

        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);

        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getErrors().size());
    }

    @Test
    public void testSetTrackErrorsWithZeroMaxErrors() {
        parser.setTrackErrors(0);

        assertFalse(parser.isTrackErrors());
        assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        parser.settings(settings);

        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        assertEquals("https://example.com", document.baseUri());
        assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        Element contextElement = new Element("div");
        String baseUri = "https://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, contextElement, baseUri);

        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals("Paragraph 1", nodes.get(0).text());
    }

    @Test
    public void testParseFragmentWithNullContext() {
        String fragmentHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        String baseUri = "https://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, null, baseUri);

        assertEquals(1, nodes.size());
        assertEquals("html", nodes.get(0).nodeName());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><item>Item 1</item><item>Item 2</item></root>";
        String baseUri = "https://example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertEquals(1, nodes.size());
        assertEquals("item", nodes.get(0).nodeName());
        assertEquals("Item 1", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("https://example.com", document.baseUri());
        assertEquals("Paragraph 1", document.select("p").get(0).text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;p&gt;Paragraph&lt;/p&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, false);

        assertEquals("<p>Paragraph</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://example.com";

        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertEquals("https://example.com", document.baseUri());
        assertEquals("Paragraph 1", document.select("p").get(0).text());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}