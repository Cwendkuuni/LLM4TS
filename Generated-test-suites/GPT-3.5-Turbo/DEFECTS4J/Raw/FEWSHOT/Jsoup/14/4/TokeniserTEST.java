

package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token = new Token.Tag();
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmit_char() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        char c = 'a';
        tokeniser.emit(c);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.tagPending = new Token.Tag();
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testError() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.error(TokeniserState.Data);
        assertFalse(tokeniser.trackErrors);
    }

    @Test
    public void testEofError() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(tokeniser.trackErrors);
    }

    @Test
    public void testCharacterReferenceError() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.characterReferenceError();
        assertFalse(tokeniser.trackErrors);
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        boolean result = tokeniser.currentNodeInHtmlNS();
        assertTrue(result);
    }
}
