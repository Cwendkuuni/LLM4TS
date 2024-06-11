package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        Token token = tokeniser.read();
        assertEquals("test", token.toString());
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertEquals("test", tokeniser.emitPending.toString());
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertTrue(tagPending instanceof Token.StartTag);

        tokeniser.createTagPending(false);
        assertTrue(tagPending instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(tagPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createCommentPending();
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createDoctypePending();
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "tag";
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "tag";
        assertTrue(tokeniser.isAppropriateEndTagToken());

        tokeniser.tagPending.tagName = "different";
        assertFalse(tokeniser.isAppropriateEndTagToken());

        tokeniser.tagPending.tagName = null;
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        assertTrue(tokeniser.isTrackErrors());
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.error(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.characterReferenceError();
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testErrorWithMsg() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.error("Error message");
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}