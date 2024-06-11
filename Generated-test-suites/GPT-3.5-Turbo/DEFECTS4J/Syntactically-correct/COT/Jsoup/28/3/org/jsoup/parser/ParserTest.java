import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("<html><body><p>Hello World!</p></body></html>", doc.html());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
    }

    @Test
    public void testParse() {
        String html = "<html><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("<html><body><p>Hello World!</p></body></html>", doc.html());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World!</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("p", element.tagName());
        assertEquals("Hello World!", element.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head></head><body><p>Hello World!</p></body></html>", doc.html());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head></head><body><p>Hello World!</p></body></html>", doc.html());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

}