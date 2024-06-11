package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
    }

    @Test
    public void testInitialiseParse() {
        // Test if the document is added to the stack
        xmlTreeBuilder.initialiseParse("", "", null);
        assertSame(doc, xmlTreeBuilder.stack.getFirst());
    }

    @Test
    public void testProcess_StartTag() {
        // Test if the start tag is correctly inserted
        Token.StartTag token = new Token.StartTag("test", null);
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.setTokeniser(tokeniser);

        xmlTreeBuilder.process(token);

        Element insertedElement = (Element) doc.childNode(0);
        assertEquals("test", insertedElement.tagName());
    }

    @Test
    public void testProcess_EndTag() {
        // Test if the matching start tag is correctly removed from the stack
        Token.StartTag startTag = new Token.StartTag("test", null);
        Token.EndTag endTag = new Token.EndTag("test");
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.setTokeniser(tokeniser);

        xmlTreeBuilder.process(startTag);
        xmlTreeBuilder.process(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcess_Comment() {
        // Test if the comment is correctly inserted
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.setTokeniser(tokeniser);

        xmlTreeBuilder.process(commentToken);

        Node insertedNode = doc.childNode(0);
        assertTrue(insertedNode instanceof Comment);
        assertEquals("This is a comment", ((Comment) insertedNode).getData());
    }

    @Test
    public void testProcess_Character() {
        // Test if the character is correctly inserted
        Token.Character characterToken = new Token.Character("This is a character");
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.setTokeniser(tokeniser);

        xmlTreeBuilder.process(characterToken);

        Node insertedNode = doc.childNode(0);
        assertTrue(insertedNode instanceof TextNode);
        assertEquals("This is a character", ((TextNode) insertedNode).getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        // Test if the doctype is correctly inserted
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.setTokeniser(tokeniser);

        xmlTreeBuilder.process(doctype);

        Node insertedNode = doc.childNode(0);
        assertTrue(insertedNode instanceof DocumentType);
        assertEquals("html", ((DocumentType) insertedNode).getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testProcess_UnexpectedToken() {
        // Test if an exception is thrown for unexpected token types
        Token token = new Token(Token.TokenType.Text, "text");
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.setTokeniser(tokeniser);

        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsert_StartTag() {
        // Test if the element is correctly inserted and added to the stack if it's not self-closing
        Token.StartTag startTag = new Token.StartTag("test", null);
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.setTokeniser(tokeniser);

        Element insertedElement = xmlTreeBuilder.insert(startTag);

        assertEquals("test", insertedElement.tagName());
        assertTrue(xmlTreeBuilder.stack.contains(insertedElement));
    }

    @Test
    public void testInsert_SelfClosingTag() {
        // Test if the element is correctly inserted and not added to the stack if it's self-closing
        Token.StartTag startTag = new Token.StartTag("test", null);
        startTag.setSelfClosing();
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.setTokeniser(tokeniser);

        Element insertedElement = xmlTreeBuilder.insert(startTag);

        assertEquals("test", insertedElement.tagName());
        assertFalse(xmlTreeBuilder.stack.contains(insertedElement));
    }

    @Test
    public void testInsert_CommentToken() {
        // Test if the comment is correctly inserted
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.setTokeniser(tokeniser);

        xmlTreeBuilder.insert(commentToken);

        Node insertedNode = doc.childNode(0);
        assertTrue(insertedNode instanceof Comment);
        assertEquals("This is a comment", ((Comment) insertedNode).getData());
    }

    @Test
    public void testInsert_CharacterToken() {
        // Test if the character is correctly inserted
        Token.Character characterToken = new Token.Character("This is a character");
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.setTokeniser(tokeniser);

        xmlTreeBuilder.insert(characterToken);

        Node insertedNode = doc.childNode(0);
        assertTrue(insertedNode instanceof TextNode);
        assertEquals("This is a character", ((TextNode) insertedNode).getWholeText());
    }

    @Test
    public void testInsert_DoctypeToken() {
        // Test if the doctype is correctly inserted
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.setTokeniser(tokeniser);

        xmlTreeBuilder.insert(doctype);

        Node insertedNode = doc.childNode(0);
        assertTrue(insertedNode instanceof DocumentType);
        assertEquals("html", ((DocumentType) insertedNode).getName());
    }

    @Test
    public void testPopStackToClose() {
        // Test if the first occurrence of the matching start tag is correctly removed from the stack
        Token.StartTag startTag1 = new Token.StartTag("test", null);
        Token.StartTag startTag2 = new Token.StartTag("test", null);
        Token.StartTag startTag3 = new Token.StartTag("test", null);
        Token.EndTag endTag = new Token.EndTag("test");
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.setTokeniser(tokeniser);

        xmlTreeBuilder.process(startTag1);
        xmlTreeBuilder.process(startTag2);
        xmlTreeBuilder.process(startTag3);
        xmlTreeBuilder.process(endTag);

        assertFalse(xmlTreeBuilder.stack.contains(startTag2.name()));
    }
}