import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new TreeBuilderStub());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        Document document = parser.parseInput(html, "");
        assertNotNull("Parsed document should not be null", document);
        assertEquals("Parsed document should have correct HTML", html, document.html());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull("TreeBuilder should not be null", treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilderStub();
        parser.setTreeBuilder(treeBuilder);
        assertEquals("TreeBuilder should be set", treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse("Track errors should be disabled by default", parser.isTrackErrors());
        parser.setTrackErrors(10);
        assertTrue("Track errors should be enabled when maxErrors is greater than 0", parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse("Track errors should be disabled when maxErrors is 0", parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        assertEquals("Track errors should be set to the specified maxErrors", 5, parser.getTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull("Parse errors should not be null", errors);
        assertEquals("Parse errors should be empty", 0, errors.size());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = parser.settings();
        assertNotNull("Settings should not be null", settings);
    }

    @Test
    public void testSetSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        parser.settings(settings);
        assertEquals("Settings should be set", settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        Document document = Parser.parse(html, "");
        assertNotNull("Parsed document should not be null", document);
        assertEquals("Parsed document should have correct HTML", html, document.html());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello, World!</p>";
        Element context = new Element("div");
        String baseUri = "";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals("Parsed fragment should have correct number of nodes", 1, nodes.size());
        assertTrue("Parsed fragment should contain p tag", nodes.get(0) instanceof Element);
        assertEquals("Parsed fragment should have correct HTML", fragmentHtml, nodes.get(0).outerHtml());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<node>Hello, World!</node>";
        String baseUri = "";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertEquals("Parsed XML fragment should have correct number of nodes", 1, nodes.size());
        assertTrue("Parsed XML fragment should contain node tag", nodes.get(0) instanceof Element);
        assertEquals("Parsed XML fragment should have correct HTML", fragmentXml, nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull("Parsed document should not be null", document);
        assertEquals("Parsed document should have correct HTML", "<html><head></head><body>" + bodyHtml + "</body></html>", document.html());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;p&gt;Hello, World!&lt;/p&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, false);
        assertEquals("Unescaped string should be correct", "<p>Hello, World!</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull("Parsed document should not be null", document);
        assertEquals("Parsed document should have correct HTML", bodyHtml, document.html());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertNotNull("HTML parser should not be null", htmlParser);
        assertTrue("HTML parser should use HtmlTreeBuilder", htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertNotNull("XML parser should not be null", xmlParser);
        assertTrue("XML parser should use XmlTreeBuilder", xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

    private class TreeBuilderStub implements TreeBuilder {
        @Override
        public Document parse(String html, String baseUri, ParseErrorList errors, ParseSettings settings) {
            return new Document("");
        }

        @Override
        public List<Node> parseFragment(String fragmentHtml, Element context, String baseUri, ParseErrorList errors, ParseSettings settings) {
            return null;
        }

        @Override
        public ParseSettings defaultSettings() {
            return null;
        }
    }
}