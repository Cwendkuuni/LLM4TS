import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput_typicalCase() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseInput_emptyHtml() {
        String html = "";
        String baseUri = "http://example.com";

        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseInput_errorTracking() {
        String html = "<html><body><h1>Title</h1></body>";
        String baseUri = "http://example.com";
        parser.setTrackErrors(10);

        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Unclosed tag", errors.get(0).getErrorMsg());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser updatedParser = parser.setTreeBuilder(treeBuilder);

        assertNotNull(updatedParser);
        assertEquals(treeBuilder, updatedParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors_enabled() {
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testIsTrackErrors_disabled() {
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser updatedParser = parser.setTrackErrors(5);

        assertNotNull(updatedParser);
        assertTrue(updatedParser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
    }

    @Test
    public void testParse_typicalCase() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParse_emptyHtml() {
        String html = "";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseFragment_typicalCase() {
        String fragmentHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", ((Element) nodes.get(0)).tagName());
    }

    @Test
    public void testParseFragment_emptyFragmentHtml() {
        String fragmentHtml = "";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testParseBodyFragment_typicalCase() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertTrue(doc.getElementsByTag("body").isEmpty());
        assertEquals(bodyHtml, doc.body().html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseBodyFragment_emptyBodyHtml() {
        String bodyHtml = "";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertTrue(doc.getElementsByTag("body").isEmpty());
        assertTrue(doc.body().children().isEmpty());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseBodyFragmentRelaxed_typicalCase() {
        String bodyHtml = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals(bodyHtml, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseBodyFragmentRelaxed_emptyBodyHtml() {
        String bodyHtml = "";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals(bodyHtml, doc.html());
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