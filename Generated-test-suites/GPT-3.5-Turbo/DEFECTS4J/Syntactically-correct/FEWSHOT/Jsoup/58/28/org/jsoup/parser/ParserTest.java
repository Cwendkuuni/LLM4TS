import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testConstructor() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertNotNull(parser.getTreeBuilder());
    }

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        Document document = parser.parseInput("<html></html>", "http://example.com");
        assertNotNull(document);
        Element rootElement = document.child(0);
        assertEquals("html", rootElement.tagName());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        parser.setTrackErrors(5);
        
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testSettings() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        ParseSettings settings = new ParseSettings(true);
        parser.settings(settings);
        
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html></html>", "http://example.com");
        assertNotNull(document);
        Element rootElement = document.child(0);
        assertEquals("html", rootElement.tagName());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment("<p>hello world</p>", context, baseUri);
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());

        assertEquals(1, nodes.size());
        Node node = nodes.get(0);

        assertTrue(node instanceof Element);
        assertEquals("p", ((Element) node).tagName());
        assertEquals("hello world", ((Element) node).text());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<p>hello world</p>", "http://example.com");
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());

        assertEquals(1, nodes.size());
        Node node = nodes.get(0);

        assertTrue(node instanceof Element);
        assertEquals("p", ((Element) node).tagName());
        assertEquals("hello world", ((Element) node).text());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<p>hello world</p>", "http://example.com");
        assertNotNull(document);

        Element body = document.body();
        assertNotNull(body);

        List<Node> children = body.childNodes();
        assertNotNull(children);
        assertFalse(children.isEmpty());

        assertEquals(1, children.size());
        Node child = children.get(0);

        assertTrue(child instanceof Element);
        assertEquals("p", ((Element) child).tagName());
        assertEquals("hello world", ((Element) child).text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;p&gt;hello world&lt;/p&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, true);

        assertEquals("<p>hello world</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<p>hello world</p>", "http://example.com");
        assertNotNull(document);
        Element rootElement = document.child(0);
        assertEquals("html", rootElement.tagName());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser.getTreeBuilder());
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser.getTreeBuilder());
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}