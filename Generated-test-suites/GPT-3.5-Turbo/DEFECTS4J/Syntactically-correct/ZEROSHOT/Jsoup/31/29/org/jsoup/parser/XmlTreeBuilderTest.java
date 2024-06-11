package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void test_initialiseParse() {
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Check if the document is added to the stack
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
    }
    
    @Test
    public void test_process_StartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        
        xmlTreeBuilder.process(startTag);
        
        // Check if the element is inserted into the tree correctly
        assertEquals("div", xmlTreeBuilder.currentElement().nodeName());
        assertEquals(xmlTreeBuilder.baseUri, xmlTreeBuilder.currentElement().baseUri());
        assertEquals(startTag.attributes, xmlTreeBuilder.currentElement().attributes());
        
        // Check if the element is added to the stack if it's not self closing
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.currentElement()));
    }
    
    @Test
    public void test_process_EndTag() {
        Element element1 = new Element(Tag.valueOf("div"), xmlTreeBuilder.baseUri);
        Element element2 = new Element(Tag.valueOf("span"), xmlTreeBuilder.baseUri);
        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);
        
        Token.EndTag endTag = new Token.EndTag("div");
        
        xmlTreeBuilder.process(endTag);
        
        // Check if the elements are removed from the stack correctly
        assertFalse(xmlTreeBuilder.stack.contains(element1));
        assertTrue(xmlTreeBuilder.stack.contains(element2));
    }
    
    @Test
    public void test_process_Comment() {
        Token.Comment commentToken = new Token.Comment("Test comment");
        
        xmlTreeBuilder.process(commentToken);
        
        // Check if the comment is inserted into the tree correctly
        assertEquals("Test comment", xmlTreeBuilder.currentElement().childNodes().get(0).getWholeText());
    }
    
    @Test
    public void test_process_Character() {
        Token.Character characterToken = new Token.Character("Test character");
        
        xmlTreeBuilder.process(characterToken);
        
        // Check if the character data is inserted into the tree correctly
        assertEquals("Test character", xmlTreeBuilder.currentElement().childNodes().get(0).getWholeText());
    }
    
    @Test
    public void test_process_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", xmlTreeBuilder.baseUri);
        
        xmlTreeBuilder.process(doctype);
        
        // Check if the doctype is inserted into the tree correctly
        assertEquals("html", xmlTreeBuilder.currentElement().childNodes().get(0).nodeName());
    }
    
    @Test(expected = AssertionError.class)
    public void test_process_InvalidToken() {
        Token invalidToken = new Token(Token.TokenType.EOF);
        
        xmlTreeBuilder.process(invalidToken);
        
        // Check if an exception is thrown for invalid token types
    }
    
    @Test
    public void test_insertNode() {
        Element element = new Element(Tag.valueOf("div"), xmlTreeBuilder.baseUri);
        xmlTreeBuilder.stack.push(element);
        Node node = new TextNode("Test", xmlTreeBuilder.baseUri);
        
        xmlTreeBuilder.insertNode(node);
        
        // Check if the node is inserted into the current element correctly
        assertEquals(node, xmlTreeBuilder.currentElement().childNodes().get(0));
    }
    
    @Test
    public void test_insert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Check if the element is inserted into the tree correctly
        assertEquals("div", element.nodeName());
        assertEquals(xmlTreeBuilder.baseUri, element.baseUri());
        assertEquals(startTag.attributes, element.attributes());
        
        // Check if the element is self-closing
        assertTrue(startTag.isSelfClosing());
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
        
        // Check if the element is not added to the stack if it's self-closing
        assertFalse(xmlTreeBuilder.stack.contains(element));
    }
    
    @Test
    public void test_insert_Comment() {
        Token.Comment commentToken = new Token.Comment("Test comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        // Check if the comment is inserted into the tree correctly
        assertEquals("Test comment", xmlTreeBuilder.currentElement().childNodes().get(0).getWholeText());
    }
    
    @Test
    public void test_insert_Character() {
        Token.Character characterToken = new Token.Character("Test character");
        
        xmlTreeBuilder.insert(characterToken);
        
        // Check if the character data is inserted into the tree correctly
        assertEquals("Test character", xmlTreeBuilder.currentElement().childNodes().get(0).getWholeText());
    }
    
    @Test
    public void test_insert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", xmlTreeBuilder.baseUri);
        
        xmlTreeBuilder.insert(doctype);
        
        // Check if the doctype is inserted into the tree correctly
        assertEquals("html", xmlTreeBuilder.currentElement().childNodes().get(0).nodeName());
    }
    
    @Test
    public void test_popStackToClose_ElementFound() {
        Element element1 = new Element(Tag.valueOf("div"), xmlTreeBuilder.baseUri);
        Element element2 = new Element(Tag.valueOf("div"), xmlTreeBuilder.baseUri);
        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);
        
        Token.EndTag endTag = new Token.EndTag("div");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Check if the first occurrence of the element is removed from the stack
        assertFalse(xmlTreeBuilder.stack.contains(element1));
        assertTrue(xmlTreeBuilder.stack.contains(element2));
    }
    
    @Test
    public void test_popStackToClose_ElementNotFound() {
        Element element1 = new Element(Tag.valueOf("div"), xmlTreeBuilder.baseUri);
        Element element2 = new Element(Tag.valueOf("span"), xmlTreeBuilder.baseUri);
        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);
        
        Token.EndTag endTag = new Token.EndTag("p");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Check if the stack remains unchanged
        assertTrue(xmlTreeBuilder.stack.contains(element1));
        assertTrue(xmlTreeBuilder.stack.contains(element2));
    }
}