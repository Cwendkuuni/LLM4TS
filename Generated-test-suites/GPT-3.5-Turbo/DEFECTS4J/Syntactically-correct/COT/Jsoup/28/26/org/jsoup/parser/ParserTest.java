
package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private Parser parser = new Parser(new HtmlTreeBuilder());

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
        Assert.assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        Assert.assertEquals(newTreeBuilder, treeBuilder);
    }
    
    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        
        Assert.assertFalse(trackErrors);
    }
    
    @Test
    public void testSetTrackErrors() {
        int maxErrors = 5;
        Parser updatedParser = parser.setTrackErrors(maxErrors);
        
        Assert.assertEquals(maxErrors, updatedParser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }
    
    @Test
    public void testStaticParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
        Assert.assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testStaticParseFragment() {
        String fragmentHtml = "<h1>Hello World</h1>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("<h1>Hello World</h1>", nodes.get(0).outerHtml());
    }
    
    @Test
    public void testStaticParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
        Assert.assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testStaticParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
        Assert.assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        
        Assert.assertNotNull(htmlParser);
        Assert.assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        
        Assert.assertNotNull(xmlParser);
        Assert.assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}
