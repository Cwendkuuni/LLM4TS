import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new TreeBuilder());
    }

    // Test for parseInput method

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        Element body = doc.body();
        assertNotNull(body);
        assertEquals(1, body.children().size());
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Hello World!", h1.text());
    }

    // Tests for getter and setter methods

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser newParser = parser.setTreeBuilder(treeBuilder);
        assertNotNull(newParser);
        assertEquals(treeBuilder, newParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser newParser = parser.setTrackErrors(5);
        assertNotNull(newParser);
        assertTrue(newParser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    // Tests for static parse functions

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        Element body = doc.body();
        assertNotNull(body);
        assertEquals(1, body.children().size());
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Hello World!", h1.text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Test</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element div = (Element) nodes.get(0);
        assertEquals("div", div.tagName());
        assertEquals(1, div.children().size());
        Element p = div.getElementsByTag("p").first();
        assertNotNull(p);
        assertEquals("Test", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        assertNotNull(body);
        assertEquals(1, body.children().size());
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Hello World!", h1.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        assertNotNull(body);
        assertEquals(1, body.children().size());
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Hello World!", h1.text());
    }

    // Tests for builders

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