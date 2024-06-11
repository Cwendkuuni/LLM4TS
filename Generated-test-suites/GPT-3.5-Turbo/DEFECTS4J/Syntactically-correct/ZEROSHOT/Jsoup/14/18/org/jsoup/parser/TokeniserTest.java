package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token.Character character = new Token.Character("test");
        tokeniser.emit(character);
        Token token = tokeniser.read();
        assertEquals(character, token);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        assertEquals("", token);
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        assertEquals("t", token);
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        TokeniserState newState = tokeniser.getState();
        assertEquals(state, newState);
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        TokeniserState newState = tokeniser.getState();
        assertEquals(state, newState);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        Character character = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        boolean appropriate = tokeniser.isAppropriateEndTagToken();
        assertTrue(appropriate);
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = tokeniser.isTrackErrors();
        assertTrue(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        boolean trackErrors = true;
        tokeniser.setTrackErrors(trackErrors);
        assertEquals(trackErrors, tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Unexpected character in input";
        tokeniser.error(errorMsg);
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS();
        assertTrue(inHtmlNS);
    }
}