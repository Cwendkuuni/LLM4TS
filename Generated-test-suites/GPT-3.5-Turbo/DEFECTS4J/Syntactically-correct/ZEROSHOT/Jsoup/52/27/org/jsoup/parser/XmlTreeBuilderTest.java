package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        // Test case 1
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Assertions
        assertEquals(baseUri, xmlTreeBuilder.baseUri());
        assertNotNull(xmlTreeBuilder.stack);
        assertFalse(xmlTreeBuilder.stack.isEmpty());
        
        Document doc = xmlTreeBuilder.doc;
        assertNotNull(doc);
        assertEquals(1, doc.childNodeSize());
        
        Document.OutputSettings syntax = doc.outputSettings().syntax();
        assertEquals(Document.OutputSettings.Syntax.xml, syntax);
        
        // Test case 2
        input = "";
        baseUri = "http://example.com";
        errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Assertions
        assertEquals(baseUri, xmlTreeBuilder.baseUri());
        assertNotNull(xmlTreeBuilder.stack);
        assertFalse(xmlTreeBuilder.stack.isEmpty());
        
        doc = xmlTreeBuilder.doc;
        assertNotNull(doc);
        assertEquals(1, doc.childNodeSize());
        
        syntax = doc.outputSettings().syntax();
        assertEquals(Document.OutputSettings.Syntax.xml, syntax);
    }
    
    @Test
    public void testProcess() {
        // Test case 1
        Token token = new Token(Token.TokenType.StartTag, "root");
        boolean result = xmlTreeBuilder.process(token);
        
        // Assertions
        assertTrue(result);
        assertNotNull(xmlTreeBuilder.stack);
        assertFalse(xmlTreeBuilder.stack.isEmpty());
        
        Document doc = xmlTreeBuilder.doc;
        assertNotNull(doc);
        assertEquals(2, doc.childNodeSize());
        
        Element topElement = xmlTreeBuilder.stack.peek();
        assertNotNull(topElement);
        assertEquals("root", topElement.nodeName());
        
        // Test case 2
        token = new Token(Token.TokenType.EndTag, "root");
        result = xmlTreeBuilder.process(token);
        
        // Assertions
        assertTrue(result);
        assertNotNull(xmlTreeBuilder.stack);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
        
        doc = xmlTreeBuilder.doc;
        assertNotNull(doc);
        assertEquals(2, doc.childNodeSize());
        
        topElement = xmlTreeBuilder.stack.peek();
        assertNull(topElement);

        // Test case 3
        token = new Token(Token.TokenType.Comment, "sample comment");
        result = xmlTreeBuilder.process(token);
        
        // Assertions
        assertTrue(result);
        assertNotNull(xmlTreeBuilder.stack);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
        
        doc = xmlTreeBuilder.doc;
        assertNotNull(doc);
        assertEquals(3, doc.childNodeSize());
        
        Node childNode = doc.childNode(2);
        assertNotNull(childNode);
        assertTrue(childNode instanceof Comment);
        Comment comment = (Comment) childNode;
        assertEquals("sample comment", comment.getData());
        
        // Test case 4
        token = new Token(Token.TokenType.Character, "sample character");
        result = xmlTreeBuilder.process(token);
        
        // Assertions
        assertTrue(result);
        assertNotNull(xmlTreeBuilder.stack);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
        
        doc = xmlTreeBuilder.doc;
        assertNotNull(doc);
        assertEquals(4, doc.childNodeSize());
        
        childNode = doc.childNode(3);
        assertNotNull(childNode);
        assertTrue(childNode instanceof TextNode);
        TextNode textNode = (TextNode) childNode;
        assertEquals("sample character", textNode.getWholeText());
        
        // Test case 5
        token = new Token(Token.TokenType.Doctype, "sample doctype");
        result = xmlTreeBuilder.process(token);
        
        // Assertions
        assertTrue(result);
        assertNotNull(xmlTreeBuilder.stack);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
        
        doc = xmlTreeBuilder.doc;
        assertNotNull(doc);
        assertEquals(5, doc.childNodeSize());
        
        childNode = doc.childNode(4);
        assertNotNull(childNode);
        assertTrue(childNode instanceof DocumentType);
        DocumentType documentType = (DocumentType) childNode;
        assertEquals("sample doctype", documentType.getName());
    }
    
    @Test
    public void testInsertNode() {
        // Test case 1
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        
        xmlTreeBuilder.insertNode(element);
        
        // Assertions
        Element topElement = xmlTreeBuilder.stack.peek();
        assertNotNull(topElement);
        assertEquals("div", topElement.nodeName());
        
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertFalse(childNodes.isEmpty());
        assertEquals(1, childNodes.size());
        assertSame(element, childNodes.get(0));
        
        // Test case 2
        element = new Element("p");
        
        xmlTreeBuilder.insertNode(element);
        
        // Assertions
        topElement = xmlTreeBuilder.stack.peek();
        assertNotNull(topElement);
        assertEquals("p", topElement.nodeName());
        
        childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertFalse(childNodes.isEmpty());
        assertEquals(1, childNodes.size());
        assertSame(element, childNodes.get(0));
        
        Element parentElement = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 2);
        assertNotNull(parentElement);
        assertEquals("div", parentElement.nodeName());
    }
    
    @Test
    public void testInsertStartTag() {
        // Test case 1
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("class", "sample");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Assertions
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
        
        Attributes attributes = element.attributes();
        assertNotNull(attributes);
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("class"));
        assertEquals("sample", attributes.get("class"));
        
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertFalse(childNodes.isEmpty());
        assertEquals(1, childNodes.size());
        assertSame(element, childNodes.get(0));
        
        // Test case 2
        startTag = new Token.StartTag("hr");
        
        element = xmlTreeBuilder.insert(startTag);
        
        // Assertions
        assertNotNull(element);
        assertEquals("hr", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
        
        attributes = element.attributes();
        assertNotNull(attributes);
        assertTrue(attributes.isEmpty());
        
        childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertFalse(childNodes.isEmpty());
        assertEquals(2, childNodes.size());
        assertSame(element, childNodes.get(1));
        
        Element parentElement = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 2);
        assertNotNull(parentElement);
        assertEquals("div", parentElement.nodeName());
    }
    
    @Test
    public void testInsertComment() {
        // Test case 1
        Token.Comment comment = new Token.Comment("sample comment");
        
        xmlTreeBuilder.insert(comment);
        
        // Assertions
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertFalse(childNodes.isEmpty());
        assertEquals(1, childNodes.size());
        
        Node childNode = childNodes.get(0);
        assertTrue(childNode instanceof Comment);
        Comment commentNode = (Comment) childNode;
        assertEquals("sample comment", commentNode.getData());
        
        // Test case 2
        comment = new Token.Comment("<!DOCTYPE html>");
        
        xmlTreeBuilder.insert(comment);
        
        // Assertions
        childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertFalse(childNodes.isEmpty());
        assertEquals(2, childNodes.size());
        
        childNode = childNodes.get(1);
        assertTrue(childNode instanceof XmlDeclaration);
        XmlDeclaration xmlDeclaration = (XmlDeclaration) childNode;
        assertEquals("<!DOCTYPE html>", xmlDeclaration.getData());
    }
    
    @Test
    public void testInsertCharacter() {
        // Test case 1
        Token.Character character = new Token.Character("sample character");
        
        xmlTreeBuilder.insert(character);
        
        // Assertions
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertFalse(childNodes.isEmpty());
        assertEquals(1, childNodes.size());
        
        Node childNode = childNodes.get(0);
        assertTrue(childNode instanceof TextNode);
        TextNode textNode = (TextNode) childNode;
        assertEquals("sample character", textNode.getWholeText());
        
        // Test case 2
        character = new Token.Character("\n");
        
        xmlTreeBuilder.insert(character);
        
        // Assertions
        childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertFalse(childNodes.isEmpty());
        assertEquals(2, childNodes.size());
        
        childNode = childNodes.get(1);
        assertTrue(childNode instanceof TextNode);
        textNode = (TextNode) childNode;
        assertEquals("\n", textNode.getWholeText());
    }
    
    @Test
    public void testInsertDoctype() {
        // Test case 1
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://example.com");
        
        xmlTreeBuilder.insert(doctype);
        
        // Assertions
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertFalse(childNodes.isEmpty());
        assertEquals(1, childNodes.size());
        
        Node childNode = childNodes.get(0);
        assertTrue(childNode instanceof DocumentType);
        DocumentType documentType = (DocumentType) childNode;
        assertEquals("html", documentType.getName());
        assertEquals("http://example.com", documentType.baseUri());
        
        // Test case 2
        doctype = new Token.Doctype("svg", "-//W3C//DTD SVG 1.1//EN", "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd", "");
        
        xmlTreeBuilder.insert(doctype);
        
        // Assertions
        childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertFalse(childNodes.isEmpty());
        assertEquals(2, childNodes.size());
        
        childNode = childNodes.get(1);
        assertTrue(childNode instanceof DocumentType);
        documentType = (DocumentType) childNode;
        assertEquals("svg", documentType.getName());
        assertEquals("-//W3C//DTD SVG 1.1//EN", documentType.getPublicId());
        assertEquals("http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd", documentType.getSystemId());
        assertEquals("", documentType.baseUri());
    }
    
    @Test
    public void testPopStackToClose() {
        // Test case 1
        Token.EndTag endTag = new Token.EndTag("div");
        
        xmlTreeBuilder.stack.push(new Element("p"));
        xmlTreeBuilder.stack.push(new Element("div"));
        xmlTreeBuilder.stack.push(new Element("div"));
        xmlTreeBuilder.stack.push(new Element("span"));
        xmlTreeBuilder.stack.push(new Element("div"));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assertions
        List<Element> stack = xmlTreeBuilder.stack;
        assertNotNull(stack);
        assertFalse(stack.isEmpty());
        assertEquals(2, stack.size());
        assertEquals("p", stack.get(0).nodeName());
        assertEquals("span", stack.get(1).nodeName());
        
        // Test case 2
        endTag = new Token.EndTag("span");
        
        xmlTreeBuilder.stack.clear();
        xmlTreeBuilder.stack.push(new Element("div"));
        xmlTreeBuilder.stack.push(new Element("span"));
        xmlTreeBuilder.stack.push(new Element("div"));
        xmlTreeBuilder.stack.push(new Element("p"));
        xmlTreeBuilder.stack.push(new Element("div"));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assertions
        stack = xmlTreeBuilder.stack;
        assertNotNull(stack);
        assertFalse(stack.isEmpty());
        assertEquals(3, stack.size());
        assertEquals("div", stack.get(0).nodeName());
        assertEquals("p", stack.get(1).nodeName());
        assertEquals("div", stack.get(2).nodeName());
        
        xmlTreeBuilder.stack.clear();
        xmlTreeBuilder.stack.push(new Element("div"));
        xmlTreeBuilder.stack.push(new Element("span"));
        xmlTreeBuilder.stack.push(new Element("div"));
        xmlTreeBuilder.stack.push(new Element("span"));
        xmlTreeBuilder.stack.push(new Element("div"));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assertions
        stack = xmlTreeBuilder.stack;
        assertNotNull(stack);
        assertFalse(stack.isEmpty());
        assertEquals(4, stack.size());
        assertEquals("div", stack.get(0).nodeName());
        assertEquals("div", stack.get(1).nodeName());
        assertEquals("span", stack.get(2).nodeName());
        assertEquals("div", stack.get(3).nodeName());
    }
    
    @Test
    public void testParseFragment() {
        // Test case 1
        String inputFragment = "<p>Sample paragraph</p>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        // Assertions
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
    }
}