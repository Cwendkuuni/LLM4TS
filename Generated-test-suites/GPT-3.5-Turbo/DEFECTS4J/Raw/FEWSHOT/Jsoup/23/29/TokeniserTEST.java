package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testRead() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Test read method when there is no emit pending
        Token token = tokeniser.read();
        assertNull(token);
        
        // Test read method when there is a character buffer
        tokeniser.emit('c');
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        
        // Test read method when there is an emit pending
        tokeniser.emit(new Token.StartTag());
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
    }
    
    @Test
    public void testEmit() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Test emit method with token parameter
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.emitPending);
        
        // Test emit method with string parameter
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charBuffer.toString());
        
        // Test emit method with char parameter
        char c = 'c';
        tokeniser.emit(c);
        assertEquals(String.valueOf(c), tokeniser.charBuffer.toString());
    }
    
    @Test
    public void testGetState() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Test getState method
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }
    
    @Test
    public void testTransition() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Test transition method
        tokeniser.transition(TokeniserState.Comment);
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Comment, state);
    }
    
    @Test
    public void testAdvanceTransition() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Test advanceTransition method
        tokeniser.advanceTransition(TokeniserState.Comment);
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Comment, state);
        assertEquals(2, reader.pos());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Test acknowledgeSelfClosingFlag method
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Create tagPending and lastStartTag instances
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending.tagName = "html";
        tokeniser.lastStartTag.tagName = "html";
        
        // Test isAppropriateEndTagToken method when tagPending and lastStartTag have the same tag name
        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertTrue(isAppropriateEndTagToken);
        
        // Test isAppropriateEndTagToken method when tagPending and lastStartTag have different tag names
        tokeniser.tagPending.tagName = "head";
        isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertFalse(isAppropriateEndTagToken);
    }
    
    @Test
    public void testAppropriateEndTagName() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Set lastStartTag's tag name
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "html";
        
        // Test appropriateEndTagName method
        String appropriateEndTagName = tokeniser.appropriateEndTagName();
        assertEquals("html", appropriateEndTagName);
    }
    
    @Test
    public void testError() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Test error method
        tokeniser.error(TokeniserState.Comment);
        assertEquals(1, errors.size());
    }
    
    @Test
    public void testEOFError() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Test eofError method
        tokeniser.eofError(TokeniserState.Comment);
        assertEquals(1, errors.size());
    }
    
    @Test
    public void testCharacterReferenceError() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Test characterReferenceError method
        tokeniser.characterReferenceError("invalid character reference");
        assertEquals(1, errors.size());
    }
    
    @Test
    public void testErrorWithMessage() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Test error method with error message
        tokeniser.error("Error message");
        assertEquals(1, errors.size());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        // Create CharacterReader and ParseErrorList instances
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        // Create Tokeniser instance
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        // Test currentNodeInHtmlNS method
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}