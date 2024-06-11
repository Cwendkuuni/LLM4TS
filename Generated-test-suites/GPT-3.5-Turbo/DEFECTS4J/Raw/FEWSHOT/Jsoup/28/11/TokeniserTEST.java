package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setup() {
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(new CharacterReader(""), errors);
    }

    @Test
    public void testRead() {
        assertNull(tokeniser.read());
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmit_String() {
        tokeniser.emit("test");
        assertTrue(tokeniser.isEmitPending);
        assertEquals("test", tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmit_Char() {
        tokeniser.emit('c');
        assertTrue(tokeniser.isEmitPending);
        assertEquals('c', tokeniser.charBuffer.charAt(0));
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
        assertEquals(1, tokeniser.reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        assertNull(tokeniser.consumeCharacterReference('a', true));
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);

        tokeniser.createTagPending(false);
        assertTrue(tokeniser.tagPending instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending = new Token.Comment();
        tokeniser.emitCommentPending();
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.emitDoctypePending();
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        assertFalse(tokeniser.isAppropriateEndTagToken());

        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending = new Token.EndTag();
        assertFalse(tokeniser.isAppropriateEndTagToken());

        tokeniser.lastStartTag.tagName = "tag";
        tokeniser.tagPending.tagName = "tag";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        assertNull(tokeniser.appropriateEndTagName());

        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "tag";
        assertEquals("tag", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError_1() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errors.size());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("error");
        assertEquals(1, errors.size());
    }

    @Test
    public void testData() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        assertNull(tokeniser.read());
    }
}