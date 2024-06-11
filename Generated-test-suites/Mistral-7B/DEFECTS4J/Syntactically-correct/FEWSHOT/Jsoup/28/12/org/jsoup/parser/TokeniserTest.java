package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;

public class TokeniserTest {

    @Test
    public void testRead() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        when(reader.isEmpty()).thenReturn(false);
        when(reader.current()).thenReturn('a');

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.toString());

        Token expected = new Token.Character("a");
        assertSame(expected, token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndOfFile, token.type);
        assertNull(token);

        when(reader.isEmpty()).thenReturn(true);
        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = new Token.Character('a');
        tokeniser.emit(token);
        Token expected = new Token.Character('a');
        assertSame(expected, tokeniser.read());
    }

    @Test
    public void testEmitEndTag() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token endTag = new Token.EndTag("div");
        tokeniser.emit(endTag);
        Token expected = new Token.EndTag("div");
        assertSame(expected, tokeniser.read());
    }

    @Test
    public void testEmitStartTag() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag = new Token.StartTag("div");
        tokeniser.emit(startTag);
        Token expected = new Token.StartTag("div");
        assertSame(expected, tokeniser.read());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token endTag = new Token.EndTag("div");
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.emit(endTag);
        Token expected = new Token.EndTag("div");
        assertSame(expected, tokeniser.read());
    }

    @Test
    public void testCreateTagPending() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token.StartTag startTag = tokeniser.createTagPending(true);
        Token.EndTag endTag = tokeniser.createTagPending(false);

        assertEquals(Token.StartTag.class, startTag.getClass());
        assertEquals(Token.EndTag.class, endTag.getClass());
    }

    @Test
    public void testCreateCommentPending() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token comment = tokeniser.createCommentPending();
        assertEquals(Token.Comment.class, comment.getClass());
    }

    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token doctype = tokeniser.createDoctypePending();
        assertEquals(Token.Doctype.class, doctype.getClass());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag = new Token.StartTag("div");
        Token endTag = new Token.EndTag("div");
        Token tagPending = new Token.StartTag("p");

        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emit(startTag);
        tokeniser.createTagPending(true);
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.emit(endTag);

        assertEquals(true, tokeniser.isAppropriateEndTagToken());
        assertEquals("div", tokeniser.appropriateEndTagName());

        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emit(startTag);
        tokeniser.createTagPending(true);
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.emit(tagPending);

        assertEquals(false, tokeniser.isAppropriateEndTagToken());
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);
        tokeniser.error(TokeniserState.ErrorStartTagName);

        ParseError expectedError = new ParseError(reader.pos(), "Unexpected character '%s' in input state [%s]", reader.current(), TokeniserState.ErrorStartTagName);
        assertEquals(expectedError.getMessage(), tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testEOFError() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);
        tokeniser.eofError(TokeniserState.Data);

        ParseError expectedError = new ParseError(reader.pos(), "Unexpectedly reached end of file (EOF) in input state [%s]", TokeniserState.Data);
        assertEquals(expectedError.getMessage(), tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.CharRef);
        tokeniser.characterReferenceError("numeric reference with no numerals");

        ParseError expectedError = new ParseError(reader.pos(), "Invalid character reference: numeric reference with no numerals");
        assertEquals(expectedError.getMessage(), tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        when(reader.matchesAny('\t', '\n', '\r', '\f', ' ', '<', '&')).thenReturn(false);
        when(reader.matchesConsume('#')).thenReturn(true);
        when(reader.consumeLetterThenDigitSequence()).thenReturn("amp");
        when(reader.matchConsume(';')).thenReturn(true);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);

        character = tokeniser.consumeCharacterReference('a', false);
        assertNull(character);

        character = tokeniser.consumeCharacterReference(null, true);
        assertNull(character);

        character = tokeniser.consumeCharacterReference("amp", false);
        assertEquals(Character.AMPERSAND, character);
    }
}