package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInput_WithValidHtml_ReturnsDocument() {
        String html = "<html><head><title>Test</title></head><body><p>Hello!</p></body></html>";
        String baseUri = "http://www.example.com";

        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello!", doc.body().selectFirst("p").text());
    }

    @Test
    public void testParseInput_WithInvalidHtml_ReturnsDocumentWithErrors() {
        String html = "<html><head><title>Test</title></head><body><p>Hello!</p>";
        String baseUri = "http://www.example.com";

        Document doc = parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello!", doc.body().selectFirst("p").text());
        assertEquals(1, errors.size());
        assertEquals("Unclosed tag: p", errors.get(0).getErrorMsg());
    }

    @Test
    public void testGetTreeBuilder_ReturnsInitializedTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder_ChangesTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();

        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }

    @Test
    public void testIsTrackErrors_WhenErrorsTrackingEnabled_ReturnsTrue() {
        parser.setTrackErrors(5);

        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testIsTrackErrors_WhenErrorsTrackingDisabled_ReturnsFalse() {
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors_ChangesErrorsTracking() {
        parser.setTrackErrors(5);

        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors_ReturnsEmptyList_WhenNoErrorsOccurred() {
        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParse_WithValidHtml_ReturnsDocument() {
        String html = "<html><head><title>Test</title></head><body><p>Hello!</p></body></html>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello!", doc.body().selectFirst("p").text());
    }

    @Test
    public void testParseFragment_WithValidHtml_ReturnsListOfNodes() {
        String htmlFragment = "<div><p>Hello</p><p>World</p></div>";
        Element context = new Element(Tag.valueOf("div"), "");
        String baseUri = "http://www.example.com";

        List<Node> nodes = Parser.parseFragment(htmlFragment, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("div", nodes.get(0).nodeName());
        assertEquals("Hello", nodes.get(0).child(0).text());
        assertEquals("World", nodes.get(0).child(1).text());
    }

    @Test
    public void testParseFragment_WithInvalidHtml_ReturnsEmptyListOfNodes() {
        String htmlFragment = "<div><p>Hello</div>";
        Element context = new Element(Tag.valueOf("div"), "");
        String baseUri = "http://www.example.com";

        List<Node> nodes = Parser.parseFragment(htmlFragment, context, baseUri);

        assertNotNull(nodes);
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testParseBodyFragment_WithValidHtml_ReturnsDocumentWithBodyParsed() {
        String bodyHtml = "<p>Hello!</p><p>World!</p>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertNull(doc.head());
        assertEquals("Hello!", doc.body().child(0).text());
        assertEquals("World!", doc.body().child(1).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed_WithValidHtml_ReturnsDocument() {
        String bodyHtml = "<p>Hello!</p><p>World!</p>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello!", doc.body().child(0).text());
        assertEquals("World!", doc.body().child(1).text());
    }

    @Test
    public void testHtmlParser_ReturnsParserWithHtmlTreeBuilder() {
        Parser htmlParser = Parser.htmlParser();

        assertNotNull(htmlParser);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser_ReturnsParserWithXmlTreeBuilder() {
        Parser xmlParser = Parser.xmlParser();

        assertNotNull(xmlParser);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}