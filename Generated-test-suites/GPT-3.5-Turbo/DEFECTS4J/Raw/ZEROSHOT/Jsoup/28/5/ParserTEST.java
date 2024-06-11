import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.parser.TreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;

import java.util.List;

public class ParserTest {
    
    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html><body><h1>Hello World!</h1></body></html>", doc.html());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder1 = new HtmlTreeBuilder();
        TreeBuilder treeBuilder2 = new XmlTreeBuilder();
        
        Parser parser = new Parser(treeBuilder1);
        parser.setTreeBuilder(treeBuilder2);
        
        assertEquals(treeBuilder2, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        assertFalse(parser.isTrackErrors());
        
        parser.setTrackErrors(5);
        
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTrackErrors(5);
        
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTrackErrors(5);
        
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html><body><h1>Hello World!</h1></body></html>", doc.html());
    }
    
    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        
        String fragmentHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("<p>Test</p>", nodes.get(0).outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html><head></head><body><p>Test</p></body></html>", doc.html());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        assertNotNull(parser);
        assertEquals(parser.getTreeBuilder().getClass(), HtmlTreeBuilder.class);
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        assertNotNull(parser);
        assertEquals(parser.getTreeBuilder().getClass(), XmlTreeBuilder.class);
    }
}