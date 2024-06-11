package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><body><div>Hello World</div></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(1, body.children().size());
        Element div = body.child(0);
        assertEquals("div", div.tagName());
        assertEquals("Hello World", div.text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(treeBuilder instanceof TreeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = new TreeBuilder();
        
        Parser newParser = parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, newParser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        
        assertFalse(parser.isTrackErrors());
        
        parser.setTrackErrors(10);
        
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertEquals(parser, parser.setTrackErrors(10));
        assertEquals(parser, parser.setTrackErrors(0));
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors instanceof List);
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><div>Hello World</div></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(1, body.children().size());
        Element div = body.child(0);
        assertEquals("div", div.tagName());
        assertEquals("Hello World", div.text());
    }
    
    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        context.appendElement("span").text("Hello");
        String fragmentHtml = "<p>World</p>";
        String baseUri = "https://www.example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertEquals(1, nodes.size());
        Element p = (Element) nodes.get(0);
        assertEquals("p", p.tagName());
        assertEquals("World", p.text());
        assertNull(context.child(0));
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(1, body.children().size());
        Element p = body.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(1, body.children().size());
        Element p = body.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
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