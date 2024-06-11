import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        Document doc = parser.parseInput("<html></html>", "https://www.example.com");

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
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
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
        assertEquals(10, parser.getErrors().size());
    }

    @Test
    public void testSettings() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        ParseSettings settings = new ParseSettings(false);
        parser.settings(settings);

        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html></html>", "https://www.example.com");

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testParseFragment() {
        Element element = new Element("div");
        List<Node> nodeList = Parser.parseFragment("<p>This is a paragraph</p>", element, "https://www.example.com");

        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodeList = Parser.parseXmlFragment("<tag>Content</tag>", "https://www.example.com");

        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>This is a paragraph</p>", "https://www.example.com");

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testUnescapeEntities() {
        String unescapedString = Parser.unescapeEntities("&lt;p&gt;This is a paragraph&lt;/p&gt;", true);

        assertEquals("<p>This is a paragraph</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<html></html>", "https://www.example.com");

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();

        assertNotNull(parser);
        assertEquals("org.jsoup.parser.HtmlTreeBuilder", parser.getTreeBuilder().getClass().getName());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();

        assertNotNull(parser);
        assertEquals("org.jsoup.parser.XmlTreeBuilder", parser.getTreeBuilder().getClass().getName());
    }
}