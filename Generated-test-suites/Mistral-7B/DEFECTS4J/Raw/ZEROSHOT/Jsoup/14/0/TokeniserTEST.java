package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Tokeniser class.
 */
public class TokeniserTest {

    @Test
    public void testTokeniser() {
        CharacterReader reader = new CharacterReader("Test <html><head><title>Test</title></head><body><h1>Test</h1></body>");
        Tokeniser tokeniser = new Tokeniser(reader);

        // Test reading tokens
        Token token;
        for (int i = 0; i < 10; i++) {
            token = tokeniser.read();
            System.out.println("Token type: " + token.type);
            if (token.type != Token.TokenType.StartTag && token.type != Token.TokenType.EndTag && token.type != Token.TokenType.Character && token.type != Token.TokenType.Doctype && token.type != Token.TokenType.Comment) {
                fail("Unexpected token type: " + token.type);
            }
        }

        // Test emitting tokens
        tokeniser.emit(new Token.Character("Test"));
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Test", token.data);

        tokeniser.emit(new Token.StartTag("html"));
        tokeniser.emit(new Token.StartTag("head"));
        tokeniser.emit(new Token.StartTag("title"));
        tokeniser.emit(new Token.Character("Test"));
        tokeniser.emit(new Token.EndTag("title"));
        tokeniser.emit(new Token.StartTag("body"));
        tokeniser.emit(new Token.StartTag("h1"));
        tokeniser.emit(new Token.Character("Test"));
        tokeniser.emit(new Token.EndTag("h1"));
        tokeniser.emit(new Token.EndTag("body"));
        tokeniser.emit(new Token.EndTag("html"));

        // Test reading and emitting tags
        tokeniser.transition(TokeniserState.TagStart);
        token = tokeniser.read();
        tokeniser.emit(new Token.StartTag("html"));
        assertEquals("html", token.tagName);

        tokeniser.transition(TokeniserState.TagName);
        token = tokeniser.read();
        tokeniser.consumeCharacterReference(null, false);
        assertEquals("h", token.data);

        tokeniser.transition(TokeniserState.TagAttributes);
        tokeniser.consumeCharacterReference(null, true);
        assertEquals("class", token.tagName);

        tokeniser.transition(TokeniserState.TagEnd);
        token = tokeniser.read();
        tokeniser.consumeCharacterReference(null, false);
        assertEquals("body", token.tagName);

        // Test reading and emitting comments
        tokeniser.transition(TokeniserState.Comment);
        tokeniser.createCommentPending();
        tokeniser.read();
        tokeniser.emitCommentPending();
        tokeniser.read();
        tokeniser.consumeCharacterReference(null, false);
        assertEquals("Test", token.data);

        // Test reading and emitting doctype
        tokeniser.transition(TokeniserState.Doctype);
        tokeniser.createDoctypePending();
        tokeniser.read();
        tokeniser.emitDoctypePending();
        tokeniser.read();
        tokeniser.consumeCharacterReference(null, false);
        assertEquals("<!DOCTYPE html", token.data);

        // Test error handling
        tokeniser.transition(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        assertTrue(tokeniser.isTrackErrors());
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Unexpected character in input", errors.get(0).getMessage());

        tokeniser.setTrackErrors(false);
        tokeniser.error(TokeniserState.Data);
        tokeniser.read(); // consume error token
        assertTrue(tokeniser.isTrackErrors() == false);

        // Test EOF error
        tokeniser.transition(TokeniserState.Data);
        reader.setPos(reader.getPos() + reader.available());
        tokeniser.eofError(TokeniserState.Data);
        assertTrue(tokeniser.isTrackErrors());
        errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
    }

    @Test
    public void testCharacterReference() {
        CharacterReader reader = new CharacterReader("&a; &b#x1F &c&#x{FFFD};");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.transition(TokeniserState.Data);

        tokeniser.consumeCharacterReference('a', false);
        assertEquals('a', tokeniser.read().data.charAt(0));

        tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u000b', tokeniser.read().data.charAt(0));

        tokeniser.consumeCharacterReference(null, false);
        assertEquals('\ufffd', tokeniser.read().data.charAt(0));
    }

    @Test
    public void testCharacterReferenceError() {
        CharacterReader reader = new CharacterReader("&x");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.transition(TokeniserState.Data);

        tokeniser.consumeCharacterReference(null, false);
        tokeniser.read(); // consume error token
        assertTrue(tokeniser.isTrackErrors());
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference", errors.get(0).getMessage());
    }

    // Getters and setters test
    @Test
    public void testGettersAndSetters() {
        Tokeniser tokeniser = new Tokeniser(null);

        assertEquals(TokeniserState.Data, tokeniser.getState());
        tokeniser.transition(TokeniserState.TagStart);
        assertEquals(TokeniserState.TagStart, tokeniser.getState());

        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());

        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());

        tokeniser.createTempBuffer();
        assertNull(tokeniser.dataBuffer);
    }

    // GetErrors test
    @Test
    public void testGetErrors() {
        CharacterReader reader = new CharacterReader("Test");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.error(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(2, errors.size());
    }

    // Transition test
    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.transition(TokeniserState.Data);
        tokeniser.transition(TokeniserState.TagStart);
        assertEquals(TokeniserState.TagStart, tokeniser.getState());

        tokeniser.transition(TokeniserState.TagName);
        tokeniser.transition(TokeniserState.TagAttributes);
        tokeniser.transition(TokeniserState.TagEnd);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    // State test
    @Test
    public void testState() {
        Tokeniser tokeniser = new Tokeniser(null);
        assertEquals(TokeniserState.Data, tokeniser.getState());
        tokeniser.transition(TokeniserState.TagStart);
        assertEquals(TokeniserState.TagStart, tokeniser.getState());
        tokeniser.transition(TokeniserState.TagName);
        assertEquals(TokeniserState.TagName, tokeniser.getState());
        tokeniser.transition(TokeniserState.TagAttributes);
        assertEquals(TokeniserState.TagAttributes, tokeniser.getState());
        tokeniser.transition(TokeniserState.TagEnd);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    // Get errors when track errors is off test
    @Test
    public void testGetErrorsWhenTrackErrorsIsOff() {
        CharacterReader reader = new CharacterReader("Test");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.setTrackErrors(false);
        tokeniser.error(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(0, errors.size());
    }
}