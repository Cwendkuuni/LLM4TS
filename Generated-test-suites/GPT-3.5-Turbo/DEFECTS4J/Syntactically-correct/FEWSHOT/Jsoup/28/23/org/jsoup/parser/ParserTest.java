
package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<div>Hello World</div>";
        String baseUri = "https://example.com";
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(1, doc.children().size());
        Element div = doc.children().get(0);
        assertEquals("div", div.tagName());
        assertEquals("Hello World", div.text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertEquals(TreeBuilder.class, treeBuilder.getClass());
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        boolean trackErrors = parser.isTrackErrors();
        
        assertEquals(false, trackErrors);
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(5);
        
        assertEquals(true, parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }
    
    @Test
    public void testParse() {
        String html = "<div>Hello World</div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(1, doc.children().size());
        Element div = doc.children().get(0);
        assertEquals("div", div.tagName());
        assertEquals("Hello World", div.text());
    }
    
    @Test
    public void testParseFragment() {
        String html = "<span>Hello</span><span>World</span>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(html, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        Element span1 = (Element) nodes.get(0);
        assertEquals("span", span1.tagName());
        assertEquals("Hello", span1.text());
        Element span2 = (Element) nodes.get(1);
        assertEquals("span", span2.tagName());
        assertEquals("World", span2.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String html = "<span>Hello World</span>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(1, doc.body().children().size());
        Element span = doc.body().children().get(0);
        assertEquals("span", span.tagName());
        assertEquals("Hello World", span.text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<span>Hello World</span>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(1, doc.children().size());
        Element span = doc.children().get(0);
        assertEquals("span", span.tagName());
        assertEquals("Hello World", span.text());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertEquals(XmlTreeBuilder.class, treeBuilder.getClass());
    }
    
}
