package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniser() throws IOException {
        String input = "<html>\n" +
                "<head><title>&#x27E8;&#x27E8; Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello &#x2665; world!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);

        List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = tokeniser.read()) != null) {
            tokens.add(token);
        }

        Validate.isFalse(errors.size() > 0, "Errors found during tokenisation: " + errors);

        assertEquals(17, tokens.size());
        assertEquals(Token.TokenType.StartTag, tokens.get(0).type);
        assertEquals(Token.Tag.HTML, tokens.get(0).tag);
        assertEquals(Token.TokenType.EndTag, tokens.get(15).type);
        assertEquals(Token.Tag.HTML, tokens.get(15).tag);
        assertEquals(Token.TokenType.EndTag, tokens.get(16).type);
        assertEquals(Token.Tag.BODY, tokens.get(16).tag);
        assertEquals(Token.TokenType.Comment, tokens.get(1).type);
        assertEquals(Token.Tag.TITLE, tokens.get(2).tag);
        assertEquals(Token.TokenType.StartTag, tokens.get(3).type);
        assertEquals(Token.Tag.HEAD, tokens.get(3).tag);
        assertEquals(Token.TokenType.Character, tokens.get(4).type);
        assertEquals("&amp;", tokens.get(4).text);
        assertEquals(Token.TokenType.Character, tokens.get(5).type);
        assertEquals("&#x27E8;&#x27E8;", tokens.get(5).text);
        assertEquals(Token.TokenType.Character, tokens.get(6).type);
        assertEquals("T", tokens.get(6).text);
        assertEquals(Token.TokenType.Character, tokens.get(7).type);
        assertEquals("i", tokens.get(7).text);
        assertEquals(Token.TokenType.Character, tokens.get(8).type);
        assertEquals("t", tokens.get(8).text);
        assertEquals(Token.TokenType.Character, tokens.get(9).type);
        assertEquals("l", tokens.get(9).text);
        assertEquals(Token.TokenType.EndTag, tokens.get(10).type);
        assertEquals(Token.Tag.HEAD, tokens.get(10).tag);
        assertEquals(Token.TokenType.StartTag, tokens.get(11).type);
        assertEquals(Token.Tag.P, tokens.get(11).tag);
        assertEquals(Token.TokenType.Character, tokens.get(12).type);
        assertEquals("H", tokens.get(12).text);
        assertEquals(Token.TokenType.Character, tokens.get(13).type);
        assertEquals("e", tokens.get(13).text);
        assertEquals(Token.TokenType.Character, tokens.get(14).type);
        assertEquals("l", tokens.get(14).text);
        assertEquals(Token.TokenType.Character, tokens.get(15).type);
        assertEquals("l", tokens.get(15).text);
        assertEquals(Token.TokenType.Character, tokens.get(16).type);
        assertEquals("o", tokens.get(16).text);
        assertEquals(Token.TokenType.Character, tokens.get(17).type);
        assertEquals("!", tokens.get(17).text);

        assertEquals("Hello ♥ world!", tokens.get(12).text);
        assertEquals("♥", Entities.getCharacterByName("heta"));
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()) {
            @Override
            public void characterReferenceError(String message) {
                fail(message);
            }
        };
        tokeniser.consumeCharacterReference('X', false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReferenceErrorWithNullInput() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()) {
            @Override
            public void characterReferenceError(String message) {
                // do nothing
            }
        };
        tokeniser.consumeCharacterReference(null, false);
    }

    @Test
    public void testCharacterReferenceErrorWithInvalidNamedEntity() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()) {
            @Override
            public void characterReferenceError(String message) {
                fail(message);
            }
        };
        tokeniser.consumeCharacterReference("foobar", false);
    }

    @Test
    public void testCharacterReferenceErrorWithInvalidHexadecimalSequence() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()) {
            @Override
            public void characterReferenceError(String message) {
                fail(message);
            }
        };
        tokeniser.consumeCharacterReference("&#x12", false);
    }

    @Test
    public void testCharacterReferenceErrorWithMissingSemicolon() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()) {
            @Override
            public void characterReferenceError(String message) {
                fail(message);
            }
        };
        tokeniser.consumeCharacterReference("#", false);
    }

    @Test
    public void testCharacterReferenceErrorWithInvalidNumericReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()) {
            @Override
            public void characterReferenceError(String message) {
                fail(message);
            }
        };
        tokeniser.consumeCharacterReference("&#-1", false);
    }

    @Test
    public void testCharacterReferenceErrorWithCharacterOutsideValidRange() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()) {
            @Override
            public void characterReferenceError(String message) {
                fail(message);
            }
        };
        tokeniser.consumeCharacterReference("#x110000", false);
    }

    @Test
    public void testCharacterReferenceErrorWithSelfClosingFlagNotAcknowledged() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()) {
            @Override
            public void error(String errorMsg) {
                fail(errorMsg);
            }

            @Override
            public void acknowledgeSelfClosingFlag() {
                // do nothing
            }
        };
        tokeniser.consumeCharacterReference("#", false);
    }
}