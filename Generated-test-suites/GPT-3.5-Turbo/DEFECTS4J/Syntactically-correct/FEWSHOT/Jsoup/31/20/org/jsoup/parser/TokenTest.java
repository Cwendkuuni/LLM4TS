package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testTokenGetType() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, doctype.type);

        Token.StartTag startTag = new Token.StartTag();
        assertEquals(Token.TokenType.StartTag, startTag.type);

        Token.EndTag endTag = new Token.EndTag();
        assertEquals(Token.TokenType.EndTag, endTag.type);

        Token.Comment comment = new Token.Comment();
        assertEquals(Token.TokenType.Comment, comment.type);

        Token.Character character = new Token.Character("");
        assertEquals(Token.TokenType.Character, character.type);

        Token.EOF eof = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testTokenTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Token.StartTag startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());

        Token.EndTag endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());

        Token.Comment comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        Token.Character character = new Token.Character("");
        assertEquals("Character", character.tokenType());

        Token.EOF eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }
    
    @Test
    public void testTokenIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());

        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isDoctype());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isDoctype());

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isDoctype());

        Token.Character character = new Token.Character("");
        assertFalse(character.isDoctype());

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testTokenAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals(doctype, doctype.asDoctype());

        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.asDoctype() instanceof Token.Doctype);

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.asDoctype() instanceof Token.Doctype);

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.asDoctype() instanceof Token.Doctype);

        Token.Character character = new Token.Character("");
        assertFalse(character.asDoctype() instanceof Token.Doctype);

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.asDoctype() instanceof Token.Doctype);
    }
    
    // Additional test methods for other methods in the Token class can be implemented here
}