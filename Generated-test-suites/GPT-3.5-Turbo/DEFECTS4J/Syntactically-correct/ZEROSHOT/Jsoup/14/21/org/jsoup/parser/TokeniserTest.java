package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

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
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken);
    }
    
    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token emittedToken = tokeniser.read();
        assertEquals(new Token.Character(str), emittedToken);
    }
    
    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        Token emittedToken = tokeniser.read();
        assertEquals(new Token.Character(Character.toString(c)), emittedToken);
    }
    
    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }
    
    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Comment;
        tokeniser.transition(state);
        TokeniserState currentState = tokeniser.getState();
        assertEquals(state, currentState);
    }
    
    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Comment;
        tokeniser.advanceTransition(state);
        TokeniserState currentState = tokeniser.getState();
        assertEquals(state, currentState);
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        Character characterReference = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(characterReference);
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
        tokeniser.tagPending.tagName = "test";
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals(new Token.StartTag().name("test"), emittedToken);
    }
    
    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }
    
    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.addData("test");
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertEquals(new Token.Comment().data("test"), emittedToken);
    }
    
    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }
    
    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name("html");
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertEquals(new Token.Doctype().name("html"), emittedToken);
    }
    
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = new Token.StartTag().name("test");
        tokeniser.tagPending = new Token.EndTag().name("test");
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
    
    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Comment;
        tokeniser.error(state);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Unexpected character in input", error.getErrorMessage());
        assertEquals(state, error.getState());
    }
    
    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Comment;
        tokeniser.eofError(state);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF)", error.getErrorMessage());
        assertEquals(state, error.getState());
    }
    
    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Invalid character reference", error.getErrorMessage());
    }
    
    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Error";
        tokeniser.error(errorMsg);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals(errorMsg, error.getErrorMessage());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}