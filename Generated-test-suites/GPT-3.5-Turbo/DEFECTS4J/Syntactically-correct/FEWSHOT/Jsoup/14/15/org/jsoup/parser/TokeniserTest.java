package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(null);
        assertEquals(null, tokeniser.read());
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.emit(null);
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(null);
        assertNull(tokeniser.consumeCharacterReference(null, false));
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null);
        assertNull(tokeniser.createTagPending(true));
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.emitTagPending();
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.createCommentPending();
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.emitCommentPending();
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.createDoctypePending();
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.emitDoctypePending();
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.createTempBuffer();
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null);
        assertEquals(false, tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(null);
        assertEquals(true, tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.setTrackErrors(true);
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.error(TokeniserState.Data);
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.eofError(TokeniserState.Data);
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.characterReferenceError();
    }

    @Test
    public void testErrorString() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.error("Invalid character reference");
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(null);
        assertEquals(true, tokeniser.currentNodeInHtmlNS());
    }
}