package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("html", doc.nodeName());
        Assert.assertEquals(1, doc.childNodeSize());
        
        Node childNode = doc.childNode(0);
        Assert.assertEquals("html", childNode.nodeName());
        Assert.assertEquals(1, childNode.childNodeSize());
        
        Node grandChildNode = childNode.childNode(0);
        Assert.assertEquals("body", grandChildNode.nodeName());
        Assert.assertEquals(1, grandChildNode.childNodeSize());
        
        Node greatGrandChildNode = grandChildNode.childNode(0);
        Assert.assertEquals("h1", greatGrandChildNode.nodeName());
        Assert.assertEquals(1, greatGrandChildNode.childNodeSize());
        
        Node textNode = greatGrandChildNode.childNode(0);
        Assert.assertEquals("#text", textNode.nodeName());
        Assert.assertEquals("Hello World", textNode.outerHtml());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        TreeBuilder returnedTreeBuilder = parser.getTreeBuilder();
        
        Assert.assertNotNull(returnedTreeBuilder);
        Assert.assertEquals(treeBuilder.getClass(), returnedTreeBuilder.getClass());
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        
        TreeBuilder returnedTreeBuilder = parser.getTreeBuilder();
        
        Assert.assertNotNull(returnedTreeBuilder);
        Assert.assertEquals(newTreeBuilder.getClass(), returnedTreeBuilder.getClass());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        boolean trackErrors = parser.isTrackErrors();
        
        Assert.assertFalse(trackErrors);
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTrackErrors(10);
        boolean trackErrors = parser.isTrackErrors();
        Assert.assertTrue(trackErrors);
        
        parser.setTrackErrors(0);
        trackErrors = parser.isTrackErrors();
        Assert.assertFalse(trackErrors);
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        List<ParseError> errors = parser.getErrors();
        
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }
    
    @Test
    public void testParse() {
        Document doc = Parser.parse("<p>Hello World</p>", "http://www.example.com");
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("p", doc.childNode(0).nodeName());
        Assert.assertEquals("Hello World", doc.text());
    }
    
    @Test
    public void testParseFragment() {
        Element div = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Hello World</p>", div, "http://www.example.com");
        
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("div", div.nodeName());
        Assert.assertEquals("p", nodes.get(0).nodeName());
        Assert.assertEquals("Hello World", nodes.get(0).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Hello World</p>", "http://www.example.com");
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("p", doc.body().childNode(0).nodeName());
        Assert.assertEquals("Hello World", doc.body().text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Hello World</p>", "http://www.example.com");
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("p", doc.childNode(0).nodeName());
        Assert.assertEquals("Hello World", doc.text());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        Assert.assertNotNull(parser);
        Assert.assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        Assert.assertNotNull(parser);
        Assert.assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

}