
package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    // Test parseInput method
    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://www.example.com/";

        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   Hello, World!\n" +
                "  </h1>\n" +
                " </body>\n" +
                "</html>", doc.html());
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
        TreeBuilder treeBuilder = new XmlTreeBuilder();

        parser.setTreeBuilder(treeBuilder);

        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    // Test isTrackErrors method
    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    // Test setTrackErrors method
    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getErrors().size());
        assertTrue(parser.getErrors().get(0) instanceof ParseError);
    }

    // Test settings method
    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true);

        parser.settings(settings);

        assertEquals(settings, parser.settings());
    }

    // Test parse method
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   Hello, World!\n" +
                "  </h1>\n" +
                " </body>\n" +
                "</html>", doc.html());
    }

    // Test parseFragment method
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello, World!</div>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com/";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", ((Element) nodes.get(0)).tagName());
    }

    // Test parseFragment with errorList parameter method
    @Test
    public void testParseFragmentWithErrors() {
        String fragmentHtml = "<div>Hello, World!</div>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com/";
        ParseErrorList errorList = new ParseErrorList();

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri, errorList);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", ((Element) nodes.get(0)).tagName());
        assertEquals(0, errorList.size());
    }

    // Test parseXmlFragment method
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><child>Test</child></root>";
        String baseUri = "https://www.example.com/";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("child", ((Element) nodes.get(0)).tagName());
        assertEquals("Test", ((Element) nodes.get(0)).text());
    }

    // Test parseBodyFragment method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    // Test unescapeEntities method
    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;div&gt;&amp;Hello, World!&lt;/div&gt;";
        String unescapedString = "<div>&Hello, World!</div>";

        assertEquals(unescapedString, Parser.unescapeEntities(escapedString, false));
    }

    // Test parseBodyFragmentRelaxed method
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
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

