package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><div id=\"main\"><p>Hello World!</p></div></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        
        // Check if the document has correct tags and elements
        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals(1, doc.children().size());
        
        Element htmlElement = doc.child(0);
        Assert.assertEquals("head", htmlElement.child(0).tagName());
        Assert.assertEquals("body", htmlElement.child(1).tagName());
        
        Element bodyElement = htmlElement.child(1);
        Assert.assertEquals("div", bodyElement.child(0).tagName());
        Assert.assertEquals("p", bodyElement.child(0).child(0).tagName());
        
        Element pElement = bodyElement.child(0).child(0);
        Assert.assertEquals("Hello World!", pElement.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        // Check if the body is parsed correctly
        Assert.assertEquals(1, doc.body().children().size());
        
        Element pElement = doc.body().child(0);
        Assert.assertEquals("Hello World!", pElement.text());
    }
    
    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        // Check if the comment is parsed correctly
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertTrue(doc.childNode(0) instanceof Comment);
        
        Comment comment = (Comment) doc.childNode(0);
        Assert.assertEquals(" This is a comment ", comment.getData());
    }
    
    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version='1.0' encoding='UTF-8'?>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        // Check if the xml declaration is parsed correctly
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertTrue(doc.childNode(0) instanceof XmlDeclaration);
        
        XmlDeclaration decl = (XmlDeclaration) doc.childNode(0);
        Assert.assertEquals("xml version='1.0' encoding='UTF-8'", decl.getData());
        Assert.assertTrue(decl.attr("procInstr"));
    }
    
    @Test
    public void testParseEndTag() {
        String html = "<p>Hello World!</b>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        // Check if the end tag is parsed correctly and unwrapped
        Assert.assertEquals(1, doc.body().children().size());
        Assert.assertEquals(1, doc.body().child(0).childNodes().size());
        Assert.assertTrue(doc.body().child(0).childNode(0) instanceof TextNode);
        
        TextNode textNode = (TextNode) doc.body().child(0).childNode(0);
        Assert.assertEquals("Hello World!</b>", textNode.getWholeText());
    }
    
    @Test
    public void testParseStartTag() {
        String html = "<div></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        // Check if the start tag is parsed correctly and added as child
        Assert.assertEquals(1, doc.body().children().size());
        Assert.assertEquals("div", doc.body().child(0).tagName());
    }
    
    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        // Check if the text node is parsed correctly
        Assert.assertEquals(1, doc.body().child(0).childNodes().size());
        Assert.assertTrue(doc.body().child(0).childNode(0) instanceof TextNode);
        
        TextNode textNode = (TextNode) doc.body().child(0).childNode(0);
        Assert.assertEquals("Hello World!", textNode.getWholeText());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        // Check if the CDATA section is parsed correctly
        Assert.assertEquals(1, doc.body().childNodes().size());
        Assert.assertTrue(doc.body().childNode(0) instanceof TextNode);
        
        TextNode textNode = (TextNode) doc.body().childNode(0);
        Assert.assertEquals("This is CDATA", textNode.getWholeText());
    }
    
    @Test
    public void testParseAttribute() {
        String html = "<div class=\"container\"></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        // Check if the attribute is parsed correctly and set on element
        Element divElement = doc.body().child(0);
        Assert.assertEquals("container", divElement.attr("class"));
    }
    
    @Test
    public void testPopStackToSuitableContainer() {
        String html = "<html><head><title>Test</title></head><body><div><p>Hello World!</div></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        // Check if the stack is correctly popped to suitable container
        Element divElement = doc.body().child(0);
        Assert.assertEquals("p", divElement.child(0).tagName());
    }
    
    @Test
    public void testPopStackToClose() {
        String html = "<div><p>Hello World!</p></div><p>Extra paragraph</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Elements paragraphs = doc.select("p");
        
        // Check if the stack is correctly popped to close nested div tag
        Assert.assertEquals(2, paragraphs.size());
        Assert.assertEquals("Hello World!", paragraphs.get(0).text());
        Assert.assertEquals("Extra paragraph", paragraphs.get(1).text());
    }
    
    @Test
    public void testStackHasValidParent() {
        String html = "<p><div><p>Hello World!</p></div></p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Elements paragraphs = doc.select("p");
        
        // Check if the stack has valid parent for nested p tag
        Assert.assertEquals(2, paragraphs.size());
        Assert.assertEquals("Hello World!", paragraphs.get(0).text());
        Assert.assertEquals("", paragraphs.get(1).text());
    }
}