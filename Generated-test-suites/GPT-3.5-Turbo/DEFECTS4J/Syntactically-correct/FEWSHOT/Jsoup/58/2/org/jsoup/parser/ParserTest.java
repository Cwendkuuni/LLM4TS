package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testConstructor() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        
        Parser parser = new Parser(treeBuilder);
        
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        Document doc = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
        
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World</h1>\n </body>\n</html>", doc.outerHtml());
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
        
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        assertFalse(parser.isTrackErrors());
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
        
        assertNull(parser.getErrors());
    }
    
    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        ParseSettings settings = new ParseSettings(true, true);
        parser.settings(settings);
        
        assertEquals(settings, parser.settings());
    }
    
    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
        
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World</h1>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testParseFragment() {
        Element context = Jsoup.parseBodyFragment("<div></div>").body();
        
        List<Node> nodeList = Parser.parseFragment("<span>Hello World</span>", context, "http://example.com");
        
        assertEquals("<span>Hello World</span>", nodeList.get(0).outerHtml());
    }
    
    @Test
    public void testParseXmlFragment() {
        List<Node> nodeList = Parser.parseXmlFragment("<span>Hello World</span>", "http://example.com");
        
        assertEquals("<span>Hello World</span>", nodeList.get(0).outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<h1>Hello World</h1>", "http://example.com");
        
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World</h1>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;span&gt;Hello&nbsp;World&lt;/span&gt;", true);
        
        assertEquals("<span>Hello World</span>", unescaped);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<h1>Hello World</h1>", "http://example.com");
        
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World</h1>\n </body>\n</html>", doc.outerHtml());
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