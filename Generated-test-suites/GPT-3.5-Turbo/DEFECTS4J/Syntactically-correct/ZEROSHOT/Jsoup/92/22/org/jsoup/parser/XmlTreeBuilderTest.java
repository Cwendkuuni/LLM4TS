package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.*;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private String xml;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        xml = "<root><element1>Text 1</element1><element2>Text 2</element2></root>";
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
    }
    
    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader(xml);
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        
        // Assert the stack contains the document
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(Document.class, xmlTreeBuilder.getStack().get(0).getClass());
        
        // Assert the document's output settings
        assertNotNull(xmlTreeBuilder.getDoc().outputSettings());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDoc().outputSettings().syntax());
    }
    
    @Test
    public void testParseReaderString() {
        Reader input = new StringReader(xml);
        String baseUri = "http://example.com";
        
        Document result = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(result);
        assertTrue(result instanceof Document);
    }
    
    @Test
    public void testParseStringString() {
        String baseUri = "http://example.com";
        
        Document result = xmlTreeBuilder.parse(xml, baseUri);
        
        assertNotNull(result);
        assertTrue(result instanceof Document);
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        startTag.addAttribute("attribute", "value");
        
        Element result = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(result);
        assertEquals("element", result.nodeName());
        assertEquals("http://example.com", result.baseUri());
        
        Attributes attributes = result.attributes();
        assertEquals("value", attributes.get("attribute"));
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("element");
        
        xmlTreeBuilder.getStack().push(new Element("element1"));
        xmlTreeBuilder.getStack().push(new Element("element2"));
        xmlTreeBuilder.getStack().push(new Element("element"));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assert the stack is updated correctly
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals("element2", xmlTreeBuilder.getStack().get(0).nodeName());
        assertEquals("element1", xmlTreeBuilder.getStack().get(1).nodeName());
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        
        // Assert the comment node is inserted
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) childNodes.get(0)).getData());
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("Text");
        
        xmlTreeBuilder.insert(characterToken);
        
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        
        // Assert the character node is inserted
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        assertEquals("Text", ((TextNode) childNodes.get(0)).getWholeText());
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        doctype.setPublicIdentifier("-//W3C//DTD HTML 4.01 Frameset//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/html4/frameset.dtd");
        
        xmlTreeBuilder.insert(doctype);
        
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        
        // Assert the doctype node is inserted
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) childNodes.get(0);
        assertEquals("html", doctypeNode.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01 Frameset//EN", doctypeNode.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/frameset.dtd", doctypeNode.getSystemIdentifier());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("element");
        
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        Element element = new Element("element");
        
        xmlTreeBuilder.getStack().push(element1);
        xmlTreeBuilder.getStack().push(element2);
        xmlTreeBuilder.getStack().push(element);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assert the stack is updated correctly
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals(element2, xmlTreeBuilder.getStack().get(0));
        assertEquals(element1, xmlTreeBuilder.getStack().get(1));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<element>Text</element>";
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Element);
        Element element = (Element) result.get(0);
        assertEquals("element", element.nodeName());
        assertEquals("Text", element.text());
    }
}