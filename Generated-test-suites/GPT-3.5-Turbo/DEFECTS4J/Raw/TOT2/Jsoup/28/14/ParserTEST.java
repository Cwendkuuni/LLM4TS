import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParserConstructor() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertNotNull(parser);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Document doc = parser.parseInput("<html><body><p>Hello World!</p></body></html>", "https://www.example.com");
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(null);
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getErrors().size());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body><p>Hello World!</p></body></html>", "https://www.example.com");
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodeList = Parser.parseFragment("<p>Hello World!</p>", context, "https://www.example.com");
        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Hello World!</p>", "https://www.example.com");
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertEquals(1, doc.body().childNodeSize());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<html><body><p>Hello World!</p></body></html>", "https://www.example.com");
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
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