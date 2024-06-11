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
        assertNotNull(tokeniser.read());
    }

    @Test
    public void testEmit() {
        Token.Character characterToken = new Token.Character("Test");
        tokeniser.emit(characterToken);
        assertEquals(characterToken, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String str = "Test";
        tokeniser.emit(str);
        Token.Character characterToken = new Token.Character(str);
        assertEquals(characterToken, tokeniser.read());
    }

    @Test
    public void testEmitChar() {
        char c = 'T';
        tokeniser.emit(c);
        Token.Character characterToken = new Token.Character(String.valueOf(c));
        assertEquals(characterToken, tokeniser.read());
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(tagPending, tokeniser.read());
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertNotNull(tokeniser.read());
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertNotNull(tokeniser.read());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Token.Tag startTag = tokeniser.createTagPending(true);
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;

        Token.Tag endTag = tokeniser.createTagPending(false);
        endTag.tagName = "div";

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

    @Test
    public void testErrorState() {
        tokeniser.error(TokeniserState.Data);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testEofErrorState() {
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}