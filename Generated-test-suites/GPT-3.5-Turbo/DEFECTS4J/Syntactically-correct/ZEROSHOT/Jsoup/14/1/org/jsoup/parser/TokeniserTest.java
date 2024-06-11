package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

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
        Token.Tag tag = tokeniser.createTagPending(false);
        tokeniser.emit(tag);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(tag, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "test string";
        tokeniser.emit(str);
        assertTrue(tokeniser.charBuffer.length() == str.length());
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        assertEquals(1, tokeniser.charBuffer.length());
        assertEquals(c, tokeniser.charBuffer.charAt(0));
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.StartTag;
        tokeniser.transition(newState);
        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.StartTag;
        tokeniser.advanceTransition(newState);
        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        Character characterReference = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(characterReference);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tag);
        assertEquals(start, tag.start);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(tag, tokeniser.tagPending);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Token.Comment comment = new Token.Comment();
        tokeniser.commentPending = comment;
        tokeniser.emitCommentPending();
        assertEquals(comment, tokeniser.commentPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctype = new Token.Doctype();
        tokeniser.doctypePending = doctype;
        tokeniser.emitDoctypePending();
        assertEquals(doctype, tokeniser.doctypePending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.StartTag("tag");
        tokeniser.lastStartTag = new Token.StartTag("tag");
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