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
        Token token = new Token.Character("");

        tokeniser.emit(token);

        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String str = "";

        tokeniser.emit(str);

        assertEquals(new Token.Character(str), tokeniser.read());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';

        tokeniser.emit(c);

        assertEquals(new Token.Character(String.valueOf(c)), tokeniser.read());
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
        tokeniser.advanceTransition(TokeniserState.CommentStart);

        assertEquals(TokeniserState.CommentStart, tokeniser.getState());
        assertEquals(1, tokeniser.reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;

        assertNull(tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute));
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;

        assertNotNull(tokeniser.createTagPending(start));
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        assertNotNull(tokeniser.createTagPending(true));

        tokeniser.emitTagPending();

        assertTrue(tokeniser.tagPending.isTag());
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
        assertNotNull(tokeniser.createTagPending(true));

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsNotAppropriateEndTagToken() {
        assertNotNull(tokeniser.createTagPending(true));
        tokeniser.tagPending.tagName = "div";

        assertFalse(tokeniser.isAppropriateEndTagToken());
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

    @Test
    public void testError() {
        tokeniser.setTrackErrors(true);

        tokeniser.error(TokeniserState.Rcdata);

        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        tokeniser.setTrackErrors(true);

        tokeniser.eofError(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.setTrackErrors(true);

        tokeniser.characterReferenceError();

        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testErrorWithMessage() {
        tokeniser.setTrackErrors(true);

        tokeniser.error("Invalid character");

        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}