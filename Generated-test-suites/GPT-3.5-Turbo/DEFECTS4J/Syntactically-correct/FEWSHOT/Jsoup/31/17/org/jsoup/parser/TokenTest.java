package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.TokenType;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.type = TokenType.Doctype;
        assertEquals(TokenType.Doctype, doctype.type);
        
        Token.StartTag startTag = new Token.StartTag();
        startTag.type = TokenType.StartTag;
        assertEquals(TokenType.StartTag, startTag.type);
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.type = TokenType.EndTag;
        assertEquals(TokenType.EndTag, endTag.type);
        
        Token.Comment comment = new Token.Comment();
        comment.type = TokenType.Comment;
        assertEquals(TokenType.Comment, comment.type);
        
        Token.Character character = new Token.Character();
        character.type = TokenType.Character;
        assertEquals(TokenType.Character, character.type);
        
        Token.EOF eof = new Token.EOF();
        eof.type = TokenType.EOF;
        assertEquals(TokenType.EOF, eof.type);
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("public");
        assertEquals("public", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");
        assertEquals("system", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "div";
        assertEquals("div", startTag.name());
        startTag.tagName = "span";
        assertEquals("span", startTag.name());
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isSelfClosing());
        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }
    
    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.getAttributes());
        startTag.attributes = new Attributes();
        assertEquals(new Attributes(), startTag.getAttributes());
    }
    
    @Test
    public void testAppendTagName() {
        Token.Tag tag = new Token.Tag();
        tag.appendTagName("div");
        assertEquals("div", tag.tagName);
        tag.appendTagName('a');
        assertEquals("diva", tag.tagName);
    }
    
    @Test
    public void testAppendAttributeName() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeName("href");
        assertEquals("href", tag.pendingAttributeName);
        tag.appendAttributeName('a');
        assertEquals("hrefa", tag.pendingAttributeName);
    }
    
    @Test
    public void testAppendAttributeValue() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeValue("https://example.com");
        assertEquals("https://example.com", tag.pendingAttributeValue.toString());
        tag.appendAttributeValue('a');
        assertEquals("https://example.coma", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        assertEquals("div", endTag.name());
        endTag.tagName = "span";
        assertEquals("span", endTag.name());
    }
    
    @Test
    public void testHaveAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new Attributes();
        assertEquals(0, startTag.attributes.size());
        startTag.newAttribute();
        assertEquals(1, startTag.attributes.size());
        startTag.newAttribute();
        assertEquals(2, startTag.attributes.size());
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("This is some text");
        assertEquals("This is some text", character.getData());
    }
    
    @Test
    public void testDoctypeIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
        Token token = new Token();
        assertFalse(token.isDoctype());
    }
    
    @Test
    public void testStartTagIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());
        Token token = new Token();
        assertFalse(token.isStartTag());
    }
    
    @Test
    public void testEndTagIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());
        Token token = new Token();
        assertFalse(token.isEndTag());
    }
    
    @Test
    public void testCommentIsComment() {
        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());
        Token token = new Token();
        assertFalse(token.isComment());
    }
    
    @Test
    public void testCharacterIsCharacter() {
        Token.Character character = new Token.Character("This is some text");
        assertTrue(character.isCharacter());
        Token token = new Token();
        assertFalse(token.isCharacter());
    }
    
    @Test
    public void testEOFIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
        Token token = new Token();
        assertFalse(token.isEOF());
    }
}