package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        // Typical Use Case
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);
        
        // Verify the parsed document
        Assert.assertEquals("html", doc.child(0).tagName());
        Assert.assertEquals("h1", doc.select("h1").first().tagName());
        Assert.assertEquals("p", doc.select("p").first().tagName());
        Assert.assertEquals("Title", doc.select("h1").first().text());
        Assert.assertEquals("Paragraph", doc.select("p").first().text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        // Typical Use Case
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
        
        // Edge Case: When setTreeBuilder() is not called
        parser = new Parser(null);
        treeBuilder = parser.getTreeBuilder();
        Assert.assertNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        // Typical Use Case
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
        
        // Edge Case: When null is passed as the parameter
        parser.setTreeBuilder(null);
        Assert.assertNull(parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        // Typical Use Case
        parser.setTrackErrors(10);
        Assert.assertTrue(parser.isTrackErrors());
        
        // Edge Case: When 0 is passed as the parameter
        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        // Typical Use Case
        parser.setTrackErrors(10);
        Assert.assertTrue(parser.isTrackErrors());
        
        // Edge Case: When 0 is passed as the parameter
        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        // Typical Use Case
        parser.setTrackErrors(2);
        Document doc = parser.parseInput("<html><body></body></html>", "http://example.com");
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
        
        doc = parser.parseInput("<html><body><p></body></html>", "http://example.com");
        errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        
        // Edge Case: When maxErrors is set to 0
        parser.setTrackErrors(0);
        doc = parser.parseInput("<html><body><p></body></html>", "http://example.com");
        errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }
    
    @Test
    public void testParse() {
        // Typical Use Case
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        // Verify the parsed document
        Assert.assertEquals("html", doc.child(0).tagName());
        Assert.assertEquals("h1", doc.select("h1").first().tagName());
        Assert.assertEquals("p", doc.select("p").first().tagName());
        Assert.assertEquals("Title", doc.select("h1").first().text());
        Assert.assertEquals("Paragraph", doc.select("p").first().text());
    }
    
    @Test
    public void testParseFragment() {
        // Typical Use Case
        String fragmentHtml = "<h2>Subtitle</h2><p>Subparagraph</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        // Verify the parsed nodes
        Assert.assertEquals("h2", nodes.get(0).nodeName());
        Assert.assertEquals("p", nodes.get(1).nodeName());
        Assert.assertEquals("Subtitle", nodes.get(0).child(0).outerHtml());
        Assert.assertEquals("Subparagraph", nodes.get(1).child(0).outerHtml());
        
        // Edge Case: When context element is not provided
        nodes = Parser.parseFragment(fragmentHtml, null, baseUri);
        Assert.assertEquals("h2", nodes.get(0).nodeName());
        Assert.assertEquals("p", nodes.get(1).nodeName());
        Assert.assertEquals("Subtitle", nodes.get(0).child(0).outerHtml());
        Assert.assertEquals("Subparagraph", nodes.get(1).child(0).outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        // Typical Use Case
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        // Verify the parsed document
        Assert.assertEquals("body", doc.body().tagName());
        Assert.assertEquals("h1", doc.select("h1").first().tagName());
        Assert.assertEquals("p", doc.select("p").first().tagName());
        Assert.assertEquals("Title", doc.select("h1").first().text());
        Assert.assertEquals("Paragraph", doc.select("p").first().text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        // Typical Use Case
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        // Verify the parsed document
        Assert.assertEquals("body", doc.body().tagName());
        Assert.assertEquals("h1", doc.select("h1").first().tagName());
        Assert.assertEquals("p", doc.select("p").first().tagName());
        Assert.assertEquals("Title", doc.select("h1").first().text());
        Assert.assertEquals("Paragraph", doc.select("p").first().text());
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