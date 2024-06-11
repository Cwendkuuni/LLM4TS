package org.jsoup.parser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        // Test case 1
        CharacterReader reader1 = new CharacterReader("hello");
        Tokeniser tokeniser1 = new Tokeniser(reader1);
        Token token1 = tokeniser1.read();
        assertEquals("hello", token1.toString());

        // Test case 2
        CharacterReader reader2 = new CharacterReader("");
        Tokeniser tokeniser2 = new Tokeniser(reader2);
        Token token2 = tokeniser2.read();
        assertNull(token2);

        // Test case 3
        CharacterReader reader3 = new CharacterReader("<tag>");
        Tokeniser tokeniser3 = new Tokeniser(reader3);
        Token token3 = tokeniser3.read();
        assertEquals("<tag>", token3.toString());

        // Test case 4
        CharacterReader reader4 = new CharacterReader("hello world");
        Tokeniser tokeniser4 = new Tokeniser(reader4);
        for (int i = 0; i < 5; i++) {
            Token token4 = tokeniser4.read();
            assertNotNull(token4);
            assertEquals("hello", token4.toString());
        }
    }

    @Test
    public void testEmit() {
        // Test case 1
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""));
        Token token1 = new Token.Character("hello");
        tokeniser1.emit(token1);
        Token emittedToken1 = tokeniser1.read();
        assertEquals("hello", emittedToken1.toString());

        // Test case 2
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""));
        tokeniser2.emit("hello");
        Token emittedToken2 = tokeniser2.read();
        assertEquals("hello", emittedToken2.toString());

        // Test case 3
        Tokeniser tokeniser3 = new Tokeniser(new CharacterReader(""));
        tokeniser3.emit('h');
        Token emittedToken3 = tokeniser3.read();
        assertEquals("h", emittedToken3.toString());
    }

    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.transition(TokeniserState.Data);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.advanceTransition(TokeniserState.Data);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test case 1
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader("&amp;"));
        Character character1 = tokeniser1.consumeCharacterReference(null, false);
        assertEquals('&', character1.charValue());

        // Test case 2
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader("&#38;"));
        Character character2 = tokeniser2.consumeCharacterReference(null, false);
        assertEquals('&', character2.charValue());

        // Test case 3
        Tokeniser tokeniser3 = new Tokeniser(new CharacterReader("&nbsp;"));
        Character character3 = tokeniser3.consumeCharacterReference(null, false);
        assertEquals(' ', character3.charValue());
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertNotNull(tagPending);
        assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.getCommentPending());
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.getDoctypePending());
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.getDataBuffer());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createTagPending(true);
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        assertTrue(tokeniser.isTrackErrors());
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
        tokeniser.setTrackErrors(true);
        tokeniser.error(TokeniserState.Data);
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.setTrackErrors(true);
        tokeniser.eofError(TokeniserState.Data);
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.setTrackErrors(true);
        tokeniser.characterReferenceError();
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
    }

    @Test
    public void testError_withErrorMessage() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.setTrackErrors(true);
        tokeniser.error("Invalid character");
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}