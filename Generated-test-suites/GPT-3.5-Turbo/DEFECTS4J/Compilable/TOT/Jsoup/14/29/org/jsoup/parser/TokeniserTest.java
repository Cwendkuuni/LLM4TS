package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        // Typical use case
        Token expectedToken = new Token.Character("test");
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        Token actualToken = tokeniser.read();
        assertEquals(expectedToken, actualToken);
        
        // Edge case: Empty input
        expectedToken = null;
        tokeniser = new Tokeniser(new CharacterReader(""));
        actualToken = tokeniser.read();
        assertEquals(expectedToken, actualToken);
    }
    
    @Test
    public void testEmit() {
        // Typical use case
        Token expectedToken = new Token.StartTag();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.emit(expectedToken);
        Token actualToken = tokeniser.read();
        assertEquals(expectedToken, actualToken);
        
        // Edge case: Emit empty token
        expectedToken = null;
        tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.emit("");
        actualToken = tokeniser.read();
        assertEquals(expectedToken, actualToken);
    }
    
    @Test
    public void testEmitString() {
        // Typical use case
        Token expectedToken = new Token.Character("test");
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.emit("test");
        Token actualToken = tokeniser.read();
        assertEquals(expectedToken, actualToken);
        
        // Edge case: Empty string
        expectedToken = null;
        tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.emit("");
        actualToken = tokeniser.read();
        assertEquals(expectedToken, actualToken);
    }
    
    // and so on for other methods of Tokeniser
    
}