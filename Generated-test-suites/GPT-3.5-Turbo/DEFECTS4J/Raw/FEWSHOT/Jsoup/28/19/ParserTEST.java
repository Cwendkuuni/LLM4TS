import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.*;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Title</h1><p>Content</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Title</h1><p>Content</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Parser updatedParser = parser.setTreeBuilder(new XmlTreeBuilder());
        
        assertNotNull(updatedParser);
        assertTrue(updatedParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Parser updatedParser = parser.setTrackErrors(5);
        
        assertNotNull(updatedParser);
        assertTrue(updatedParser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Title</h1><p>Content</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Title</h1><p>Content</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        String fragmentHtml = "<p>Fragment</p>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("p", element.tagName());
        assertEquals("Fragment", element.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Body</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html><head></head><body><p>Body</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Body</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html><head></head><body><p>Body</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }
}