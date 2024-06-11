package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserTest {
    
    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertTrue(doc instanceof Document);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertNotNull(doc.body().getElementById("h1"));
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
        
        HtmlTreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        assertFalse(parser.isTrackErrors());
        
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        parser.setTrackErrors(3);
        assertEquals(3, parser.getTrackErrors());
        
        parser.setTrackErrors(0);
        assertEquals(0, parser.getTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        List<ParseError> errors = new ArrayList<>();
        errors.add(new ParseError("Error 1"));
        errors.add(new ParseError("Error 2"));
        
        parser.setTrackErrors(2);
        parser.setErrors(errors);
        
        assertEquals(errors, parser.getErrors());
    }
    
    @Test
    public void testParse() {
        String html = "<html></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertTrue(doc instanceof Document);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Fragment</p></div>";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertEquals(1, nodes.size());
        assertEquals("<p>Fragment</p>", nodes.get(0).toString());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertTrue(doc instanceof Document);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("<p>Hello World</p>", doc.body().html());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertTrue(doc instanceof Document);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("<p>Hello World</p>", doc.body().html());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        assertNotNull(parser.getTreeBuilder());
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        assertNotNull(parser.getTreeBuilder());
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}