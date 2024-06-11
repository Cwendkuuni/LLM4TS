import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilderMock());
    }

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://www.example.com/";
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, World!", doc.body().select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof TreeBuilderMock);
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
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        int maxErrors = 10;
        Parser newParser = parser.setTrackErrors(maxErrors);
        assertNotNull(newParser);
        assertEquals(maxErrors, newParser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParse() {
        String html = "<div>Hello, World!</div>";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello, World!</p>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com/";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Hello, World!", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
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

    private static class TreeBuilderMock implements TreeBuilder {
        @Override
        public Document parse(String html, String baseUri, ParseErrorList errors) {
            return new Document("");
        }

        @Override
        public List<Node> parseFragment(String fragmentHtml, Element context, String baseUri, ParseErrorList errors) {
            return null;
        }
    }
}