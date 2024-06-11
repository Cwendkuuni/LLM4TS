package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.*;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
        
        // Verify stack contains document node
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(Document.class, xmlTreeBuilder.stack.peek().getClass());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Verify element is inserted into currentElement
        assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
        
        // Verify stack contains element if not self-closing tag
        if (!startTag.isSelfClosing()) {
            assertEquals(element, xmlTreeBuilder.stack.peek());
        }
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        
        xmlTreeBuilder.stack.push(new Element("tag", ""));
        xmlTreeBuilder.stack.push(new Element("other", ""));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Verify stack is correctly modified
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("other", xmlTreeBuilder.stack.peek().nodeName());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        
        xmlTreeBuilder.insert(comment);
        
        // Verify comment node is inserted into currentElement
        assertEquals(Comment.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
        assertEquals("comment", ((Comment)xmlTreeBuilder.currentElement().childNode(0)).getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("character");
        
        xmlTreeBuilder.insert(character);
        
        // Verify text node is inserted into currentElement
        assertEquals(TextNode.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
        assertEquals("character", ((TextNode)xmlTreeBuilder.currentElement().childNode(0)).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("name", "public", "system");
        
        xmlTreeBuilder.insert(doctype);
        
        // Verify document type node is inserted into currentElement
        assertEquals(DocumentType.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
    }
}