package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        // Typical use case
        Token token = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, token.type);
        
        // Edge case
        token = new Token.StartTag();
        assertEquals(Token.TokenType.StartTag, token.type);
        
        // Error scenario
        token = new Token.EndTag();
        assertNotEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testResetMethod() {
        // Typical use case
        Token.Doctype doctype = new Token.Doctype();
        doctype.reset();
        assertEquals("", doctype.getName());
        assertFalse(doctype.isForceQuirks());
        
        // Edge case
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("div", null);
        startTag.reset();
        assertNull(startTag.getAttributes());
        
        // Error scenario
        Token.Character character = new Token.Character();
        character.data("Hello World");
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testGetNameMethod() {
        // Typical use case
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
        
        // Edge case
        doctype.name.delete(0, doctype.name.length());
        assertEquals("", doctype.getName());
        
        // Error scenario
        doctype.name.append("");
        assertEquals("", doctype.getName());
    }

    // Add more test cases for other methods as needed
    
}