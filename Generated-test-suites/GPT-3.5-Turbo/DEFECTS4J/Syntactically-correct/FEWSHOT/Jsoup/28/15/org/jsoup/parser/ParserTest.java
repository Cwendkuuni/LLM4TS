
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><div id=\"test\">Hello World!</div></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        String expectedTitle = "Test Title";
        String expectedBody = "Test body content";
        
        assertEquals(expectedTitle, doc.title());
        assertEquals(expectedBody, doc.body().text());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        
        parser.setTreeBuilder(newTreeBuilder);
        
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        assertFalse(parser.isTrackErrors());
        
        parser.setTrackErrors(10);
        
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTrackErrors(5);
        
        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getErrors().size());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        List<ParseError> errors = parser.getErrors();
        
        assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><div id=\"test\">Hello World!</div></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        String expectedTitle = "Test Title";
        String expectedBody = "Test body content";
        
        assertEquals(expectedTitle, doc.title());
        assertEquals(expectedBody, doc.body().text());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div id=\"test\">Hello World!</div>";
        Element context = new Element(Tag.valueOf("div"), "");
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", nodes.get(0).tagName());
        assertEquals("Hello World!", ((Element) nodes.get(0)).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div id=\"test\">Hello World!</div>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        String expectedTitle = "Test Title";
        String expectedBody = "Test body content";
        
        assertEquals(expectedTitle, doc.title());
        assertEquals(expectedBody, doc.body().text());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div id=\"test\">Hello World!</div>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        String expectedTitle = "Test Title";
        String expectedBody = "Test body content";
        
        assertEquals(expectedTitle, doc.title());
        assertEquals(expectedBody, doc.body().text());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

}
