import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private Parser parser;

    @Before
    public void init() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    // Test parseInput method
    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Title", doc.getElementsByTag("h1").first().text());
    }

    // Test getTreeBuilder method
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    // Test setTreeBuilder method
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);

        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    // Test isTrackErrors method
    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(5);

        assertTrue(parser.isTrackErrors());
    }

    // Test setTrackErrors method
    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
        assertEquals(10, parser.getErrors().size());
    }

    // Test getErrors method
    @Test
    public void testGetErrors() {
        parser.setTrackErrors(3);

        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertEquals(3, errors.size());
    }

    // Test parse method
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Title", doc.getElementsByTag("h1").first().text());
    }

    // Test parseFragment method
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p><p>Paragraph 3</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(3, nodes.size());
        assertEquals("Paragraph 1", nodes.get(0).text());
        assertEquals("Paragraph 2", nodes.get(1).text());
        assertEquals("Paragraph 3", nodes.get(2).text());
    }

    // Test parseBodyFragment method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p><p>Paragraph 3</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.body());
        assertEquals(3, doc.getElementsByTag("p").size());
        assertEquals("Paragraph 1", doc.getElementsByTag("p").get(0).text());
        assertEquals("Paragraph 2", doc.getElementsByTag("p").get(1).text());
        assertEquals("Paragraph 3", doc.getElementsByTag("p").get(2).text());
    }

    // Test htmlParser method
    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();

        assertNotNull(htmlParser);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    // Test xmlParser method
    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();

        assertNotNull(xmlParser);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}