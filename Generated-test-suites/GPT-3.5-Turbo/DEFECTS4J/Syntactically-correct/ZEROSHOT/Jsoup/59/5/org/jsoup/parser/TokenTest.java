package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void testDoctype_Reset() {
        Token.Doctype result = doctype.reset();
        
        assertNull(result.getName());
        assertNull(result.getPubSysKey());
        assertEquals("", result.getPublicIdentifier());
        assertEquals("", result.getSystemIdentifier());
        assertFalse(result.isForceQuirks());
    }

    @Test
    public void testStartTag_Reset() {
        Token.StartTag result = startTag.reset();
        
        assertNull(result.tagName);
        assertNull(result.normalName);
        assertNull(result.pendingAttributeName);
        assertEquals("", result.pendingAttributeValue.toString());
        assertNull(result.pendingAttributeValueS);
        assertFalse(result.hasEmptyAttributeValue);
        assertFalse(result.hasPendingAttributeValue);
        assertFalse(result.selfClosing);
        assertNull(result.attributes);
    }

    @Test
    public void testEndTag_Reset() {
        Token.EndTag result = endTag.reset();
        
        assertNull(result.tagName);
        assertNull(result.normalName);
        assertNull(result.pendingAttributeName);
        assertEquals("", result.pendingAttributeValue.toString());
        assertNull(result.pendingAttributeValueS);
        assertFalse(result.hasEmptyAttributeValue);
        assertFalse(result.hasPendingAttributeValue);
        assertFalse(result.selfClosing);
        assertNull(result.attributes);
    }

    @Test
    public void testComment_Reset() {
        Token.Comment result = comment.reset();
        
        assertEquals("", result.data.toString());
        assertFalse(result.bogus);
    }

    @Test
    public void testCharacter_Reset() {
        Token.Character result = character.reset();
        
        assertNull(result.data);
    }

    @Test
    public void testEOF_Reset() {
        Token.EOF result = eof.reset();
        

    }

    @Test
    public void testToken_isDoctype() {
        boolean result = doctype.isDoctype();
        
        assertTrue(result);
    }

    @Test
    public void testToken_asDoctype() {
        Token.Doctype result = doctype.asDoctype();

    }

    @Test
    public void testToken_isStartTag() {
        boolean result = startTag.isStartTag();
        
        assertTrue(result);
    }

    @Test
    public void testToken_asStartTag() {
        Token.StartTag result = startTag.asStartTag();

    }

    @Test
    public void testToken_isEndTag() {
        boolean result = endTag.isEndTag();
        
        assertTrue(result);
    }

    @Test
    public void testToken_asEndTag() {
        Token.EndTag result = endTag.asEndTag();

    }

    @Test
    public void testToken_isComment() {
        boolean result = comment.isComment();
        
        assertTrue(result);
    }

    @Test
    public void testToken_asComment() {
        Token.Comment result = comment.asComment();

    }

    @Test
    public void testToken_isCharacter() {
        boolean result = character.isCharacter();
        
        assertTrue(result);
    }

    @Test
    public void testToken_asCharacter() {
        Token.Character result = character.asCharacter();

    }

    @Test
    public void testToken_isEOF() {
        boolean result = eof.isEOF();
        
        assertTrue(result);
    }

    @Test
    public void testToken_TokenType() {
        Token.TokenType result = Token.TokenType.Doctype;
        

        result = Token.TokenType.StartTag;
        

        result = Token.TokenType.EndTag;
        

        result = Token.TokenType.Comment;
        

        result = Token.TokenType.Character;
        

        result = Token.TokenType.EOF;
        

    }
}