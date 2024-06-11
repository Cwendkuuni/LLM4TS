package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        // Typical use case
        Token token = tokeniser.read();
        assertNull(token);
        
        // Edge case - empty buffer
        token = tokeniser.read();
        assertNull(token);
        
        // Error scenario - self closing flag not acknowledged
        tokeniser.acknowledgeSelfClosingFlag();
        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Error, token.getType());
        assertEquals("Self closing flag not acknowledged", token.getValue());
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        // Typical use case
        Token expectedToken = new Token.StartTag();
        tokeniser.emit(expectedToken);
        
        Token token = tokeniser.read();
        assertEquals(expectedToken, token);
        
        // Error scenario - emit called with unread token pending
        try {
            tokeniser.emit(new Token.EndTag());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("There is an unread token pending!", e.getMessage());
        }
    }

    @Test
    public void testEmitString() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        // Typical use case
        String str = "Test String";
        tokeniser.emit(str);
        
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals(str, token.getValue());
    }

    @Test
    public void testEmitChar() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        // Typical use case
        char c = 'a';
        tokeniser.emit(c);
        
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals(String.valueOf(c), token.getValue());
    }

    // Similarly, test cases can be written for other methods in the Tokeniser class
}