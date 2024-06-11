package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        // Test the initializeParse() method
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("input", "baseUri", new ParseErrorList());
        
        // Assert that the document is added to the stack
        assertEquals(1, builder.stack.size());
        assertEquals("org.jsoup.nodes.Document", builder.stack.get(0).getClass().getName());
    }
    
    @Test
    public void testProcessStartTag() {
        // Test the process(Token.StartTag) method
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("parent"), "", new Attributes()));
        Token.StartTag startTag = new Token.StartTag("child");
        startTag.attributes.put("attr1", "value1");
        builder.process(startTag);
        
        // Assert that the child element is inserted onto the stack
        assertEquals(2, builder.stack.size());
        assertEquals("org.jsoup.nodes.Element", builder.stack.get(1).getClass().getName());
        
        // Assert that the child element has the correct tag and attributes
        Element childElement = (Element)builder.stack.get(1);
        assertEquals("child", childElement.tagName());
        assertEquals("value1", childElement.attr("attr1"));
    }
    
    @Test
    public void testProcessEndTag() {
        // Test the process(Token.EndTag) method
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("parent"), "", new Attributes()));
        builder.stack.add(new Element(Tag.valueOf("child"), "", new Attributes()));
        Token.EndTag endTag = new Token.EndTag("child");
        builder.process(endTag);
        
        // Assert that the child element is removed from the stack
        assertEquals(1, builder.stack.size());
        assertEquals("org.jsoup.nodes.Element", builder.stack.get(0).getClass().getName());
        
        // Assert that the parent element is still on the stack
        Element parentElement = (Element)builder.stack.get(0);
        assertEquals("parent", parentElement.tagName());
    }
    
    @Test
    public void testProcessComment() {
        // Test the process(Token.Comment) method
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("parent"), "", new Attributes()));
        Token.Comment commentToken = new Token.Comment("This is a comment");
        builder.process(commentToken);
        
        // Assert that the comment node is inserted onto the stack
        assertEquals(2, builder.stack.size());
        assertEquals("org.jsoup.nodes.Comment", builder.stack.get(1).getClass().getName());
        
        // Assert that the comment node has the correct data
        Comment comment = (Comment)builder.stack.get(1);
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testProcessCharacter() {
        // Test the process(Token.Character) method
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("parent"), "", new Attributes()));
        Token.Character characterToken = new Token.Character("This is some text");
        builder.process(characterToken);
        
        // Assert that the text node is inserted onto the stack
        assertEquals(2, builder.stack.size());
        assertEquals("org.jsoup.nodes.TextNode", builder.stack.get(1).getClass().getName());
        
        // Assert that the text node has the correct data
        TextNode textNode = (TextNode)builder.stack.get(1);
        assertEquals("This is some text", textNode.text());
    }
    
    @Test
    public void testProcessDoctype() {
        // Test the process(Token.Doctype) method
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("parent"), "", new Attributes()));
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "");
        builder.process(doctypeToken);
        
        // Assert that the doctype node is inserted onto the stack
        assertEquals(2, builder.stack.size());
        assertEquals("org.jsoup.nodes.DocumentType", builder.stack.get(1).getClass().getName());
        
        // Assert that the doctype node has the correct name
        DocumentType doctype = (DocumentType)builder.stack.get(1);
        assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testPopStackToClose() {
        // Test the popStackToClose(Token.EndTag) method
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element parentElement = new Element(Tag.valueOf("parent"), "", new Attributes());
        Element childElement = new Element(Tag.valueOf("child"), "", new Attributes());
        builder.stack.add(parentElement);
        builder.stack.add(childElement);
        Token.EndTag endTag = new Token.EndTag("child");
        builder.popStackToClose(endTag);
        
        // Assert that the child element is removed from the stack
        assertEquals(1, builder.stack.size());
        assertEquals("org.jsoup.nodes.Element", builder.stack.get(0).getClass().getName());
        
        // Assert that the parent element is still on the stack
        assertEquals(parentElement, builder.stack.get(0));
    }
}