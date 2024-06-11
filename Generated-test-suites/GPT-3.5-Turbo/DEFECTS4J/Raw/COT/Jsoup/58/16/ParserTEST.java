
package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    // Test parseInput method
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><body>Test HTML</body></html>";
        String baseUri = "https://www.example.com";
        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("<html><head></head><body>Test HTML</body></html>", document.html());
        assertEquals(baseUri, document.baseUri());
    }

    // Test getTreeBuilder method
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
    }

    // Test setTreeBuilder method
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = new TreeBuilder();
        parser.setTreeBuilder(treeBuilder);

        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    // Test isTrackErrors method
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        boolean trackErrors = parser.isTrackErrors();

        assertFalse(trackErrors);
    }

    // Test setTrackErrors method
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        int maxErrors = 10;
        parser.setTrackErrors(maxErrors);

        assertTrue(parser.isTrackErrors());
    }

    // Test getErrors method
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
    }

    // Test settings method
    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());
        ParseSettings settings = new ParseSettings();
        parser.settings(settings);

        assertEquals(settings, parser.settings());
    }

    // Test parse method
    @Test
    public void testParse() {
        String html = "<html><body>Test HTML</body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("<html><head></head><body>Test HTML</body></html>", document.html());
        assertEquals(baseUri, document.baseUri());
    }

    // Test parseFragment method
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>This is a fragment</div>";
        Element context = new Element("body");
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", ((Element) nodes.get(0)).tagName());
        assertEquals("This is a fragment", ((Element) nodes.get(0)).text());
    }

    // Test parseXmlFragment method
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><element>Test XML</element></root>";
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("root", ((Element) nodes.get(0)).tagName());
        assertEquals("Test XML", ((Element) nodes.get(0)).text());
    }

    // Test parseBodyFragment method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a paragraph.</p>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("<html><head></head><body><p>This is a paragraph.</p></body></html>", document.html());
        assertEquals(baseUri, document.baseUri());
    }

    // Test unescapeEntities method
    @Test
    public void testUnescapeEntities() {
        String string = "This is a &lt;Test&gt; String";
        boolean inAttribute = false;
        String unescapedString = Parser.unescapeEntities(string, inAttribute);

        assertEquals("This is a <Test> String", unescapedString);
    }

    // Test parseBodyFragmentRelaxed method
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>This is a paragraph.</p>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("<html><head></head><body><p>This is a paragraph.</p></body></html>", document.html());
        assertEquals(baseUri, document.baseUri());
    }

    // Test htmlParser method
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();

        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    // Test xmlParser method
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();

        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

}
