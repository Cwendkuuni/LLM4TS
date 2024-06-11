package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.TokeniserState;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;

public class TokeniserTest {

    @Test
    public void testRead() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        when(reader.isEmpty()).thenReturn(false);
        when(reader.current()).thenReturn('a');

        Token token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("a", token.getData());

        when(reader.isEmpty()).thenReturn(true);
        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = new Token.Character("a");
        tokeniser.emit(token);
        Token emitPending = tokeniser.emitPending;
        assertSame(token, emitPending);
    }

    @Test
    public void testEmitString() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.emit("abc");
        Token token = tokeniser.emitPending;
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("abc", token.getData());
    }

    @Test
    public void testEmitCharacter() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.emit('a');
        Token token = tokeniser.emitPending;
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("a", token.getData());
    }

    @Test
    public void testEmitStateTransition() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Script);
        assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.advanceTransition(TokeniserState.Script);
        reader.advance();
        assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testConsumeCharacterReference() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        when(reader.matchConsume("#")).thenReturn(true);
        when(reader.consumeHexSequence()).thenReturn("1234");
        when(reader.matchConsume(";")).thenReturn(true);

        Character result = tokeniser.consumeCharacterReference(null, false);
        assertEquals((char) 0x1234, result);

        when(reader.consumeLetterSequence()).thenReturn("amp");
        when(reader.matchConsume(";")).thenReturn(true);

        result = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Entities.AMPERSAND, result);

        when(reader.consumeLetterSequence()).thenReturn("a");
        when(reader.matchConsume(";")).thenReturn(false);

        tokeniser.characterReferenceError("character reference not ended with semicolon");
    }

    @Test
    public void testCreateTagPending() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token.StartTag tagPending = tokeniser.createTagPending(true);
        assertEquals(Token.StartTag.class, tagPending.getClass());
        tagPending.tagName = "div";
        tagPending.selfClosing = false;
        assertEquals("div", tagPending.tagName);
        assertFalse(tagPending.selfClosing);

        Token.EndTag tagPending2 = tokeniser.createTagPending(false);
        assertEquals(Token.EndTag.class, tagPending2.getClass());
        tagPending2.tagName = "div";
        tagPending2.selfClosing = true;
        assertEquals("div", tagPending2.tagName);
        assertTrue(tagPending2.selfClosing);
    }

    @Test
    public void testEmitTagPending() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token.StartTag tagPending = tokeniser.createTagPending(true);
        tagPending.tagName = "div";

        tokeniser.emitTagPending();
        Token token = tokeniser.emitPending;
        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("div", token.tagName);
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createCommentPending();
        Token token = tokeniser.emitPending;
        assertEquals(Token.Comment.class, token.getClass());
        assertTrue(token.getData().startsWith("<!--"));
        assertFalse(token.selfClosing);
    }

    @Test
    public void testEmitCommentPending() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token.Comment commentPending = tokeniser.createCommentPending();

        tokeniser.emitCommentPending();
        tokeniser.emitPending = null;

        assertEquals("-->", tokeniser.emitPending.getData());
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createDoctypePending();
        Token token = tokeniser.emitPending;
        assertEquals(Token.Doctype.class, token.getClass());
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testEmitDoctypePending() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Doctype doctypePending = new Doctype("html", null);

        tokeniser.emitDoctypePending();
        tokeniser.emitPending = null;

        assertEquals("html", tokeniser.emitPending.getData());
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        StringBuilder dataBuffer = tokeniser.dataBuffer;

        tokeniser.createTempBuffer();
        assertEquals(new StringBuilder(), dataBuffer);
        dataBuffer = tokeniser.dataBuffer;
        dataBuffer.append("test");
        assertEquals("test", dataBuffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";

        tokeniser.tagPending = startTag;
        tokeniser.lastStartTag = startTag;

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_differentTag() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "p";

        tokeniser.tagPending = startTag;
        tokeniser.lastStartTag = new Token.StartTag("div");

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";

        tokeniser.tagPending = startTag;

        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.error(TokeniserState.Data);
        List<ParseError> errorsList = errors.getErrors();
        assertEquals(1, errorsList.size());
        ParseError error = errorsList.get(0);
        assertEquals("Unexpected character 'x' in input state [Data]", error.getMessage());
    }

    @Test
    public void testEOFError() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        ErrorReportingTokeniser tokeniser = new ErrorReportingTokeniser(reader, errors);

        tokeniser.eofError(TokeniserState.Data);
        List<ParseError> errorsList = errors.getErrors();
        assertEquals(1, errorsList.size());
        ParseError error = errorsList.get(0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.getMessage());
    }

    @Test
    public void testCharacterReferenceError() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        ErrorReportingTokeniser tokeniser = new ErrorReportingTokeniser(reader, errors);

        tokeniser.characterReferenceError("Invalid character reference: unexpected character 'x'");
        List<ParseError> errorsList = errors.getErrors();
        assertEquals(1, errorsList.size());
        ParseError error = errorsList.get(0);
        assertEquals("Invalid character reference: unexpected character 'x'", error.getMessage());
    }

    @Test
    public void testError_nullState() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        ErrorReportingTokeniser tokeniser = new ErrorReportingTokeniser(reader, errors);

        tokeniser.error(null);
        List<ParseError> errorsList = errors.getErrors();
        assertEquals(0, errorsList.size());
    }

    private static class ErrorReportingTokeniser extends Tokeniser {
        public ErrorReportingTokeniser(CharacterReader reader, ParseErrorList errors) {
            super(reader, errors);
        }

        @Override
        void error(String errorMsg) {
            super.error(TokeniserState.Data);
            errors.add(new ParseError(reader.pos(), errorMsg));
        }
    }
}