package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null);
    }

    @Test
    public void testRead() {
        assertNull(tokeniser.read());
    }

    @Test
    public void testEmit() {
        tokeniser.emit(new Token.Character("test"));
        assertEquals(Token.TokenType.Character, tokeniser.read().type);
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("test");
        assertEquals(Token.TokenType.Character, tokeniser.read().type);
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit('t');
        assertEquals(Token.TokenType.Character, tokeniser.read().type);
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Rcdata);
        assertEquals(TokeniserState.Rcdata, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.Rcdata);
        assertEquals(TokeniserState.Rcdata, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        assertNull(tokeniser.consumeCharacterReference(null, true));
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertNotNull(tokeniser.tagPending);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(Token.TokenType.StartTag, tokeniser.read().type);
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
        assertEquals(Token.TokenType.Comment, tokeniser.read().type);
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
        assertEquals(Token.TokenType.Doctype, tokeniser.read().type);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = new Token.StartTag();
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }
}