package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testConstructor() {
        // Test the constructor by initializing the Parser with different TreeBuilders and asserting that the returned TreeBuilder is the same as the one we set
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(parser.getTreeBuilder().getClass(), HtmlTreeBuilder.class);
        
        parser = new Parser(new XmlTreeBuilder());
        assertEquals(parser.getTreeBuilder().getClass(), XmlTreeBuilder.class);
    }
    
    @Test
    public void testParseInput() {
        // Test parseInput method by passing different HTML and baseUri values and assert that the returned Document is not null
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        Document doc = parser.parseInput("<html><head><title>Test</title></head><body>Hello World!</body></html>", "http://www.example.com");
        assertNotNull(doc);
        
        // Test parseInput method with invalid HTML and assert that the returned Document is null
        doc = parser.parseInput("<html><head><title>Test<title></head><body>Hello World!</body></html>", "http://www.example.com");
        assertNull(doc);
    }
    
    @Test
    public void testGetTreeBuilder() {
        // Test getTreeBuilder method by initializing the Parser with different TreeBuilders and asserting that the returned TreeBuilder is the same as the one we set
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(parser.getTreeBuilder().getClass(), HtmlTreeBuilder.class);
        
        parser = new Parser(new XmlTreeBuilder());
        assertEquals(parser.getTreeBuilder().getClass(), XmlTreeBuilder.class);
    }
    
    @Test
    public void testSetTreeBuilder() {
        // Test setTreeBuilder method by setting different TreeBuilders and asserting that the returned TreeBuilder is the same as the one we set
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(new XmlTreeBuilder());
        assertEquals(parser.getTreeBuilder().getClass(), XmlTreeBuilder.class);
        
        parser.setTreeBuilder(new HtmlTreeBuilder());
        assertEquals(parser.getTreeBuilder().getClass(), HtmlTreeBuilder.class);
    }
    
    @Test
    public void testIsTrackErrors() {
        // Test isTrackErrors method by enabling and disabling parse error tracking and asserting the returned value
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
        
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        // Test setTrackErrors method by enabling and disabling parse error tracking and asserting the returned value of isTrackErrors method
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
        
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        // Test getErrors method by parsing HTML with errors and asserting that the returned list of parse errors is not null and its size matches the number of errors tracked
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTrackErrors(10);
        parser.parseInput("<html><head><title>Test<title></head><body>Hello World!</body></html>", "http://www.example.com");
        
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(errors.size(), 1);
    }
    
    @Test
    public void testParse() {
        // Test parse method by passing different HTML and baseUri values and asserting that the returned Document is not null
        Document doc = Parser.parse("<html><head><title>Test</title></head><body>Hello World!</body></html>", "http://www.example.com");
        assertNotNull(doc);
        
        // Test parse method with invalid HTML and assert that the returned Document is null
        doc = Parser.parse("<html><head><title>Test<title></head><body>Hello World!</body></html>", "http://www.example.com");
        assertNull(doc);
    }
    
    @Test
    public void testParseFragment() {
        // Test parseFragment method by passing different fragment HTML, context element, and baseUri values and asserting that the returned list of nodes is not null
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Hello World!</p>", context, "http://www.example.com");
        assertNotNull(nodes);
        
        // Test parseFragment method with invalid fragment HTML and assert that the returned list of nodes is null
        nodes = Parser.parseFragment("<p>Hello World!</p>", null, "http://www.example.com");
        assertNull(nodes);
    }
    
    @Test
    public void testParseBodyFragment() {
        // Test parseBodyFragment method by passing different body HTML and baseUri values and asserting that the returned Document is not null
        Document doc = Parser.parseBodyFragment("<p>Hello World!</p>", "http://www.example.com");
        assertNotNull(doc);
        
        // Test parseBodyFragment method with invalid body HTML and assert that the returned Document is null
        doc = Parser.parseBodyFragment("<p>Hello World!</p>", null);
        assertNull(doc);
    }
    
    @Test
    public void testHtmlParser() {
        // Test htmlParser method by asserting that the returned Parser uses HtmlTreeBuilder as the TreeBuilder
        Parser parser = Parser.htmlParser();
        assertEquals(parser.getTreeBuilder().getClass(), HtmlTreeBuilder.class);
    }
    
    @Test
    public void testXmlParser() {
        // Test xmlParser method by asserting that the returned Parser uses XmlTreeBuilder as the TreeBuilder
        Parser parser = Parser.xmlParser();
        assertEquals(parser.getTreeBuilder().getClass(), XmlTreeBuilder.class);
    }
}