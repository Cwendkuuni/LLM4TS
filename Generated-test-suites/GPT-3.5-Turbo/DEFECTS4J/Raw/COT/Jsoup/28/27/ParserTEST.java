import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
        parser.setTrackErrors(1);
        errors = parser.getErrors();
        assertNotNull(errors);
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello, World!</div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("Hello, World!", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.select("h1").text());
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