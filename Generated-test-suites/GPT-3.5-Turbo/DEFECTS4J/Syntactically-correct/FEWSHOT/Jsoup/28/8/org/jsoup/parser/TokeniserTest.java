package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TokeniserTest {
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token token = tokeniser.read();
        
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", ((Token.Character)token).getData());
    }

    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token token = new Token.StartTag();
        
        tokeniser.emit(token);
        
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.emit("test");
        
        assertTrue(tokeniser.isEmitPending);
        assertEquals("test", tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.emit('t');
        
        assertTrue(tokeniser.isEmitPending);
        assertEquals("t", tokeniser.charBuffer.toString());
    }

    @Test
    public void testTransition() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.transition(TokeniserState.Data);
        
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        CharacterReader reader = new CharacterReader("t");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.advanceTransition(TokeniserState.Data);
        
        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertEquals('t', reader.current());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.acknowledgeSelfClosingFlag();
        
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("#65");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Character character = tokeniser.consumeCharacterReference(null, false);
        
        assertEquals(Character.valueOf('A'), character);
    }

    @Test
    public void testCreateTagPending() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token.Tag tagPending = tokeniser.createTagPending(true);
        
        assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "tag";
        
        tokeniser.emitTagPending();
        
        assertTrue(tokeniser.isEmitPending);
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
        assertEquals("tag", ((Token.StartTag)tokeniser.emitPending).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createCommentPending();
        
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "comment";
        
        tokeniser.emitCommentPending();
        
        assertTrue(tokeniser.isEmitPending);
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
        assertEquals("comment", ((Token.Comment)tokeniser.emitPending).getData());
    }

    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createDoctypePending();
        
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "doctype";
        
        tokeniser.emitDoctypePending();
        
        assertTrue(tokeniser.isEmitPending);
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
        assertEquals("doctype", ((Token.Doctype)tokeniser.emitPending).name);
    }

    @Test
    public void testCreateTempBuffer() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createTempBuffer();
        
        assertTrue(tokeniser.dataBuffer instanceof StringBuilder);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "tag";
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "tag";
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagTokenNoTagPending() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "tag";
        
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagTokenDifferentTag() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "tag1";
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "tag2";
        
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "tag";
        
        assertEquals("tag", tokeniser.appropriateEndTagName());
    }
    
    // Add more test cases for other methods in the Tokeniser class
    
}