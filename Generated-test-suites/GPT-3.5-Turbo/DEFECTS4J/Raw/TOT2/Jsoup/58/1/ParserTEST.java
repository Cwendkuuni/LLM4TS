package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testParserConstructor() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertNotNull(parser.getTreeBuilder());
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTreeBuilder(treeBuilder);
        
        assertNotNull(parser.getTreeBuilder());
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        
        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getErrors().size());
    }
    
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("<html><body><p>Hello, World!</p></body></html>", "http://www.example.com");
        
        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
    }
    
    // Add more individual test cases for other methods as needed
    
    @Test
    public void testStaticMethods() {
        Document document = Parser.parse("<html><body><p>Hello, World!</p></body></html>", "http://www.example.com");
        assertNotNull(document);
        
        List<Node> nodes = Parser.parseFragment("<p>Hello, World!</p>", new Element("div"), "http://www.example.com");
        assertEquals(1, nodes.size());
        
        List<Node> xmlNodes = Parser.parseXmlFragment("<p>Hello, World!</p>", "http://www.example.com");
        assertEquals(1, xmlNodes.size());
        
        Document bodyDocument = Parser.parseBodyFragment("<p>Hello, World!</p>", "http://www.example.com");
        assertNotNull(bodyDocument);
    }
    
    @Test
    public void testUtilityMethods() {
        String unescapedString = Parser.unescapeEntities("&lt;p&gt;Hello, World!&lt;/p&gt;", false);
        assertEquals("<p>Hello, World!</p>", unescapedString);
        
        String attributeString = Parser.unescapeEntities("&lt;p&gt;Hello, World!&lt;/p&gt;", true);
        assertEquals("<p>Hello, World!</p>", attributeString);
    }
    
    //Add more test cases for other scenarios
    
    @Test
    public void testDeprecatedMethod() {
        Document document = Parser.parseBodyFragmentRelaxed("<html><body><p>Hello, World!</p></body></html>", "http://www.example.com");
        assertNotNull(document);
    }
    
    @Test
    public void testHtmlParserBuilder() {
        Parser htmlParser = Parser.htmlParser();
        assertNotNull(htmlParser);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParserBuilder() {
        Parser xmlParser = Parser.xmlParser();
        assertNotNull(xmlParser);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}