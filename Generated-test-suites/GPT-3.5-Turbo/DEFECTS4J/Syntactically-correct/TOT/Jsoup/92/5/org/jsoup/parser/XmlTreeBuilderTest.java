package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.*;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings settings = treeBuilder.defaultSettings();
        
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<tag>test</tag>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(new XmlTreeBuilder());
        
        // Creating a reader to read the input
        Reader reader = new StringReader(input);
        
        // Initializing the parse
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.initialiseParse(reader, baseUri, parser);
        
        Document doc = treeBuilder.doc;
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }
    
    @Test
    public void testParse() {
        String input = "<tag>test</tag>";
        String baseUri = "https://www.example.com";
        
        // Parsing the document
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
        assertEquals("<tag>test</tag>", doc.html());
    }
    
    @Test
    public void testProcessTokenStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        startTag.attributes(new Attributes());
        
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element element = treeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals(0, element.attributes().size());
    }
    
    @Test
    public void testProcessTokenEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("tag");
        
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element("tag"));
        treeBuilder.popStackToClose(endTag);
        
        assertEquals(0, treeBuilder.stack.size());
    }
    
    @Test
    public void testProcessTokenComment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("test comment");
        
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.insert(commentToken);
        
        Node node = treeBuilder.currentElement().childNodes().get(0);
        
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        assertEquals("test comment", ((Comment) node).getData());
    }
    
    @Test
    public void testProcessTokenCharacter() {
        Token.Character characterToken = new Token.Character();
        characterToken.data("test data");
        
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.insert(characterToken);
        
        Node node = treeBuilder.currentElement().childNodes().get(0);
        
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        assertEquals("test data", ((TextNode) node).getWholeText());
    }
    
    @Test
    public void testProcessTokenDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");
        doctype.setPublicIdentifier("-//W3C//DTD XHTML 1.0 Transitional//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        doctype.setPubSysKey("-//W3C//DTD XHTML 1.0 Transitional//ENhttp://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.insert(doctype);
        
        Node node = treeBuilder.currentElement().childNodes().get(0);
        
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) node;
        assertEquals("html", doctypeNode.nodeName());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctypeNode.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctypeNode.attr("systemId"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//ENhttp://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctypeNode.attr("pubSysKey"));
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("tag");
        
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element element = new Element("tag");
        treeBuilder.stack.add(element);
        treeBuilder.popStackToClose(endTag);
        
        assertEquals(0, treeBuilder.stack.size());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>test</tag>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(new XmlTreeBuilder());
        
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, parser);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
        assertEquals("test", element.text());
    }
    
    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "<tag>test</tag>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(new XmlTreeBuilder());
        
        Element context = new Element("context");
        
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
        assertEquals("test", element.text());
    }
}