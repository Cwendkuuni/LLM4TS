package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testTokeniserRead() {
        // Test Tokeniser read method
        // Typical use case
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<html></html>"), new ParseErrorList());
        Token token = tokeniser.read();
        
        // Verify token is not null
        assertNotNull(token);
    }

    @Test
    public void testTokeniserEmit() {
        // Test Tokeniser emit method
        // Typical use case
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<html></html>"), new ParseErrorList());
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);
        
        // Verify isEmitPending is true
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testTokeniserConsumeCharacterReference() {
        // Test Tokeniser consumeCharacterReference method
        // Edge case: empty reader
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Character character = tokeniser.consumeCharacterReference(null, false);
        
        // Verify character is null
        assertNull(character);
    }

    @Test
    public void testTokeniserCreateTagPending() {
        // Test Tokeniser createTagPending method
        // Typical use case
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<html></html>"), new ParseErrorList());
        Token.Tag tagPending = tokeniser.createTagPending(true);
        
        // Verify tagPending is not null
        assertNotNull(tagPending);
    }

    @Test
    public void testTokeniserAppropriateEndTagName() {
        // Test Tokeniser appropriateEndTagName method
        // Edge case: lastStartTag is null
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<html></html>"), new ParseErrorList());
        String endTagName = tokeniser.appropriateEndTagName();
        
        // Verify endTagName is null
        assertNull(endTagName);
    }

    @Test
    public void testTokeniserError() {
        // Test Tokeniser error method
        // Error scenario
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.error(TokeniserState.Data);
        
        // Verify error added to ParseErrorList
        assertEquals(1, tokeniser.errors.size());
    }

}