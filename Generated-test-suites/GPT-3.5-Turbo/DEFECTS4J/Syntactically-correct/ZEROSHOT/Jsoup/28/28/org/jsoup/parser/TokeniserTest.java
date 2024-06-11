package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    
    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }
    
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
    }
    
    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }
    
    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(str, tokeniser.charBuffer.toString());
    }
    
    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(Character.toString(c), tokeniser.charBuffer.toString());
    }
    
    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
    }
    
    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }
    
    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    // Add more test methods for other methods in Tokeniser class
    
}