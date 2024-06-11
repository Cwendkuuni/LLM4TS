package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import org.junit.Test;
import org.junit.Assert;
import java.util.List;


public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><head></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document document = parser.parseInput(html, baseUri);
        
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("Hello, World!", document.select("h1").text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder1 = new TreeBuilder();
        TreeBuilder treeBuilder2 = new TreeBuilder();
        Parser parser = new Parser(treeBuilder1);
        
        parser.setTreeBuilder(treeBuilder2);
        Assert.assertEquals(treeBuilder2, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser1 = new Parser(new TreeBuilder());
        Assert.assertFalse(parser1.isTrackErrors());
        
        Parser parser2 = new Parser(new TreeBuilder()).setTrackErrors(10);
        Assert.assertTrue(parser2.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser1 = new Parser(new TreeBuilder());
        parser1.setTrackErrors(5);
        Assert.assertTrue(parser1.isTrackErrors());
        
        Parser parser2 = new Parser(new TreeBuilder()).setTrackErrors(0);
        Assert.assertFalse(parser2.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><head></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        parser.parseInput(html, baseUri);
        
        Assert.assertEquals(0, parser.getErrors().size());
    }
    
    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());
        ParseSettings settings = new ParseSettings(false, false);
        
        parser.settings(settings);
        Assert.assertEquals(settings, parser.settings());
    }
    
    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("Hello, World!", document.select("h1").text());
    }
    
    @Test
    public void testParseFragment() {
        String html = "<p>Hello, <em>World</em>!</p>";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";
        List<Node> nodeList = Parser.parseFragment(html, context, baseUri);
        
        Assert.assertEquals(3, nodeList.size());
        Assert.assertEquals("p", nodeList.get(0).nodeName());
        Assert.assertEquals("em", nodeList.get(1).nodeName());
        Assert.assertEquals("text", nodeList.get(2).nodeName());
    }
    
    @Test
    public void testParseXmlFragment() {
        String xml = "<root><child>test</child></root>";
        String baseUri = "http://www.example.com";
        List<Node> nodeList = Parser.parseXmlFragment(xml, baseUri);
        
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("root", nodeList.get(0).nodeName());
        Assert.assertEquals("child", nodeList.get(0).child(0).nodeName());
        Assert.assertEquals("test", nodeList.get(0).child(0).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String html = "<p>Hello, <em>World</em>!</p>";
        String baseUri = "http://www.example.com";
        Document document = Parser.parseBodyFragment(html, baseUri);
        
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("Hello, World!", document.body().text());
    }
    
    @Test
    public void testUnescapeEntities() {
        String string = "Hello &lt;World&gt;! &amp; ";
        String unescaped = Parser.unescapeEntities(string, false);
        
        Assert.assertEquals("Hello <World>! & ", unescaped);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<html><head></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document document = Parser.parseBodyFragmentRelaxed(html, baseUri);
        
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("Hello, World!", document.select("h1").text());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}