package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag("div", null);
        tokeniser.emit(token);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "Hello World";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        assertEquals(String.valueOf(c), tokeniser.charBuffer.toString());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
        reader.advance();
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        assertNull(tokeniser.consumeCharacterReference(null, false));
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
        tokeniser.emitTagPending();
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
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
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
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
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.tagPending;
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.tagPending;
        String tagName = tokeniser.appropriateEndTagName();
        assertEquals(tokeniser.lastStartTag.tagName, tagName);
    }

    @Test
    public void testErrorWithState() {
        TokeniserState state = TokeniserState.AttributeValue_unquoted;
        tokeniser.error(state);
        ParseError error = errors.get(0);
        assertNotNull(error);
        assertEquals(reader.pos(), error.getPos());
        String expectedErrorMsg = String.format("Unexpected character '%s' in input state [%s]", reader.current(), state);
        assertEquals(expectedErrorMsg, error.getErrorMsg());
    }

    @Test
    public void testEOFErrorWithState() {
        TokeniserState state = TokeniserState.AttributeValue_unquoted;
        tokeniser.eofError(state);
        ParseError error = errors.get(0);
        assertNotNull(error);
        assertEquals(reader.pos(), error.getPos());
        String expectedErrorMsg = String.format("Unexpectedly reached end of file (EOF) in input state [%s]", state);
        assertEquals(expectedErrorMsg, error.getErrorMsg());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        ParseError error = errors.get(0);
        assertNotNull(error);
        assertEquals(reader.pos(), error.getPos());
        assertEquals("Invalid character reference: " + message, error.getErrorMsg());
    }

    @Test
    public void testError() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        ParseError error = errors.get(0);
        assertNotNull(error);
        assertEquals(reader.pos(), error.getPos());
        assertEquals(errorMsg, error.getErrorMsg());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}