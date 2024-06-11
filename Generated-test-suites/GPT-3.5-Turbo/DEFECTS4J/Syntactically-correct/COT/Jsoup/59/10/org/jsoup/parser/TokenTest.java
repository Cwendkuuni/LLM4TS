
package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.type = Token.TokenType.Doctype;
        assertEquals(Token.TokenType.Doctype, doctype.type);
        
        Token.StartTag startTag = new Token.StartTag();
        startTag.type = Token.TokenType.StartTag;
        assertEquals(Token.TokenType.StartTag, startTag.type);
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.type = Token.TokenType.EndTag;
        assertEquals(Token.TokenType.EndTag, endTag.type);
        
        Token.Comment comment = new Token.Comment();
        comment.type = Token.TokenType.Comment;
        assertEquals(Token.TokenType.Comment, comment.type);
        
        Token.Character character = new Token.Character();
        character.type = Token.TokenType.Character;
        assertEquals(Token.TokenType.Character, character.type);
        
        Token.EOF eof = new Token.EOF();
        eof.type = Token.TokenType.EOF;
        assertEquals(Token.TokenType.EOF, eof.type);
    }
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        token.type = Token.TokenType.Doctype;
        assertTrue(token.isDoctype());
        
        token = new Token.StartTag();
        token.type = Token.TokenType.StartTag;
        assertTrue(token.isStartTag());
        
        token = new Token.EndTag();
        token.type = Token.TokenType.EndTag;
        assertTrue(token.isEndTag());
        
        token = new Token.Comment();
        token.type = Token.TokenType.Comment;
        assertTrue(token.isComment());
        
        token = new Token.Character();
        token.type = Token.TokenType.Character;
        assertTrue(token.isCharacter());
        
        token = new Token.EOF();
        token.type = Token.TokenType.EOF;
        assertTrue(token.isEOF());
    }
    
    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("doctype name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("public identifier");
        doctype.systemIdentifier.append("system identifier");
        doctype.forceQuirks = true;
        
        doctype = doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }
    
    // Repeat the reset() methods for other classes
    
    // Generate test cases for identified edge cases and exceptions
    
}

