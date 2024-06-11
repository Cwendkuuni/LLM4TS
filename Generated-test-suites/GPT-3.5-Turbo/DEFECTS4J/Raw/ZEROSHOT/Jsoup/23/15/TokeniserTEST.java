package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
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
        assertEquals("test", tokeniser.charBuffer.toString());
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("test");
        assertEquals("test", tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit('t');
        assertEquals("t", tokeniser.charBuffer.toString());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.TagName);
        assertEquals(TokeniserState.TagName, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.TagName);
        assertEquals(TokeniserState.TagName, tokeniser.getState());
        assertEquals(1, tokeniser.reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.selfClosingFlagAcknowledged = false;
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        Character character = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.createTempBuffer();
        tokeniser.dataBuffer.append("div");
        tokeniser.tagPending.setTagName(tokeniser.dataBuffer.toString());
        tokeniser.tagPending.finaliseTag();
        tokeniser.emitTagPending();
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
        assertEquals("div", ((Token.StartTag) tokeniser.emitPending).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.setData("test comment");
        tokeniser.emitCommentPending();
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
        assertEquals("test comment", ((Token.Comment) tokeniser.emitPending).getData());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.setName("html");
        tokeniser.emitDoctypePending();
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
        assertEquals("html", ((Token.Doctype) tokeniser.emitPending).getName());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.createTempBuffer();
        tokeniser.dataBuffer.append("div");
        tokeniser.tagPending.setTagName(tokeniser.dataBuffer.toString());
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.tagPending;
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        tokeniser.createTempBuffer();
        tokeniser.dataBuffer.append("div");
        tokeniser.tagPending.setTagName(tokeniser.dataBuffer.toString());
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.tagPending;
        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError_state() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError_state() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference");
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testError_message() {
        tokeniser.error("Error message");
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}