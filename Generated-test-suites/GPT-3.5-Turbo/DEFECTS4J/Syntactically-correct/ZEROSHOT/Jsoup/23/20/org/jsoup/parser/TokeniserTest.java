package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        // Create a Tokeniser object for testing
        CharacterReader reader = new CharacterReader(""); // Provide an input for the reader if required
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // Test the read() method
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertTrue(token instanceof Token.Character);
    }

    @Test
    public void testEmit() {
        // Test the emit() method
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmit_String() {
        // Test the emit(String str) method
        tokeniser.emit("test");
        String expected = "test";
        assertEquals(expected, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmit_char() {
        // Test the emit(char c) method
        tokeniser.emit('t');
        String expected = "t";
        assertEquals(expected, tokeniser.charBuffer.toString());
    }

    @Test
    public void testGetState() {
        // Test the getState() method
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        // Test the transition(TokeniserState state) method
        tokeniser.transition(TokeniserState.Comment);
        assertEquals(TokeniserState.Comment, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        // Test the advanceTransition(TokeniserState state) method
        tokeniser.advanceTransition(TokeniserState.TagName);
        assertEquals(TokeniserState.TagName, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test the acknowledgeSelfClosingFlag() method
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test the consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) method
        assertNull(tokeniser.consumeCharacterReference(null, false));
    }

    @Test
    public void testCreateTagPending() {
        // Test the createTagPending(boolean start) method
        tokeniser.createTagPending(true);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
        tokeniser.createTagPending(false);
        assertTrue(tokeniser.tagPending instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending() {
        // Test the emitTagPending() method
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertNotEquals(null, tokeniser.emitPending);
        assertEquals(tokeniser.tagPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        // Test the createCommentPending() method
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        // Test the emitCommentPending() method
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertNotEquals(null, tokeniser.emitPending);
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        // Test the createDoctypePending() method
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        // Test the emitDoctypePending() method
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertNotEquals(null, tokeniser.emitPending);
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        // Test the createTempBuffer() method
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test the isAppropriateEndTagToken() method
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = (Token.StartTag)tokeniser.tagPending;
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        // Test the appropriateEndTagName() method
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.tagPending;
        assertEquals(tokeniser.lastStartTag.tagName, tokeniser.appropriateEndTagName());
    }

    @Test
    public void testErrorWithState() {
        // Test the error(TokeniserState state) method
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        // Verify that the error is added to the error list
        assertTrue(tokeniser.errors.hasErrors());
        assertEquals(1, tokeniser.errors.size());
    }
    
    @Test
    public void testEofErrorWithState() {
        // Test the eofError(TokeniserState state) method
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        // Verify that the error is added to the error list
        assertTrue(tokeniser.errors.hasErrors());
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        // Test the characterReferenceError(String message) method
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        // Verify that the error is added to the error list
        assertTrue(tokeniser.errors.hasErrors());
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testError() {
        // Test the error(String errorMsg) method
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        // Verify that the error is added to the error list
        assertTrue(tokeniser.errors.hasErrors());
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test the currentNodeInHtmlNS() method
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

}