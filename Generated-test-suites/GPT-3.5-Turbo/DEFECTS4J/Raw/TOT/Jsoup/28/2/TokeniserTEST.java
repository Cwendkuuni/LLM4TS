package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = mock(CharacterReader.class);
    }

    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(reader, null);
        
        // Test reading a character token
        Token.Character expectedCharToken = new Token.Character("Hello World");
        tokeniser.emit("Hello World");
        Token actualCharToken = tokeniser.read();
        assertEquals(expectedCharToken, actualCharToken);
        
        // Test reading an emitPending token
        Token.EndTag expectedEndTagToken = new Token.EndTag();
        tokeniser.emit(expectedEndTagToken);
        Token actualEndTagToken = tokeniser.read();
        assertEquals(expectedEndTagToken, actualEndTagToken);
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(reader, null);
        
        // Test emitting a token
        Token.StartTag expectedStartTagToken = new Token.StartTag();
        tokeniser.emit(expectedStartTagToken);
        Token actualStartTagToken = tokeniser.read();
        assertEquals(expectedStartTagToken, actualStartTagToken);
    }

    @Test
    public void testEmitString() {
        Tokeniser tokeniser = new Tokeniser(reader, null);
        
        // Test emitting a string
        String expectedString = "Hello World";
        tokeniser.emit(expectedString);
        Token actualToken = tokeniser.read();
        assertNotNull(actualToken);
        assertEquals(expectedString, ((Token.Character) actualToken).getData());
    }

    @Test
    public void testEmitChar() {
        Tokeniser tokeniser = new Tokeniser(reader, null);
        
        // Test emitting a single character
        char expectedChar = 'H';
        tokeniser.emit(expectedChar);
        Token actualToken = tokeniser.read();
        assertNotNull(actualToken);
        assertEquals(expectedChar, ((Token.Character) actualToken).getData().charAt(0));
    }

    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(reader, null);
        
        // Test transitioning to a new state
        TokeniserState expectedState = TokeniserState.Data;
        tokeniser.transition(expectedState);
        assertEquals(expectedState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        Tokeniser tokeniser = new Tokeniser(reader, null);
        
        // Test advancing to a new state after consuming a character
        TokeniserState expectedState = TokeniserState.Data;
        tokeniser.advanceTransition(expectedState);
        verify(reader).advance();
        assertEquals(expectedState, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(reader, null);
        
        // Test acknowledging self closing flag
        tokeniser.acknowledgeSelfClosingFlag();
        assertEquals(true, tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(reader, null);
        
        // Test consuming a character reference
        char expectedChar = 'A';
        Token actualToken = tokeniser.consumeCharacterReference(null, false);
        assertNotNull(actualToken);
        assertEquals(expectedChar, ((Token.Character) actualToken).getData().charAt(0));
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(reader, null);
        
        // Test creating a tag pending token
        boolean isStartTag = true;
        Token.Tag actualToken = tokeniser.createTagPending(isStartTag);
        assertNotNull(actualToken);
        assertEquals(isStartTag, actualToken.isStartTag());
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test emitting a tag pending token
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        Token actualToken = tokeniser.read();
        assertNotNull(actualToken);
        assertEquals(Token.TokenType.StartTag, actualToken.type);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test creating a comment pending token
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test emitting a comment pending token
        tokeniser.commentPending = new Token.Comment();
        tokeniser.emitCommentPending();
        Token actualToken = tokeniser.read();
        assertNotNull(actualToken);
        assertEquals(Token.TokenType.Comment, actualToken.type);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test creating a doctype pending token
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test emitting a doctype pending token
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.emitDoctypePending();
        Token actualToken = tokeniser.read();
        assertNotNull(actualToken);
        assertEquals(Token.TokenType.Doctype, actualToken.type);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test creating a temporary buffer
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test checking if the current tag is appropriate for an end tag token
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "div";
        assertEquals(true, tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test getting the appropriate end tag name
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test throwing an error for an unexpected character
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        verify(tokeniser.errors).add(new ParseError(0, "Unexpected character '%s' in input state [%s]", null, state));
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test throwing an error for unexpected end of file
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        verify(tokeniser.errors).add(new ParseError(0, "Unexpectedly reached end of file (EOF) in input state [%s]", state));
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test throwing an error for an invalid character reference
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        verify(tokeniser.errors).add(new ParseError(0, "Invalid character reference: %s", message));
    }

    @Test
    public void testErrorWithString() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test throwing an error with a custom message
        String errorMsg = "Custom error message";
        tokeniser.error(errorMsg);
        verify(tokeniser.errors).add(new ParseError(0, errorMsg));
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(reader, null);

        // Test checking if the current node is in the HTML namespace
        assertEquals(true, tokeniser.currentNodeInHtmlNS());
    }
}