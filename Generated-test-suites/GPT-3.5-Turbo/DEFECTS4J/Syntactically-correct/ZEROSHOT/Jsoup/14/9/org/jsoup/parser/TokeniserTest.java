package org.jsoup.parser;

import org.jsoup.nodes.Entities;
import org.jsoup.helper.Validate;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);
        assertEquals(startTag, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertTrue(tag instanceof Token.StartTag);
        assertEquals(tag, tokeniser.tagPending);
    }

    @Test
    public void testEmitTagPending() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.tagPending = startTag;
        tokeniser.emitTagPending();
        assertEquals(startTag, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        Token.Comment comment = new Token.Comment();
        tokeniser.commentPending = comment;
        tokeniser.emitCommentPending();
        assertEquals(comment, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctype = new Token.Doctype();
        tokeniser.doctypePending = doctype;
        tokeniser.emitDoctypePending();
        assertEquals(doctype, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertTrue(tokeniser.dataBuffer instanceof StringBuilder);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Token.Tag startTag = new Token.StartTag();
        startTag.tagName = "div";
        Token.Tag endTag = new Token.EndTag();
        endTag.tagName = "div";
        tokeniser.tagPending = endTag;
        tokeniser.lastStartTag = startTag;
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = tokeniser.isTrackErrors();
        assertTrue(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        tokeniser.setTrackErrors(false);
        boolean trackErrors = tokeniser.isTrackErrors();
        assertFalse(trackErrors);
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        // Check if error is tracked
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        // Check if error is tracked
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        // Check if error is tracked
    }

    @Test
    public void testErrorWithString() {
        String errorMsg = "Error";
        tokeniser.error(errorMsg);
        // Check if error is tracked
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS();
        // Check if currentNode is in HTML namespace
    }
}