import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput_typical() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseInput_emptyHtml() {
        String html = "";
        String baseUri = "https://example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals("", doc.text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser updatedParser = parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, updatedParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser updatedParser = parser.setTrackErrors(10);
        assertTrue(updatedParser.isTrackErrors());
    }

    @Test
    public void testGetErrors_withNoErrors() {
        assertTrue(parser.getErrors().isEmpty());
    }

    @Test
    public void testParse_typical() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseFragment_typical() {
        String fragmentHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
        Element div = (Element) nodeList.get(0);
        assertEquals(2, div.select("p").size());
        assertEquals("Paragraph 1", div.selectFirst("p").text());
    }

    @Test
    public void testParseBodyFragment_typical() {
        String bodyHtml = "<h1>Hello World</h1><p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals("Hello World", doc.selectFirst("h1").text());
        assertEquals(2, doc.select("p").size());
        assertEquals("Paragraph 1", doc.select("p").get(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed_typical() {
        String bodyHtml = "<h1>Hello World</h1><p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals("Hello World", doc.selectFirst("h1").text());
        assertEquals(2, doc.select("p").size());
        assertEquals("Paragraph 1", doc.select("p").get(0).text());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertNotNull(htmlParser);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertNotNull(xmlParser);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}