package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token result = tokeniser.read();

        assertEquals(new Token.Character("t"), result);
    }

    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = new Token.EndTag();
        tokeniser.emit(token);

        assertTrue(tokeniser.isEmitPending());
        assertEquals(token, tokeniser.getEmitPending());
    }

    @Test
    public void testEmitString() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.emit("test");

        assertEquals("test", tokeniser.getCharBuffer().toString());
    }

    @Test
    public void testEmitChar() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.emit('t');

        assertEquals("t", tokeniser.getCharBuffer().toString());
    }

    @Test
    public void testGetState() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        TokeniserState state = tokeniser.getState();

        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.TagOpen);

        assertEquals(TokeniserState.TagOpen, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.advanceTransition(TokeniserState.TagOpen);

        assertEquals(TokeniserState.TagOpen, tokeniser.getState());
        assertEquals('t', reader.current());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character result = tokeniser.consumeCharacterReference(null, false);

        assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createTagPending(true);

        assertNotNull(tokeniser.getTagPending());
        assertTrue(tokeniser.getTagPending() instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getEmitPending() instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createCommentPending();

        assertNotNull(tokeniser.getCommentPending());
    }

    @Test
    public void testEmitCommentPending() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getEmitPending() instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.getDoctypePending());
    }

    @Test
    public void testEmitDoctypePending() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getEmitPending() instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.getDataBuffer());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        tokeniser.lastStartTag = startTag;
        tokeniser.createTagPending(false);

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        tokeniser.lastStartTag = startTag;
        tokeniser.createTagPending(false);

        assertEquals("test", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.error(TokeniserState.Data);

        assertEquals(1, errors.size());
    }

    @Test
    public void testEofError() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.eofError(TokeniserState.Data);

        assertEquals(1, errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.characterReferenceError("error");

        assertEquals(1, errors.size());
    }

    @Test
    public void testError2() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.error("error");

        assertEquals(1, errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue(result);
    }

    // Utility methods not tested

}