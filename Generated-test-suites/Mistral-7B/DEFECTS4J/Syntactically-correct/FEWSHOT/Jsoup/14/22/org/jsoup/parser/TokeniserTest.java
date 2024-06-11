package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testTokeniser_BasicFunctionality() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("test".getBytes()));
        Tokeniser tokeniser = new Tokeniser(reader);

        // read without any special handling
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("e", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("st", token.charContent());

        // self closing tag
        tokeniser.transition(TokeniserState.SelfClosingStartTag);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("/", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(" ", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("i", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("", token.tagName);
        assertNull(token.tagData);

        // start tag
        tokeniser.transition(TokeniserState.StartTagStart);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("<", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(" ", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("i", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(">", token.charContent());

        Token startTag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals("ti", startTag.tagName);
        assertNull(startTag.tagData);

        // end tag
        tokeniser.transition(TokeniserState.EndTagStart);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("/", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(">", token.charContent());

        Token endTag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals("ti", endTag.tagName);
        assertNull(endTag.tagData);

        // check isAppropriateEndTagToken
        tokeniser.transition(TokeniserState.StartTagStart);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.EndTagStart);
        tokeniser.createTagPending(false);
        assertTrue(tokeniser.isAppropriateEndTagToken());

        tokeniser.transition(TokeniserState.StartTagStart);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.EndTagStart);
        tokeniser.createTagPending(true);
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testTokeniser_HandlesErrors() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("test<".getBytes()));
        Tokeniser tokeniser = new Tokeniser(reader);

        tokeniser.transition(TokeniserState.Data);

        // read without any special handling
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("e", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("s", token.charContent());

        tokeniser.error("Unexpected character in input");

        // self closing tag
        tokeniser.transition(TokeniserState.SelfClosingStartTag);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("/", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.charContent());

        tokeniser.error("Unexpected character in input");

        // check errors
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character in input", error.getMessage());
        assertEquals(10, error.getLineNumber());
        assertEquals(1, error.getColumnNumber());

        // start tag
        tokeniser.transition(TokeniserState.StartTagStart);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("<", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.charContent());

        tokeniser.error("Unexpected character in input");

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("i", token.charContent());

        tokeniser.transition(TokeniserState.StartTag);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(">", token.charContent());

        // check errors
        errors = tokeniser.getErrors();
        assertEquals(2, errors.size());
        error = errors.get(0);
        assertEquals("Unexpected character in input", error.getMessage());
        assertEquals(10, error.getLineNumber());
        assertEquals(1, error.getColumnNumber());
        error = errors.get(1);
        assertEquals("Unexpected character in input", error.getMessage());
        assertEquals(11, error.getLineNumber());
        assertEquals(2, error.getColumnNumber());

        // end tag
        tokeniser.transition(TokeniserState.EndTagStart);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("/", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.charContent());

        tokeniser.error("Unexpected character in input");

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(">", token.charContent());

        tokeniser.transition(TokeniserState.Data);

        // check errors
        errors = tokeniser.getErrors();
        assertEquals(3, errors.size());
        error = errors.get(0);
        assertEquals("Unexpected character in input", error.getMessage());
        assertEquals(10, error.getLineNumber());
        assertEquals(1, error.getColumnNumber());
        error = errors.get(1);
        assertEquals("Unexpected character in input", error.getMessage());
        assertEquals(11, error.getLineNumber());
        assertEquals(2, error.getColumnNumber());
        error = errors.get(2);
        assertEquals("Unexpected character in input", error.getMessage());
        assertEquals(11, error.getLineNumber());
        assertEquals(4, error.getColumnNumber());
    }

    @Test
    public void testTokeniser_HandlesCharacterReferences() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("test&#10;&#x61;&#255;&#x1F44D;test".getBytes()));
        Tokeniser tokeniser = new Tokeniser(reader);

        // read without any special handling
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("e", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\n", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(" ", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\u00fd", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\uD834\uDD1E", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("e", token.charContent());

        // check character reference handling
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(0, errors.size());

        // check character replacement handling (not implemented)
        // TODO: implement character replacement table
        //Character replacementChar = tokeniser.consumeCharacterReference(null, false);
        //assertEquals(replacementChar, replacementChar);

        // check character replacement handling (not implemented)
        //Character replacementChar = tokeniser.consumeCharacterReference('\uFFFD', false);
        //assertEquals(replacementChar, replacementChar);

        // check character reference handling (named)
        tokeniser.transition(TokeniserState.Data);
        tokeniser.acknowledgeSelfClosingFlag();
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("&", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("m", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("p", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(";", token.charContent());

        // check character reference handling (numbered)
        tokeniser.transition(TokeniserState.Data);
        tokeniser.acknowledgeSelfClosingFlag();
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("#", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("x", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("4", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(";", token.charContent());

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u0041', character.charValue());

        // check character reference handling (numbered, invalid)
        tokeniser.transition(TokeniserState.Data);
        tokeniser.acknowledgeSelfClosingFlag();
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("#", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("x", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("5", token.charContent());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(";", token.charContent());

        character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }
}