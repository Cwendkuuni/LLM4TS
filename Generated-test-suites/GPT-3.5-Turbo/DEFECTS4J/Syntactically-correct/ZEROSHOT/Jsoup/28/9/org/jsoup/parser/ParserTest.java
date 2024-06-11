import org.jsoup.parser.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testparseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "https://www.example.com");
        assertNotNull("Document should not be null", doc);
        assertEquals("Document should have h1 element", 1, doc.select("h1").size());
    }
    
    @Test
    public void testgetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull("TreeBuilder should not be null", treeBuilder);
    }
    
    @Test
    public void testsetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals("TreeBuilder should be updated", treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testisTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse("Track errors should be disabled", parser.isTrackErrors());
    }
    
    @Test
    public void testsetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue("Track errors should be enabled", parser.isTrackErrors());
        assertEquals("Max errors should be updated", 5, parser.getMaxErrors());
    }
    
    @Test
    public void testgetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        List<ParseError> errors = parser.getErrors();
        assertNotNull("Errors should not be null", errors);
        assertEquals("Errors size should be 0", 0, errors.size());
    }
    
    @Test
    public void testparse() {
        Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "https://www.example.com");
        assertNotNull("Document should not be null", doc);
        assertEquals("Document should have h1 element", 1, doc.select("h1").size());
    }
    
    @Test
    public void testparseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<span>Hello World</span>", context, "https://www.example.com");
        assertNotNull("Nodes should not be null", nodes);
        assertEquals("Nodes size should be 1", 1, nodes.size());
    }
    
    @Test
    public void testparseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<h1>Hello World</h1>", "https://www.example.com");
        assertNotNull("Document should not be null", doc);
        assertEquals("Document should have h1 element", 1, doc.select("h1").size());
    }
    
    @Test
    public void testparseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<h1>Hello World</h1>", "https://www.example.com");
        assertNotNull("Document should not be null", doc);
        assertEquals("Document should have h1 element", 1, doc.select("h1").size());
    }
    
    @Test
    public void testhtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull("Parser should not be null", parser);
        assertTrue("TreeBuilder should be HtmlTreeBuilder", parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testxmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull("Parser should not be null", parser);
        assertTrue("TreeBuilder should be XmlTreeBuilder", parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}