package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_data() {
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals("", ((Token.Character) token).getData());
    }

    @Test
    public void testRead_emitPending() {
        TokeniserState state = tokeniser.getState();
        tokeniser.transition(TokeniserState.TagOpen);
        TokeniserState newState = tokeniser.getState();
        assertNotEquals(state, newState);

        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(newState, tokeniser.getState());
        assertTrue(tokeniser.isEmitPending());
    }

    @Test
    public void testEmit() {
        TokeniserState state = tokeniser.getState();
        tokeniser.emit(new Token.StartTag());
        TokeniserState newState = tokeniser.getState();
        assertEquals(state, newState);
        assertTrue(tokeniser.isEmitPending());
    }

    @Test
    public void testEmit_String() {
        TokeniserState state = tokeniser.getState();
        tokeniser.emit("data");
        TokeniserState newState = tokeniser.getState();
        assertEquals(state, newState);
        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getCharsString() != null);
    }

    @Test
    public void testEmit_charArray() {
        TokeniserState state = tokeniser.getState();
        tokeniser.emit(new char[]{'d', 'a', 't', 'a'});
        TokeniserState newState = tokeniser.getState();
        assertEquals(state, newState);
        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getCharsString() != null);
    }

    @Test
    public void testEmit_charCodepoints() {
        TokeniserState state = tokeniser.getState();
        tokeniser.emit(new int[]{'d', 'a', 't', 'a'});
        TokeniserState newState = tokeniser.getState();
        assertEquals(state, newState);
        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getCharsString() != null);
    }

    @Test
    public void testEmit_char() {
        TokeniserState state = tokeniser.getState();
        tokeniser.emit('d');
        TokeniserState newState = tokeniser.getState();
        assertEquals(state, newState);
        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getCharsString() != null);
    }

    @Test
    public void testEmitTagPending() {
        TokeniserState state = tokeniser.getState();
        tokeniser.createTagPending(true);
        Token.Tag tagPending = tokeniser.getTagPending();
        tagPending.start();
        tokeniser.emitTagPending();
        TokeniserState newState = tokeniser.getState();
        assertEquals(state, newState);
        assertTrue(tokeniser.isEmitPending());
    }

    @Test
    public void testCreateTagPending_start() {
        TokeniserState state = tokeniser.getState();
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertEquals(state, tokeniser.getState());
        assertNotNull(tagPending);
        assertTrue(tagPending instanceof Token.StartTag);
        assertTrue(tokeniser.getTagPending() instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_end() {
        TokeniserState state = tokeniser.getState();
        Token.Tag tagPending = tokeniser.createTagPending(false);
        assertEquals(state, tokeniser.getState());
        assertNotNull(tagPending);
        assertTrue(tagPending instanceof Token.EndTag);
        assertTrue(tokeniser.getTagPending() instanceof Token.EndTag);
    }

    @Test
    public void testEmitCommentPending() {
        TokeniserState state = tokeniser.getState();
        tokeniser.createCommentPending();
        Token.Comment commentPending = tokeniser.getCommentPending();
        tokeniser.emitCommentPending();
        TokeniserState newState = tokeniser.getState();
        assertEquals(state, newState);
        assertTrue(tokeniser.isEmitPending());
    }

    @Test
    public void testCreateCommentPending() {
        TokeniserState state = tokeniser.getState();
        tokeniser.createCommentPending();
        Token.Comment commentPending = tokeniser.getCommentPending();
        assertEquals(state, tokeniser.getState());
        assertNotNull(commentPending);
    }

    @Test
    public void testEmitDoctypePending() {
        TokeniserState state = tokeniser.getState();
        tokeniser.createDoctypePending();
        Token.Doctype doctypePending = tokeniser.getDoctypePending();
        tokeniser.emitDoctypePending();
        TokeniserState newState = tokeniser.getState();
        assertEquals(state, newState);
        assertTrue(tokeniser.isEmitPending());
    }

    @Test
    public void testCreateDoctypePending() {
        TokeniserState state = tokeniser.getState();
        tokeniser.createDoctypePending();
        Token.Doctype doctypePending = tokeniser.getDoctypePending();
        assertEquals(state, tokeniser.getState());
        assertNotNull(doctypePending);
    }

    @Test
    public void testCreateTempBuffer() {
        TokeniserState state = tokeniser.getState();
        tokeniser.createTempBuffer();
        TokeniserState newState = tokeniser.getState();
        assertEquals(state, newState);
        assertNotNull(tokeniser.getDataBuffer());
    }

    @Test
    public void testIsAppropriateEndTagToken_notSet() {
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_true() {
        String lastStartTag = "startTag";
        tokeniser.setLastStartTag(lastStartTag);
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName_notSet() {
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test
    public void testAppropriateEndTagName() {
        String lastStartTag = "startTag";
        tokeniser.setLastStartTag(lastStartTag);
        assertEquals(lastStartTag, tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        TokeniserState state = tokeniser.getState();
        tokeniser.error(TokeniserState.TagOpen);
        assertTrue(tokeniser.getErrors().size() > 0);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testEofError() {
        TokeniserState state = tokeniser.getState();
        tokeniser.eofError(TokeniserState.TagOpen);
        assertTrue(tokeniser.getErrors().size() > 0);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testCharacterReferenceError() {
        TokeniserState state = tokeniser.getState();
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        assertTrue(tokeniser.getErrors().size() > 0);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testError_customMessage() {
        TokeniserState state = tokeniser.getState();
        String errorMsg = "Custom error message";
        tokeniser.error(errorMsg);
        assertTrue(tokeniser.getErrors().size() > 0);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}