package org.jsoup.parser;

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
        String input = "<html></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Verify that the document is added to the stack
        assertEquals(1, xmlTreeBuilder.getStack().size());
        // Verify that the document is added as the first element in the stack
        assertEquals(xmlTreeBuilder.getDoc(), xmlTreeBuilder.getStack().get(0));
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Verify that the element is inserted into the document
        assertEquals(1, xmlTreeBuilder.getDoc().children().size());
        assertEquals(element, xmlTreeBuilder.getDoc().children().get(0));
        // Verify that the element is added to the stack
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(element, xmlTreeBuilder.getStack().get(0));
    }

    @Test
    public void testProcessEndTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Verify that the element is inserted into the document
        assertEquals(1, xmlTreeBuilder.getDoc().children().size());
        assertEquals(element, xmlTreeBuilder.getDoc().children().get(0));
        // Verify that the element is added to the stack
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(element, xmlTreeBuilder.getStack().get(0));
        
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Verify that the element is removed from the stack
        assertEquals(0, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("Test comment");
        xmlTreeBuilder.insert(comment);
        
        // Verify that the comment is inserted into the document
        assertEquals(1, xmlTreeBuilder.getDoc().childNodes().size());
        assertTrue(xmlTreeBuilder.getDoc().childNodes().get(0) instanceof Comment);
        Comment insertedComment = (Comment) xmlTreeBuilder.getDoc().childNodes().get(0);
        assertEquals("Test comment", insertedComment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Test character");
        xmlTreeBuilder.insert(character);
        
        // Verify that the character is inserted into the document
        assertEquals(1, xmlTreeBuilder.getDoc().childNodes().size());
        assertTrue(xmlTreeBuilder.getDoc().childNodes().get(0) instanceof TextNode);
        TextNode insertedCharacter = (TextNode) xmlTreeBuilder.getDoc().childNodes().get(0);
        assertEquals("Test character", insertedCharacter.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctype);
        
        // Verify that the doctype is inserted into the document
        assertEquals(1, xmlTreeBuilder.getDoc().childNodes().size());
        assertTrue(xmlTreeBuilder.getDoc().childNodes().get(0) instanceof DocumentType);
        DocumentType insertedDoctype = (DocumentType) xmlTreeBuilder.getDoc().childNodes().get(0);
        assertEquals("html", insertedDoctype.getName());
    }
}