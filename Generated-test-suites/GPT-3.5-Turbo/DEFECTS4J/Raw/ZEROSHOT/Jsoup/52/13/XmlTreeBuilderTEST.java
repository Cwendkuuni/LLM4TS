package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;

import java.util.List;

public class XmlTreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>hello</tag></xml>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        builder.initialiseParse(input, baseUri, errors);
        
        assertEquals(builder.currentElement(), builder.doc);
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.addAttribute("class", "test");
        
        builder.stack.add(new Element(Tag.valueOf("xml"), "https://example.com"));
        
        Element el = builder.insert(startTag);
        
        assertEquals("tag", el.tagName());
        assertEquals("https://example.com", el.baseUri());
        assertTrue(el.hasAttr("class"));
        assertEquals("test", el.attr("class"));
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Token.EndTag endTag = new Token.EndTag("tag");
        
        Element element1 = new Element(Tag.valueOf("tag"), "https://example.com");
        Element element2 = new Element(Tag.valueOf("div"), "https://example.com");
        
        builder.stack.add(element2);
        builder.stack.add(element1);
        
        builder.popStackToClose(endTag);
        
        assertEquals(1, builder.stack.size());
        assertEquals(element2, builder.stack.get(0));
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Token.Comment commentToken = new Token.Comment("test comment", "https://example.com");
        
        builder.stack.add(new Element(Tag.valueOf("xml"), "https://example.com"));
        
        builder.insert(commentToken);
        
        Node node = builder.currentElement().childNodes().get(0);
        
        assertTrue(node instanceof Comment);
        assertEquals("test comment", node.outerHtml());
        assertEquals("https://example.com", ((Comment) node).baseUri());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Token.Character characterToken = new Token.Character("test character");
        
        builder.stack.add(new Element(Tag.valueOf("xml"), "https://example.com"));
        
        builder.insert(characterToken);
        
        Node node = builder.currentElement().childNodes().get(0);
        
        assertTrue(node instanceof TextNode);
        assertEquals("test character", node.outerHtml());
        assertEquals("https://example.com", ((TextNode) node).baseUri());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Token.Doctype doctypeToken = new Token.Doctype("test", "public", "system");
        
        builder.stack.add(new Element(Tag.valueOf("xml"), "https://example.com"));
        
        builder.insert(doctypeToken);
        
        Node node = builder.currentElement().childNodes().get(0);
        
        assertTrue(node instanceof DocumentType);
        assertEquals("test", ((DocumentType) node).name());
        assertEquals("public", ((DocumentType) node).publicIdentifier());
        assertEquals("system", ((DocumentType) node).systemIdentifier());
        assertEquals("https://example.com", ((DocumentType) node).baseUri());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        String inputFragment = "<tag>hello</tag>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", nodes.get(0).nodeName());
        assertEquals("https://example.com", nodes.get(0).baseUri());
        assertEquals("hello", nodes.get(0).outerHtml());
    }
    
}