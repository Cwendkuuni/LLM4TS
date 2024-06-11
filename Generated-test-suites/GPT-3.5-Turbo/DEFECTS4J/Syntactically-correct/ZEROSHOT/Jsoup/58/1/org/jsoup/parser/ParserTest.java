import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document parsedDocument = parser.parseInput(html, baseUri);
        assertEquals(document, parsedDocument);
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Document parsedDocument = Parser.parse(html, baseUri);
        assertEquals(document, parsedDocument);
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello World!</div>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodeList.size());
        assertTrue(nodeList.get(0) instanceof Element);
        Element element = (Element) nodeList.get(0);
        assertEquals("div", element.tagName());
        assertEquals("Hello World!", element.text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><child>Hello World!</child></root>";
        String baseUri = "http://example.com";
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertEquals(1, nodeList.size());
        assertTrue(nodeList.get(0) instanceof Element);
        Element element = (Element) nodeList.get(0);
        assertEquals("root", element.tagName());
        assertEquals(1, element.children().size());
        Element child = element.child(0);
        assertEquals("child", child.tagName());
        assertEquals("Hello World!", child.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element body = document.body();
        List<Node> nodeList = Parser.parseFragment(bodyHtml, body, baseUri);
        assertEquals(1, nodeList.size());
        assertTrue(nodeList.get(0) instanceof Element);
        Element element = (Element) nodeList.get(0);
        assertEquals("h1", element.tagName());
        assertEquals("Hello World!", element.text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;html&gt;&lt;h1&gt;Hello World!&lt;/h1&gt;&lt;/html&gt;";
        String unescapedString = "<html><h1>Hello World!</h1></html>";
        String result = Parser.unescapeEntities(escapedString, false);
        assertEquals(unescapedString, result);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element body = document.body();
        List<Node> nodeList = Parser.parseFragment(bodyHtml, body, baseUri);
        assertEquals(1, nodeList.size());
        assertTrue(nodeList.get(0) instanceof Element);
        Element element = (Element) nodeList.get(0);
        assertEquals("h1", element.tagName());
        assertEquals("Hello World!", element.text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}