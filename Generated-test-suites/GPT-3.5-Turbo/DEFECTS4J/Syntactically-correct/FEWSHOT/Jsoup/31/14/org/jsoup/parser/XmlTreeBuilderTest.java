package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root></root>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        
        builder.initialiseParse(input, baseUri, errors);
        
        assertNotNull(builder.getStack());
        assertEquals(1, builder.getStack().size());
        assertEquals("http://www.example.com", builder.getBaseUri());
        assertNotNull(builder.getDoc());
        assertNotNull(builder.getTokeniser());
        assertNotNull(builder.getErrors());
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Token startTagToken = new Token(Token.TokenType.StartTag);
        startTagToken.asStartTag().name("tag");
        builder.process(startTagToken);
        
        assertEquals(1, builder.getStack().size());
        
        Token endTagToken = new Token(Token.TokenType.EndTag);
        endTagToken.asEndTag().name("tag");
        builder.process(endTagToken);
        
        assertEquals(0, builder.getStack().size());
        
        Token commentToken = new Token(Token.TokenType.Comment);
        commentToken.asComment().data("comment");
        builder.process(commentToken);
        
        assertEquals(1, builder.getDoc().childNodeSize());
        
        Token characterToken = new Token(Token.TokenType.Character);
        characterToken.asCharacter().data("character");
        builder.process(characterToken);
        
        assertEquals(2, builder.getDoc().childNodeSize());
        
        Token doctypeToken = new Token(Token.TokenType.Doctype);
        doctypeToken.asDoctype().name("doctype");
        builder.process(doctypeToken);
        
        assertEquals(3, builder.getDoc().childNodeSize());
        
        Token eofToken = new Token(Token.TokenType.EOF);
        builder.process(eofToken);
        
        assertEquals(3, builder.getDoc().childNodeSize());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Element parent = new Element(Tag.valueOf("parent"), "http://www.example.com");
        builder.getStack().add(parent);
        
        Element child = new Element(Tag.valueOf("child"), "http://www.example.com");
        builder.insertNode(child);
        
        assertEquals(1, parent.childNodes().size());
        assertEquals(child, parent.childNodes().get(0));
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        
        builder.insert(startTag);
        
        assertEquals(1, builder.getStack().size());
        assertEquals("tag", builder.getStack().get(0).nodeName());
        assertEquals("http://www.example.com", builder.getStack().get(0).baseUri());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Token.Comment comment = new Token.Comment();
        comment.data("comment");
        
        builder.insert(comment);
        
        assertEquals(1, builder.getDoc().childNodeSize());
        assertEquals("comment", builder.getDoc().childNode(0).toString());
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Token.Character character = new Token.Character();
        character.data("character");
        
        builder.insert(character);
        
        assertEquals(1, builder.getDoc().childNodeSize());
        assertEquals("character", builder.getDoc().childNode(0).toString());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("doctype");
        
        builder.insert(doctype);
        
        assertEquals(1, builder.getDoc().childNodeSize());
        assertEquals("doctype", builder.getDoc().childNode(0).toString());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag");
        
        Element el1 = new Element(Tag.valueOf("tag"), "http://www.example.com");
        Element el2 = new Element(Tag.valueOf("tag"), "http://www.example.com");
        builder.getStack().add(el1);
        builder.getStack().add(el2);
        
        builder.popStackToClose(endTag);
        
        assertEquals(0, builder.getStack().size());
    }
}