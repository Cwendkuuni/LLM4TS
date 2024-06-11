package org.jsoup.parser;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TokeniserTest {

    @Mock
    private CharacterReader reader;
    @Mock
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    public TokeniserTest() {
        MockitoAnnotations.initMocks(this);
        tokeniser = new Tokeniser(reader, errors);
    }

    // Test case 1 for read() method
    @Test
    public void testRead() {
        when(reader.isEmpty()).thenReturn(false);
        when(reader.current()).thenReturn('a');
        when(reader.advance()).thenReturn(true);
        
        Token token = new Token.Character("a");
        tokeniser.emit(token);
        
        Token output = tokeniser.read();
        verify(reader).isEmpty();
        verify(reader).current();
        verify(reader).advance();
        verifyNoMoreInteractions(reader);
        
        assertFalse(tokeniser.isEmitPending);
        assertNull(tokeniser.emitPending);
        assertEquals(token, output);
    }

    // Test case 1 for emit(Token) method
    @Test
    public void testEmitToken() {
        Token token = new Token.Character("a");
        tokeniser.emit(token);

        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
    }

    // Test case 1 for emit(String) method
    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);

        assertFalse(tokeniser.isEmitPending);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    // Test case 1 for emit(char) method
    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);

        assertFalse(tokeniser.isEmitPending);
        assertEquals(c, tokeniser.charBuffer.charAt(0));
    }

    // Test case 1 for getState() method
    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    // Test case 1 for transition(TokeniserState) method
    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.AfterAttributeValue;
        tokeniser.transition(newState);
    
        assertEquals(newState, tokeniser.getState());
    }
    
    // Test case 1 for advanceTransition(TokeniserState) method
    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.BeforeAttributeName;
        when(reader.advance()).thenReturn(true);
        
        tokeniser.advanceTransition(newState);
        
        verify(reader).advance();
        verifyNoMoreInteractions(reader);
        
        assertEquals(newState, tokeniser.getState());
    }

    // Test case 1 for acknowledgeSelfClosingFlag() method
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    // Test case 1 for consumeCharacterReference() method
    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        
        when(reader.isEmpty()).thenReturn(false);
        when(reader.current()).thenReturn('b');
        when(reader.matchesAny(anyCharVararg())).thenReturn(false);
        when(reader.matchConsumeIgnoreCase(anyString())).thenReturn(false);
        when(reader.consumeHexSequence()).thenReturn("");
        when(reader.consumeDigitSequence()).thenReturn("");
        when(reader.matches(';')).thenReturn(false);
        when(reader.matchesLetter()).thenReturn(false);
        when(reader.matchesDigit()).thenReturn(false);
        when(reader.matchesAny(anyCharVararg())).thenReturn(true);
        when(reader.matchConsume(anyString())).thenReturn(false);
        when(reader.matchesLetter()).thenReturn(true);
        when(reader.matchesDigit()).thenReturn(true);
        when(reader.matchesAny(anyCharVararg())).thenReturn(true);
        when(reader.matches(';')).thenReturn(true);
        
        Character output = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        
        verify(reader).isEmpty();
        verify(reader).current();
        verify(reader).matchesAny(anyCharVararg());
        verify(reader).mark();
        verify(reader).matchConsumeIgnoreCase(anyString());
        verify(reader).consumeHexSequence();
        verify(reader).consumeDigitSequence();
        verify(reader).matches(';');
        verify(reader).rewindToMark();
        verify(reader).unconsume();
        verify(reader).matchesLetter();
        verify(reader).matchesDigit();
        verify(reader).matchesAny(anyCharVararg());
        verify(reader).matchesLetter();
        verify(reader).matchesDigit();
        verify(reader).matchesAny(anyCharVararg());
        verify(reader).matches(';');
        verifyNoMoreInteractions(reader);
        
        assertNull(output);
    }

    // Test case 1 for createTagPending(boolean) method
    @Test
    public void testCreateTagPending() {
        boolean start = true;
        
        Token.Tag output = tokeniser.createTagPending(start);
        
        assertTrue(output instanceof Token.StartTag);
        assertEquals(start, output.start);
    }

    // Test case 1 for emitTagPending() method
    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = mock(Token.Tag.class);
        
        tokeniser.emitTagPending();
        
        verify(tokeniser.tagPending).finaliseTag();
        verifyNoMoreInteractions(tokeniser.tagPending);
    }

    // Test case 1 for createCommentPending() method
    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        assertNotNull(tokeniser.commentPending);
    }

    // Test case 1 for emitCommentPending() method
    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending = mock(Token.Comment.class);

        tokeniser.emitCommentPending();

        verify(tokeniser).emit(tokeniser.commentPending);
        verifyNoMoreInteractions(tokeniser);
    }

    // Test case 1 for createDoctypePending() method
    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.doctypePending);
    }

    // Test case 1 for emitDoctypePending() method
    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending = mock(Token.Doctype.class);

        tokeniser.emitDoctypePending();

        verify(tokeniser).emit(tokeniser.doctypePending);
        verifyNoMoreInteractions(tokeniser);
    }

    // Test case 1 for createTempBuffer() method
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
    }

    // Test case 1 for isAppropriateEndTagToken() method
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = null;
        tokeniser.tagPending = new Token.StartTag("div");

        assertFalse(tokeniser.isAppropriateEndTagToken());
        
        tokeniser.lastStartTag = new Token.StartTag("div");
        tokeniser.tagPending = new Token.StartTag("span");

        assertFalse(tokeniser.isAppropriateEndTagToken());
        
        tokeniser.lastStartTag = new Token.StartTag("div");
        tokeniser.tagPending = new Token.StartTag("div");

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    // Test case 1 for appropriateEndTagName() method
    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag("div");

        String output = tokeniser.appropriateEndTagName();

        assertEquals("div", output);
    }

    // Test case 1 for error(TokeniserState) method
    @Test
    public void testErrorWithState() {
        TokeniserState state = TokeniserState.BogusDoctype;

        tokeniser.error(state);

        verify(errors).canAddError();
        verify(errors).add(any(ParseError.class));
        verifyNoMoreInteractions(errors);
    }

    // Test case 1 for eofError(TokeniserState) method
    @Test
    public void testEofErrorWithState() {
        TokeniserState state = TokeniserState.Data;

        tokeniser.eofError(state);

        verify(errors).canAddError();
        verify(errors).add(any(ParseError.class));
        verifyNoMoreInteractions(errors);
    }

    // Test case 1 for characterReferenceError(String) method
    @Test
    public void testCharacterReferenceError() {
        String message = "invalid reference";

        tokeniser.characterReferenceError(message);

        verify(errors).canAddError();
        verify(errors).add(any(ParseError.class));
        verifyNoMoreInteractions(errors);
    }

    // Test case 1 for error(String) method
    @Test
    public void testErrorWithString() {
        String errorMsg = "error message";

        tokeniser.error(errorMsg);

        verify(errors).canAddError();
        verify(errors).add(any(ParseError.class));
        verifyNoMoreInteractions(errors);
    }

    // Test case 1 for currentNodeInHtmlNS() method
    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}