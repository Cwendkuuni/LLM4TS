package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

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
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
        assertFalse(tokeniser.charBuffer.length() > 0);
        assertEquals(token, tokeniser.emitPending);
        
        Token.Character character = new Token.Character("test");
        tokeniser.emit(character);
        assertTrue(tokeniser.isEmitPending);
        assertTrue(tokeniser.charBuffer.length() > 0);
        assertEquals(character, tokeniser.charBuffer.toString());
    }
    
    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        assertTrue(tokeniser.isEmitPending);
        assertTrue(tokeniser.charBuffer.length() > 0);
        assertEquals(c, tokeniser.charBuffer.charAt(0));
    }
    
    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }
    
    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Rawtext);
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Rawtext, state);
    }
    
    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.RawtextDoubleEscaped);
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.RawtextDoubleEscaped, state);
        assertTrue(reader.pos() > 0);
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = false;
        
        Character reference = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(reference);
    }
    
    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertEquals(start, tag instanceof Token.StartTag);
        assertEquals(start, tokeniser.tagPending instanceof Token.StartTag);
    }
    
    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isEmitPending);
        assertFalse(tokeniser.charBuffer.length() > 0);
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
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
        assertTrue(tokeniser.isEmitPending);
        assertFalse(tokeniser.charBuffer.length() > 0);
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
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
        assertTrue(tokeniser.isEmitPending);
        assertFalse(tokeniser.charBuffer.length() > 0);
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
    }
    
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertTrue(tokeniser.dataBuffer instanceof StringBuilder);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending = new Token.EndTag();
        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertFalse(isAppropriateEndTagToken);
        
        tokeniser.tagPending.tagName = "test";
        isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertTrue(isAppropriateEndTagToken);
    }
    
    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "test";
        String tagName = tokeniser.appropriateEndTagName();
        assertEquals("test", tagName);
    }
    
    @Test
    public void testError() {
        TokeniserState state = TokeniserState.ScriptDataEscaped;
        tokeniser.error(state);
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals("Unexpected character '" + reader.current() + "' in input state [" + state + "]", error.getErrorMessage());
    }
    
    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.BeforeAttributeName;
        tokeniser.eofError(state);
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [" + state + "]", error.getErrorMessage());
    }
    
    @Test
    public void testCharacterReferenceError() {
        String message = "test error";
        tokeniser.characterReferenceError(message);
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference: " + message, error.getErrorMessage());
    }
    
    @Test
    public void testErrorWithString() {
        String errorMsg = "test error";
        tokeniser.error(errorMsg);
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals(errorMsg, error.getErrorMessage());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();
        assertTrue(currentNodeInHtmlNS);
    }
    
    // More test cases for the remaining methods in Tokeniser class
}