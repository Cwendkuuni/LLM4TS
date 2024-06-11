package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        Token result = tokeniser.read();
        assertEquals(Token.Character.class, result.getClass());
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testAdvanceTransition() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        boolean start = true;
        Token.Tag result = tokeniser.createTagPending(start);
        assertEquals(Token.StartTag.class, result.getClass());
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.commentPending = new Token.Comment();
        tokeniser.emitCommentPending();
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.emitDoctypePending();
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = new Token.StartTag();
        boolean result = tokeniser.isAppropriateEndTagToken();
        assertTrue(result);
    }

    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.trackErrors = true;
        boolean result = tokeniser.isTrackErrors();
        assertTrue(result);
    }

    @Test
    public void testSetTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        boolean trackErrors = true;
        tokeniser.setTrackErrors(trackErrors);
        assertTrue(tokeniser.trackErrors);
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        assertFalse(tokeniser.trackErrors);
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        assertFalse(tokeniser.trackErrors);
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.trackErrors = true;
        tokeniser.characterReferenceError();
        assertFalse(tokeniser.trackErrors);
    }

    @Test
    public void testErrorWithMessage() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.trackErrors = true;
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        assertFalse(tokeniser.trackErrors);
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        boolean result = tokeniser.currentNodeInHtmlNS();
        assertTrue(result);
    }
}