package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Assert statements
        // Assert that stack contains doc
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.peek());
    }
    
    @Test
    public void testProcess_startTag() {
        // Create a StartTag token
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attr", "value");
        
        // Call the process method
        boolean result = xmlTreeBuilder.process(startTag);
        
        // Assert statements
        // Assert that the result is true
        assertTrue(result);
        // Assert that the current element has been appended with the new element
        Element currentElement = xmlTreeBuilder.currentElement();
        assertEquals(1, currentElement.childNodeSize());
        assertTrue(currentElement.childNode(0) instanceof Element);
        Element newElement = (Element) currentElement.childNode(0);
        assertEquals("tag", newElement.nodeName());
        // Assert that the new element has the correct attribute
        assertTrue(newElement.hasAttr("attr"));
        assertEquals("value", newElement.attr("attr"));
        // Assert that the new element has been pushed onto the stack
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals(newElement, xmlTreeBuilder.stack.peek());
    }
    
    @Test
    public void testProcess_endTag() {
        // Create an EndTag token
        Token.EndTag endTag = new Token.EndTag("tag");
        
        // Push a new element onto the stack
        Element element = new Element("tag");
        xmlTreeBuilder.stack.push(element);
        
        // Call the process method
        boolean result = xmlTreeBuilder.process(endTag);
        
        // Assert statements
        // Assert that the result is true
        assertTrue(result);
        // Assert that the element has been popped from the stack
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testProcess_comment() {
        // Create a Comment token
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        // Call the process method
        boolean result = xmlTreeBuilder.process(commentToken);
        
        // Assert statements
        // Assert that the result is true
        assertTrue(result);
        // Assert that the current element has been appended with the new comment node
        Element currentElement = xmlTreeBuilder.currentElement();
        assertEquals(1, currentElement.childNodeSize());
        assertTrue(currentElement.childNode(0) instanceof Comment);
        Comment comment = (Comment) currentElement.childNode(0);
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testProcess_character() {
        // Create a Character token
        Token.Character characterToken = new Token.Character("This is a character");
        
        // Call the process method
        boolean result = xmlTreeBuilder.process(characterToken);
        
        // Assert statements
        // Assert that the result is true
        assertTrue(result);
        // Assert that the current element has been appended with the new text node
        Element currentElement = xmlTreeBuilder.currentElement();
        assertEquals(1, currentElement.childNodeSize());
        assertTrue(currentElement.childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) currentElement.childNode(0);
        assertEquals("This is a character", textNode.getWholeText());
    }
    
    @Test
    public void testProcess_doctype() {
        // Create a Doctype token
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");
        
        // Call the process method
        boolean result = xmlTreeBuilder.process(doctype);
        
        // Assert statements
        // Assert that the result is true
        assertTrue(result);
        // Assert that the current element has been appended with the new doctype node
        Element currentElement = xmlTreeBuilder.currentElement();
        assertEquals(1, currentElement.childNodeSize());
        assertTrue(currentElement.childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) currentElement.childNode(0);
        assertEquals("html", documentType.getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.getSystemIdentifier());
    }
    
    @Test
    public void testPopStackToClose() {
        // Push elements onto the stack
        Element element1 = new Element("tag");
        Element element2 = new Element("div");
        Element element3 = new Element("p");
        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);
        xmlTreeBuilder.stack.push(element3);
        
        // Create an EndTag token with a name that matches element2
        Token.EndTag endTag = new Token.EndTag("div");
        
        // Call the popStackToClose method
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assert statements
        // Assert that element2 is removed from the stack
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertFalse(xmlTreeBuilder.stack.contains(element2));
        // Assert that element3 is removed from the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertFalse(xmlTreeBuilder.stack.contains(element3));
    }
}