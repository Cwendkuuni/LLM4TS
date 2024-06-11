package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;

public class TokeniserTest {

    @Test
    public void testRead_DataState() {
        CharacterReader reader = new CharacterReader("DataData");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals('D', token.getData()[0]);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals('a', token.getData()[0]);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndOfInput, token.getType());
    }

    @Test
    public void testRead_ResetTypeState() {
        CharacterReader reader = new CharacterReader("DataData");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals('D', token.getData()[0]);

        tokeniser.transition(TokeniserState.TagStart);
        token = tokeniser.read();
        assertNull(token);

        tokeniser.transition(TokeniserState.CharacterRef);
        token = tokeniser.read();
        assertEquals(Token.TokenType.CharacterReference, token.getType());
        assertNull(token.getData());

        tokeniser.transition(TokeniserState.Data);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals('a', token.getData()[0]);

        tokeniser.transition(TokeniserState.EndTag);
        token = tokeniser.read();
        assertNull(token);

        tokeniser.transition(TokeniserState.Doctype);
        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit_Character() {
        CharacterReader reader = new CharacterReader("DataData");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        tokeniser.emit(token);

        token = tokeniser.read();
        tokeniser.emit(token);

        token = tokeniser.read();
        tokeniser.emit(token);

        tokeniser.transition(TokeniserState.EndTag);
        token = tokeniser.read();
        tokeniser.emit(token);

        assertEquals(3, tokeniser.getState().getEmittedCount());
    }

    @Test
    public void testEmit_StartTag() {
        CharacterReader reader = new CharacterReader("tag tag /tag");;
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag = tokeniser.read();
        tokeniser.emit(startTag);

        Token endTag = tokeniser.read();
        tokeniser.emit(endTag);

        assertEquals(2, tokeniser.getState().getEmittedCount());
        assertSame(startTag, tokeniser.getState().getLastEmittedToken());
        assertSame(endTag, tokeniser.getState().getLastEmittedToken(1));
    }

    @Test(expected = AssertionError.class)
    public void testEmit_InvalidSequence() {
        CharacterReader reader = new CharacterReader("tag tag tag");;
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag = tokeniser.read();
        tokeniser.emit(startTag);

        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emit(startTag);

        tokeniser.emit(tokeniser.read()); // should be EndTag
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&#x1F600;&#x1F601;");
        Tokeniser tokeniser = new Tokeniser(reader, null);

        int[] result = tokeniser.consumeCharacterReference(null, false);
        assertArrayEquals(new int[]{0xD83D, 0xDC00}, result);

        result = tokeniser.consumeCharacterReference('x', false);
        assertArrayEquals(new int[]{0xD83D, 0xDC01}, result);

        result = tokeniser.consumeCharacterReference(null, true);
        assertNull(result);

        result = tokeniser.consumeCharacterReference('X', true);
        assertArrayEquals(new int[]{0}, result);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), null);
        Token.StartTag tagStart = tokeniser.createTagPending(true);
        Token.EndTag tagEnd = tokeniser.createTagPending(false);

        assertSame(tagStart.getName(), tagStart.getName());
        assertNull(tagStart.getAttributes());
        assertSame(tagEnd.getName(), tagEnd.getName());
        assertNull(tagEnd.getAttributes());
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new CharacterReader("tagtag");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token.StartTag tagStart = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        assertEquals(Tag.Tag.TAG, tagStart.getType());
        assertEquals("tag", tagStart.getName());
        assertNull(tagStart.getAttributes());

        tagStart = tokeniser.createTagPending(true);
        tokeniser.emitsuccessfully(tagStart);
        tokeniser.emitTagPending();

        assertEquals(Tag.Tag.TAG, tagStart.getType());
        assertEquals("tag", tagStart.getName());
        assertNull(tagStart.getAttributes());
        assertEquals(tagStart, tokeniser.getState().getLastEmittedToken());
    }

    @Test
    public void testCreateCommentPending() {
        CharacterReader reader = new CharacterReader("--comment");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token comment = tokeniser.read();
        assertEquals(Token.TokenType.Comment, comment.getType());
        assertEquals("comment", comment.getData());
    }

    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token doctype = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, doctype.getType());
        assertEquals("<!DOCTYPE html>", doctype.getData());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = new CharacterReader("tag tag /tag");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        assertTrue(tokeniser.isAppropriateEndTagToken());

        tokeniser.transition(TokeniserState.Character);
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        CharacterReader reader = new CharacterReader("tag tag /tag");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        assertEquals("tag", tokeniser.appropriateEndTagName());
        tokeniser.transition(TokeniserState.Character);
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test(expected = AssertionError.class)
    public void testError_TokeniserState() {
        CharacterReader reader = new CharacterReader("DataData");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Character);
        tokeniser.error(TokeniserState.CharacterRef);
    }

    @Test
    public void testError_ParseError() {
        CharacterReader reader = new CharacterReader("DataData");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Character);
        tokeniser.error("Unexpected character '%s' in input state [%s]", reader.current(), TokeniserState.CharacterRef);
    }

    @Test
    public void testReadEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndOfInput, token.getType());
    }

    @Test
    public void testReadEmptyInputEndTagState() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.EndTag);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndOfInput, token.getType());
    }

    @Test(expected = AssertionError.class)
    public void testReadEmptyInputCharacterState() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Character);
        tokeniser.read();
    }

    @Test
    public void testReadEmptyInputDataState() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndOfInput, token.getType());
    }

    @Test
    public void testReadEmptyInputDoctypeState() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Doctype);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndOfInput, token.getType());
    }

    @Test
    public void testReadEmptyInputTagStartState() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.TagStart);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndOfInput, token.getType());
    }

    @Test
    public void testReadEmptyInputTagEndState() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.TagEnd);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndOfInput, token.getType());
    }

    @Test
    public void testUnescapeEntities() {
        CharacterReader reader = new CharacterReader("&#x1F600;&#x1F601;");
        String result = new Tokeniser(reader, null).unescapeEntities(false);
        assertEquals("\uD83D\uDC00\uD83D\uDC01", result);

        result = new Tokeniser(reader, null).unescapeEntities(true);
        assertEquals("🤴🏻🤴🏼", result);
    }
}