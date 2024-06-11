package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private CharacterReader reader;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @After
    public void tearDown() {
        tokeniser = null;
        reader = null;
        errors = null;
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals(new Token.Character(str), tokeniser.read());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
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
        tokeniser.advanceTransition(TokeniserState.ScriptData);
        assertEquals(TokeniserState.ScriptData, tokeniser.getState());
        assertEquals(1, reader.pos());
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
        assertNull(tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute));
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        assertEquals(tokeniser.tagPending, tokeniser.read());
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
        assertEquals(tokeniser.commentPending, tokeniser.read());
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
        assertEquals(tokeniser.doctypePending, tokeniser.read());
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
        tokeniser.tagPending.tagName = tokeniser.lastStartTag.tagName;
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "tag";
        assertEquals("tag", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
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
    public void testErrorWithMessage() {
        String errorMsg = "error message";
        tokeniser.error(errorMsg);
        assertEquals(1, errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}