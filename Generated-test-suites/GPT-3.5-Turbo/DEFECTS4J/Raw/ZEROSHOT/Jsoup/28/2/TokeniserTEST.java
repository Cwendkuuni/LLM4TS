package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertEquals(null, token);
    }

    @Test
    public void testEmit() {
        Token.Character token = new Token.Character("test");
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken);
    }

    @Test
    public void testCharBuffer() {
        StringBuilder charBuffer = new StringBuilder("test");
        assertEquals(charBuffer, tokeniser.charBuffer);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token.Character token = (Token.Character) tokeniser.read();
        assertEquals(str, token.getData());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        Token.Character token = (Token.Character) tokeniser.read();
        assertEquals(Character.toString(c), token.getData());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.TagName;
        tokeniser.transition(newState);
        TokeniserState currentState = tokeniser.getState();
        assertEquals(newState, currentState);
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.TagName;
        tokeniser.advanceTransition(newState);
        TokeniserState currentState = tokeniser.getState();
        assertEquals(newState, currentState);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("\\uFFFD");
        tokeniser = new Tokeniser(reader, new ParseErrorList());
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        Character characterReference = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertEquals('\uFFFD', characterReference.charValue());
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tagPending = tokeniser.createTagPending(start);
        assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.StartTag);
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
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Comment);
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
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = new Token.StartTag("test");
        tokeniser.tagPending = new Token.EndTag("test");
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        String tagName = "test";
        tokeniser.lastStartTag = new Token.StartTag(tagName);
        String endTagName = tokeniser.appropriateEndTagName();
        assertEquals(tagName, endTagName);
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Doctype);
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference: test";
        tokeniser.characterReferenceError("test");
        assertFalse(tokeniser.errors.isEmpty());
        assertEquals(message, tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        assertFalse(tokeniser.errors.isEmpty());
        assertEquals(errorMsg, tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();
        assertTrue(currentNodeInHtmlNS);
    }

}