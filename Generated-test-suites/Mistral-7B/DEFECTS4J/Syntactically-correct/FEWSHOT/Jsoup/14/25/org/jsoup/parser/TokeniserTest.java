package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.jsoup.parser.Tokeniser.TokenType.*;

public class TokeniserTest {

    @Test
    public void testTokeniserBasics() {
        CharacterReader reader = new CharacterReader("abc") {
            @Override
            public int pos() {
                return 0;
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token;
        for (int i = 0; i < 3; i++) {
            token = tokeniser.read();
            Assert.assertEquals("Character", CHARACTER, token.type);
            Assert.assertEquals("a", token.data);
            reader.advance();
        }

        token = tokeniser.read();
        Assert.assertEquals("StartTag", START_TAG, token.type);
        Assert.assertNull(token.data);

        tokeniser.emit(new Token.Character('>'));
        token = tokeniser.read();
        Assert.assertEquals("EndTag", END_TAG, token.type);
        Assert.assertNull(token.data);

        tokeniser.transition(TokeniserState.Empty);
        tokeniser.transition(TokeniserState.Character);
        tokeniser.transition(TokeniserState.Data);
        tokeniser.transition(TokeniserState.TagOpen);
        tokeniser.transition(TokeniserState.TagName);
        tokeniser.transition(TokeniserState.TagAttributes);
        tokeniser.transition(TokeniserState.TagEnd);
        tokeniser.transition(TokeniserState.Text);
        tokeniser.transition(TokeniserState.Script);
        tokeniser.transition(TokeniserState.Doctype);
        tokeniser.transition(TokeniserState.Error);
    }

    @Test
    public void testTokeniserErrorHandling() {
        CharacterReader reader = new CharacterReader("&foo; <bar") {
            @Override
            public int pos() {
                return 0;
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token;
        for (int i = 0; i < 2; i++) {
            token = tokeniser.read();
            Assert.assertEquals("Character", CHARACTER, token.type);
            Assert.assertEquals("&", token.data);
            reader.advance();
        }

        tokeniser.transition(TokeniserState.Character);
        tokeniser.transition(TokeniserState.Error);

        token = tokeniser.read();
        Assert.assertEquals("Error", ERROR, token.type);
        Assert.assertEquals("Unexpected character in input", token.errorMessage);
        Assert.assertEquals(1, token.line);
        Assert.assertEquals(2, token.column);

        tokeniser.setTrackErrors(false);
        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testTokeniserCharacterReference() {
        CharacterReader reader = new CharacterReader("&#x1F600;") {
            @Override
            public int pos() {
                return 0;
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();
        Assert.assertEquals("Character", CHARACTER, token.type);
        Assert.assertEquals("\uD834\uDD1E", token.data);
    }

    @Test
    public void testTokeniserNamedCharacterReference() {
        CharacterReader reader = new CharacterReader("&copy;") {
            @Override
            public int pos() {
                return 0;
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();
        Assert.assertEquals("Character", CHARACTER, token.type);
        Assert.assertEquals("\u00A9", token.data);
    }

    @Test
    public void testTokeniserErrorHandlingWithTrackErrors() {
        CharacterReader reader = new CharacterReader("&foo; <bar") {
            @Override
            public int pos() {
                return 0;
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.setTrackErrors(true);

        Token token;
        for (int i = 0; i < 2; i++) {
            token = tokeniser.read();
            Assert.assertEquals("Character", CHARACTER, token.type);
            Assert.assertEquals("&", token.data);
            reader.advance();
        }

        tokeniser.transition(TokeniserState.Character);
        tokeniser.transition(TokeniserState.Error);

        token = tokeniser.read();
        Assert.assertEquals("Error", ERROR, token.type);
        Assert.assertEquals("Unexpected character in input", token.errorMessage);
        Assert.assertEquals(1, token.line);
        Assert.assertEquals(3, token.column);

        List<ParseError> errors = tokeniser.getErrors();
        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Unexpected character in input", error.errorMessage);
        Assert.assertEquals("&", error.character);
        Assert.assertEquals(1, error.line);
        Assert.assertEquals(3, error.column);
    }
}