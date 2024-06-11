package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }
    
    // Test parseInput method
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        Document doc = parser.parseInput(html, "http://example.com");
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", doc.outerHtml());
    }
    
    // Test getTreeBuilder method
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }
    
    // Test setTreeBuilder method
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        Parser updatedParser = parser.setTreeBuilder(newTreeBuilder);
        
        assertNotNull(updatedParser);
        assertEquals(newTreeBuilder, updatedParser.getTreeBuilder());
    }
    
    // Test isTrackErrors method
    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        
        assertFalse(trackErrors);
    }
    
    // Test setTrackErrors method
    @Test
    public void testSetTrackErrors() {
        Parser updatedParser = parser.setTrackErrors(3);
        
        assertNotNull(updatedParser);
        assertTrue(updatedParser.isTrackErrors());
    }
    
    // Test getErrors method
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }
    
    // Test parse method
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", doc.outerHtml());
    }
    
    // Test parseFragment method
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello World!</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("p", ((Element) nodes.get(0)).tagName());
        assertEquals("Hello World!", ((Element) nodes.get(0)).text());
    }
    
    // Test parseBodyFragment method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <p>Hello World!</p>\n </body>\n</html>", doc.outerHtml());
    }
    
    // Test parseBodyFragmentRelaxed method
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <p>Hello World!</p>\n </body>\n</html>", doc.outerHtml());
    }
    
    // Test htmlParser method
    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        
        assertNotNull(htmlParser);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    // Test xmlParser method
    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        
        assertNotNull(xmlParser);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}