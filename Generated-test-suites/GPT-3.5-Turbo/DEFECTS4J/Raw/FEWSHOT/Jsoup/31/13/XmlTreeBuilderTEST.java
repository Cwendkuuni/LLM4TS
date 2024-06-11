package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before; 
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("input", "baseUri", new ParseErrorList());
        // Test whether the document is placed on the stack
        assertEquals("input", builder.stack.get(0));
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token(Token.TokenType.StartTag, "tagName");
        boolean result = builder.process(startTag);
        // Test whether the start tag was inserted successfully
        assertTrue(result);
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag"), "baseUri", new Attributes());
        builder.insertNode(element);
        // Test whether the element is appended to the current element
        assertEquals(element, builder.currentElement().children().get(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tagName", new Attributes(), false);
        Element element = builder.insert(startTag);
        // Test whether the start tag is inserted and added to the stack
        assertNotNull(element);
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testInsertSelfClosingStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tagName", new Attributes(), true);
        Element element = builder.insert(startTag);
        // Test whether the self-closing start tag is acknowledged and added to the stack
        assertNotNull(element);
        assertTrue(startTag.isSelfClosing());
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("commentData");
        builder.insert(comment);
        // Test whether the comment is inserted as a child of the current element
        assertEquals(1, builder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("characterData");
        builder.insert(character);
        // Test whether the character is inserted as a child of the current element
        assertEquals(1, builder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("name", "public", "system");
        builder.insert(doctype);
        // Test whether the doctype is inserted as a child of the current element
        assertEquals(1, builder.currentElement().childNodeSize());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tagName");
        builder.popStackToClose(endTag);
        // Test whether the stack is properly traversed and elements removed when closing a tag
        assertEquals(0, builder.stack.size());
    }
}