
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
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
        assertEquals(baseUri, doc.baseUri());
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
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
        assertEquals(10, parser.getTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello</div><span>World</span>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertEquals("<div>Hello</div>", nodes.get(0).toString());
        assertEquals("<span>World</span>", nodes.get(1).toString());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
        assertEquals(baseUri, doc.baseUri());
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
