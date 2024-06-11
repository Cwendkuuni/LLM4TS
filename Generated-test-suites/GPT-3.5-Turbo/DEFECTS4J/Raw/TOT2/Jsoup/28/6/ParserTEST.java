import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Typical use case
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Document doc = parser.parseInput("<html><body><p>Hello, World!</p></body></html>", "https://example.com");
        assertNotNull(doc);
        
        // Edge case
        doc = parser.parseInput("", "https://example.com");
        assertNull(doc);
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        TreeBuilder newTreeBuilder = new TreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertFalse(parser.isTrackErrors());
        
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        
        parser.setTrackErrors(3);
        assertEquals(3, parser.getErrors().size());
    }

    @Test
    public void testStaticParse() {
        Document doc = Parser.parse("<html><body><p>Hello, World!</p></body></html>", "https://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testStaticParseFragment() {
        Element element = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Test</p>", element, "https://example.com");
        
        assertEquals(1, nodes.size());
    }

    @Test
    public void testStaticParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Test</p>", "https://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Deprecated method, should provide similar behavior as parse()
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Test</p>", "https://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
    }

}