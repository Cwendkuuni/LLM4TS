import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {
    private static Parser parser;
    
    @BeforeClass
    public static void setUp() {
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput() {
        // Test with valid input
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("Hello World", h1.text());

        // Test with null input
        assertNull(parser.parseInput(null, baseUri));

        // Test with empty input
        assertNull(parser.parseInput("", baseUri));
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new TreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        // Test with track errors enabled
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());

        // Test with track errors disabled
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        // Test with invalid maxErrors value
        parser.setTrackErrors(-1);
        assertFalse(parser.isTrackErrors());

        // Test with valid maxErrors value
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        // Test with no errors
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.isEmpty());

        // Test with errors
        parser.setTrackErrors(3);
        parser.parseInput("<html><body><h1>Hello World</h2></body></html>", "http://example.com");
        errors = parser.getErrors();
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
        assertEquals(3, errors.size());
    }

    @Test
    public void testParse() {
        // Test with valid input
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("Hello World", h1.text());

        // Test with null input
        assertNull(Parser.parse(null, baseUri));

        // Test with empty input
        assertNull(Parser.parse("", baseUri));
    }

    @Test
    public void testParseFragment() {
        // Test with valid input
        String fragmentHtml = "<h1>Hello World</h1>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element h1 = (Element) nodes.get(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World", h1.text());

        // Test with null input
        assertNull(Parser.parseFragment(null, context, baseUri));

        // Test with empty input
        assertNull(Parser.parseFragment("", context, baseUri));
    }

    @Test
    public void testParseBodyFragment() {
        // Test with valid input
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("Hello World", h1.text());

        // Test with null input
        assertNull(Parser.parseBodyFragment(null, baseUri));

        // Test with empty input
        assertNull(Parser.parseBodyFragment("", baseUri));
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