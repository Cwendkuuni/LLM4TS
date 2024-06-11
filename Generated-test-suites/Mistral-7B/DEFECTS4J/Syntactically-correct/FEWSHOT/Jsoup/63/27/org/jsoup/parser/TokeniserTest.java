package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.Arrays;

public class TokeniserTest {

    @Test
    public void testTokeniserInstance() {
        CharacterReader reader = new CharacterReader("test input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testTokenReadingWhenNothingPending() {
        CharacterReader reader = new CharacterReader("test input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        assertFalse(tokeniser.isEmitPending());
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testTokenReadingWhenCharacterPending() {
        CharacterReader reader = new CharacterReader("a");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.emit("a");
        tokeniser.transition(TokeniserState.Char);
        Token token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals('a', token.data());
    }

    @Test
    public void testTokenReadingWhenCharacterBufferPending() {
        CharacterReader reader = new CharacterReader("ab");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.emit("a");
        tokeniser.transition(TokeniserState.Char);
        tokeniser.emit("b");
        tokeniser.emit(TokeniserState.Data);
        Token token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals('a', token.data());
        token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals('b', token.data());
    }

    @Test
    public void testTokenReadingWhenTokenPending() {
        CharacterReader reader = new CharacterReader("<p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.emit(new Token.StartTag("p"));
        tokeniser.transition(TokeniserState.StartTag);
        Token token = tokeniser.read();
        assertEquals(TokenType.StartTag, token.type);
        assertEquals("p", token.tagName());
    }

    @Test
    public void testTokenReadingWhenTokenBufferPending() {
        CharacterReader reader = new CharacterReader("<p class='test'>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.emit(new Token.StartTag("p"));
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emit("class='test'");
        tokeniser.emit(TokeniserState.Data);
        tokeniser.emit(TokeniserState.Tag);
        tokeniser.emit(new Token.Attribute("class", "test"));
        tokeniser.transition(TokeniserState.Attribute);
        Token token = tokeniser.read();
        assertEquals(TokenType.StartTag, token.type);
        assertEquals("p", token.tagName());
        assertEquals(Arrays.asList("test"), token.attributes().keySet());
    }

    @Test
    public void testTokenReadingWhenEndTagPending() {
        CharacterReader reader = new CharacterReader("<p><p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.emit(new Token.StartTag("p"));
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emit(new Token.StartTag("p"));
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emit(new Token.EndTag("p"));
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.emit(TokeniserState.Data);
        Token token = tokeniser.read();
        assertEquals(TokenType.EndTag, token.type);
        assertEquals("p", token.tagName());
    }

    @Test
    public void testTokenReadingWhenSelfClosingTagPending() {
        CharacterReader reader = new CharacterReader("<img src='test.png' />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.emit(new Token.StartTag("img"));
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emit("src='test.png'");
        tokeniser.emit(TokeniserState.Data);
        tokeniser.emit(TokeniserState.Tag);
        tokeniser.emit(new Token.Attribute("src", "test.png"));
        tokeniser.transition(TokeniserState.Attribute);
        tokeniser.emit(TokeniserState.SelfClosing);
        tokeniser.emit(TokeniserState.Data);
        Token token = tokeniser.read();
        assertEquals(TokenType.StartTag, token.type);
        assertEquals("img", token.tagName());
        assertEquals(Arrays.asList("src", "test.png"), token.attributes().keySet());
        token = tokeniser.read();
        assertEquals(TokenType.EndTag, token.type);
        assertEquals("img", token.tagName());
    }

    @Test
    public void testTokenReadingWhenDoctypePending() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.emit(new Token.Doctype("html"));
        tokeniser.transition(TokeniserState.Doctype);
        Token token = tokeniser.read();
        assertEquals(TokenType.Doctype, token.type);
        assertEquals("html", token.data());
    }

    @Test
    public void testTokenReadingWhenCommentPending() {
        CharacterReader reader = new CharacterReader("<!-- test -->");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.emit(new Token.Comment("test"));
        tokeniser.transition(TokeniserState.Comment);
        Token token = tokeniser.read();
        assertEquals(TokenType.Comment, token.type);
        assertEquals("test", token.data());
    }

    @Test
    public void testTokenReadingWhenErrorState() {
        CharacterReader reader = new CharacterReader("<!-- test --><!");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.emit(new Token.Comment("test"));
        tokeniser.transition(TokeniserState.Comment);
        tokeniser.emit("!");
        tokeniser.transition(TokeniserState.Error);
        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(1, errors.errors().size());
        ParseError error = errors.errors().get(0);
        assertEquals("Unexpected character '!' in error state [Error]", error.getMessage());
    }
}