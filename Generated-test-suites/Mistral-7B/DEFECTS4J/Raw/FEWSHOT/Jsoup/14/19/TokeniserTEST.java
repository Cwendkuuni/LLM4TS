package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

import org.junit.Test;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private CharacterReader getMockCharacterReader(String input) {
        MockCharacterReader reader = new MockCharacterReader();
        reader.setInput(input);
        return reader;
    }

    private Token createToken(TokenType type, String tagName, boolean selfClosing) {
        Token token = new Token();
        token.type = type;
        if (type == Token.TokenType.StartTag || type == Token.TokenType.EndTag) {
            Token.Tag tag = new Token.Tag();
            tag.tagName = tagName;
            tag.selfClosing = selfClosing;
            token.tag = tag;
        }
        return token;
    }

    @Test
    public void testRead_EmptyInput() {
        CharacterReader reader = getMockCharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertEquals(createToken(Token.TokenType.EndOfInput, null, false), tokeniser.read());
    }

    @Test
    public void testRead_SingleCharacter() {
        CharacterReader reader = getMockCharacterReader("a");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertEquals(createToken(Token.TokenType.Character, "a", false), tokeniser.read());
    }

    @Test
    public void testRead_SingleCharacter_EndOfInput() {
        CharacterReader reader = getMockCharacterReader("a");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.read();
        assertEquals(createToken(Token.TokenType.EndOfInput, null, false), tokeniser.read());
    }

    @Test
    public void testRead_TwoCharacters() {
        CharacterReader reader = getMockCharacterReader("ab");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertEquals(createToken(Token.TokenType.Character, "a", false), tokeniser.read());
        assertEquals(createToken(Token.TokenType.Character, "b", false), tokeniser.read());
    }

    @Test
    public void testRead_TwoCharacters_EndOfInput() {
        CharacterReader reader = getMockCharacterReader("ab");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.read();
        tokeniser.read();
        assertEquals(createToken(Token.TokenType.EndOfInput, null, false), tokeniser.read());
    }

    @Test
    public void testRead_StartTag() {
        CharacterReader reader = getMockCharacterReader("<tag>");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertEquals(createToken(Token.TokenType.StartTag, "tag", false), tokeniser.read());
    }

    @Test
    public void testRead_StartTag_SelfClosing() {
        CharacterReader reader = getMockCharacterReader("<tag />");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertEquals(createToken(Token.TokenType.StartTag, "tag", true), tokeniser.read());
    }

    @Test
    public void testRead_EndTag() {
        CharacterReader reader = getMockCharacterReader("</tag>");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertEquals(createToken(Token.TokenType.EndTag, "tag", false), tokeniser.read());
    }

    @Test(expected = AssertionError.class)
    public void testRead_UnexpectedCharacter() {
        CharacterReader reader = getMockCharacterReader("a<");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.read();
        tokeniser.read(); // consume 'a'
        tokeniser.read(); // should throw an error
    }

    @Test
    public void testEmit_Character() {
        Tokeniser tokeniser = new Tokeniser(new MockCharacterReader(""));
        tokeniser.emit(createToken(Token.TokenType.Character, "a", false));
        assertEquals("a", tokeniser.reader.getCurrent());
    }

    @Test
    public void testEmit_StartTag() {
        Tokeniser tokeniser = new Tokeniser(new MockCharacterReader(""));
        tokeniser.emit(createToken(Token.TokenType.StartTag, "tag", false));
        assertEquals("<tag", tokeniser.reader.getInput());
    }

    @Test
    public void testEmit_EndTag() {
        Tokeniser tokeniser = new Tokeniser(new MockCharacterReader(""));
        tokeniser.emit(createToken(Token.TokenType.EndTag, "tag", false));
        assertEquals("</tag", tokeniser.reader.getInput());
    }

    @Test
    public void testTransition() {
        CharacterReader reader = getMockCharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.transition(TokeniserState.Character);
        assertTrue(tokeniser.getState() == TokeniserState.Character);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(new MockCharacterReader(""));
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new MockCharacterReader();
        reader.setInput("&amp;");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertEquals('\u0026', tokeniser.consumeCharacterReference(null, false));
    }

    @Test
    public void testConsumeCharacterReference_InAttribute() {
        CharacterReader reader = new MockCharacterReader();
        reader.setInput("&amp; class=\"class\"");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertNull(tokeniser.consumeCharacterReference(null, true));
    }

    @Test
    public void testConsumeCharacterReference_Named() {
        CharacterReader reader = new MockCharacterReader();
        reader.setInput("&nbsp;");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertEquals('\u00A0', tokeniser.consumeCharacterReference(null, false));
    }

    @Test
    public void testConsumeCharacterReference_Named_InAttribute() {
        CharacterReader reader = new MockCharacterReader();
        reader.setInput("color=\"#&hhhhtt;ff\"");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertNull(tokeniser.consumeCharacterReference(null, true));
    }

    @Test
    public void testError() {
        CharacterReader reader = new MockCharacterReader();
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.error(TokeniserState.Data);
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character in input", error.getMessage());
        assertEquals(Character.MIN_VALUE, error.getCharacter());
        assertEquals(TokeniserState.Data, error.getState());
    }

    @Test
    public void testEofError() {
        CharacterReader reader = new MockCharacterReader();
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.eofError(TokeniserState.Data);
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF)", error.getMessage());
        assertEquals(TokeniserState.Data, error.getState());
    }

    @Test
    public void testCharacterReferenceError() {
        CharacterReader reader = new MockCharacterReader();
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.characterReferenceError();
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference", error.getMessage());
        assertEquals(Character.MIN_VALUE, error.getCharacter());
    }
}