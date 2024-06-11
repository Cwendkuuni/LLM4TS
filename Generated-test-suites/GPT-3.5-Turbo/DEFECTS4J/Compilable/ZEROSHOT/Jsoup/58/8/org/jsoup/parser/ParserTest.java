package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import java.util.List;
import java.util.ArrayList;

public class ParserTest {
    private Parser parser;
    private TreeBuilder treeBuilder;
    private ParseSettings settings;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        settings = treeBuilder.defaultSettings();
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = parser.parseInput(html, baseUri);
        assertNotNull(document);
        assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        assertEquals(treeBuilder, parser.getTreeBuilder());
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
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getErrors().size());
    }

    @Test
    public void testGetErrors() {
        parser.parseInput("<html><body><h1>Hello World!</h1></body></html>", "https://www.example.com");
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        ParseSettings newSettings = new ParseSettings(true, true);
        parser.settings(newSettings);
        assertEquals(newSettings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        assertNotNull(document);
        assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World!</h1>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Hello World!", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Hello World!", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "Hello &lt;World&gt;! &amp;";
        String unescapedString = Parser.unescapeEntities(escapedString, true);
        assertEquals("Hello <World>! &", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.select("h1").text());
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