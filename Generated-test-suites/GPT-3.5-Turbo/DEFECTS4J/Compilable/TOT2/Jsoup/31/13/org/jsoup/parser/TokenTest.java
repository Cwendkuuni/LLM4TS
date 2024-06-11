package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }
    
    @Test
    public void testDoctypeName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("DOCTYPE");
        assertEquals("DOCTYPE", doctype.getName());
    }
    
    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag("div");
        assertEquals("div", startTag.name());
    }
    
    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag("div");
        assertEquals("div", endTag.name());
    }
    
    @Test
    public void testCommentData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character("abc");
        assertEquals("abc", character.getData());
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag("div");
        assertTrue(token.isStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag("div");
        assertTrue(token.isEndTag());
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("abc");
        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }
}