
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // Test case for initialiseParse method
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Check if the document is placed onto the stack
        assertEquals(xmlTreeBuilder.stack.size(), 1);
    }

    @Test
    public void testProcess() {
        // Test case for process method
        Token token = new Token(TokenType.StartTag, "<div>");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        boolean result = xmlTreeBuilder.process(token);
        
        // Check if the element is inserted correctly
        assertTrue(result);
    }

    @Test
    public void testInsert() {
        // Test case for insert method
        Token.StartTag startTag = new Token.StartTag("div");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Check if the inserted element has the correct tag
        assertEquals(element.tagName(), "div");
    }

    @Test
    public void testInsertComment() {
        // Test case for insert method with comment token
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(commentToken);
        
        // Check if the comment is inserted correctly
        assertEquals(xmlTreeBuilder.currentElement().childNodesSize(), 1);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        // Test case for insert method with character token
        Token.Character characterToken = new Token.Character("This is a character");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(characterToken);
        
        // Check if the character node is inserted correctly
        assertEquals(xmlTreeBuilder.currentElement().childNodesSize(), 1);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        // Test case for insert method with doctype token
        Token.Doctype doctypeToken = new Token.Doctype("html");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(doctypeToken);
        
        // Check if the doctype node is inserted correctly
        assertEquals(xmlTreeBuilder.currentElement().childNodesSize(), 1);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        // Test case for popStackToClose method
        Token.EndTag endTag = new Token.EndTag("div");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.stack.add(new Element("span"));
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Check if the first occurrence of the element is removed from the stack
        assertEquals(xmlTreeBuilder.stack.size(), 1);
        assertEquals(xmlTreeBuilder.stack.get(0).tagName(), "span");
    }
}

