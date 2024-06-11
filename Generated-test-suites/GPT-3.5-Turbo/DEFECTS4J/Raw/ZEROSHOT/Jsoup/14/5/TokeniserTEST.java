package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(null);
    }

    @Test
    public void read_shouldReturnToken() {
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void emit_shouldSetEmitPendingToTrue() {
        tokeniser.emit(new Token.Character(""));
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void emit_shouldSetEmitPendingToFalseIfCharBufferIsEmpty() {
        tokeniser.emit(new Token.Character(""));
        tokeniser.emit("");
        assertFalse(tokeniser.isEmitPending);
    }

    @Test
    public void getState_shouldReturnTokeniserState() {
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
    }

    @Test
    public void transition_shouldSetTokeniserState() {
        TokeniserState state = TokeniserState.Doctype;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void advanceTransition_shouldAdvanceAndSetTokeniserState() {
        TokeniserState state = TokeniserState.BeforeDoctypeName;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void acknowledgeSelfClosingFlag_shouldSetSelfClosingFlagAcknowledgedToTrue() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void consumeCharacterReference_shouldReturnCharacter() {
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNotNull(character);
    }

    @Test
    public void createTagPending_shouldCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tag);
    }

    @Test
    public void emitTagPending_shouldEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void createCommentPending_shouldCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void emitCommentPending_shouldEmitCommentPending() {
        tokeniser.commentPending = new Token.Comment();
        tokeniser.emitCommentPending();
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void createDoctypePending_shouldCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void emitDoctypePending_shouldEmitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.emitDoctypePending();
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void createTempBuffer_shouldCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void isAppropriateEndTagToken_shouldReturnTrueIfTagPendingTagNameEqualsLastStartTagName() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = tokeniser.tagPending;
        boolean result = tokeniser.isAppropriateEndTagToken();
        assertTrue(result);
    }

    @Test
    public void isTrackErrors_shouldReturnTrueByDefault() {
        boolean result = tokeniser.isTrackErrors();
        assertTrue(result);
    }

    @Test
    public void setTrackErrors_shouldSetTrackErrors() {
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        assertFalse(tokeniser.isTrackErrors());
    }
}