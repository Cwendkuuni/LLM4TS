package org.jsoup.parser;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        // Typical use case
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        when(reader.isEmpty()).thenReturn(false);
        when(reader.current()).thenReturn('a');
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.read();
        verify(reader).advance();
    }

    @Test
    public void testEmit() {
        // Typical use case
        Token token = mock(Token.class);
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.emit(token);
        assertSame(token, tokeniser.emitPending);
    }

    @Test
    public void testEmit_String() {
        // Typical use case
        String str = "abc";
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmit_Character() {
        // Typical use case
        char c = 'a';
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.emit(c);
        assertEquals(String.valueOf(c), tokeniser.charBuffer.toString());
    }

    @Test
    public void testTransition() {
        // Typical use case
        TokeniserState state = TokeniserState.Data;
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(state);
        assertSame(state, tokeniser.state);
    }

    @Test
    public void testAdvanceTransition() {
        // Typical use case
        TokeniserState state = TokeniserState.TagName;
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.advanceTransition(state);
        verify(reader).advance();
        assertSame(state, tokeniser.state);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Typical use case
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.selfClosingFlagAcknowledged = false;
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        // Typical use case
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        when(reader.isEmpty()).thenReturn(false);
        when(reader.current()).thenReturn('a');
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        verify(reader).mark();
        verify(reader).consumeLetterSequence();
    }

    @Test
    public void testCreateTagPending() {
        // Typical use case
        boolean start = true;
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token.Tag result = tokeniser.createTagPending(start);
        assertTrue(result instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        // Typical use case
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        verify(tokeniser).emit(tokeniser.tagPending);
    }

    @Test
    public void testCreateCommentPending() {
        // Typical use case
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        // Typical use case
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.commentPending = new Token.Comment();
        tokeniser.emitCommentPending();
        verify(tokeniser).emit(tokeniser.commentPending);
    }

    @Test
    public void testCreateDoctypePending() {
        // Typical use case
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        // Typical use case
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.emitDoctypePending();
        verify(tokeniser).emit(tokeniser.doctypePending);
    }

    @Test
    public void testCreateTempBuffer() {
        // Typical use case
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Typical use case
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = new Token.StartTag();
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        // Typical use case
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.lastStartTag = new Token.StartTag();
        assertEquals(tokeniser.lastStartTag.tagName, tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        // Typical use case
        TokeniserState state = TokeniserState.Data;
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.error(state);
        verify(errors).add(any(ParseError.class));
    }

    @Test
    public void testEofError() {
        // Typical use case
        TokeniserState state = TokeniserState.Data;
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.eofError(state);
        verify(errors).add(any(ParseError.class));
    }

    @Test
    public void testCharacterReferenceError() {
        // Typical use case
        String message = "Invalid character reference";
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.characterReferenceError(message);
        verify(errors).add(any(ParseError.class));
    }

    @Test
    public void testError_WithErrorMessage() {
        // Typical use case
        String errorMsg = "Error message";
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.error(errorMsg);
        verify(errors).add(any(ParseError.class));
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Typical use case
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}