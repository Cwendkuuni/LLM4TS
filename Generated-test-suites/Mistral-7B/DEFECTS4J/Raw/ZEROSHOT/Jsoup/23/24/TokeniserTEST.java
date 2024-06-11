package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.CharacterInput;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokeniser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private CharacterInput input;
    private Tokeniser tokeniser;
    private List<ParseError> errors;

    @Before
    public void setUp() {
        input = new CharacterInput(new ByteArrayInputStream(new byte[]{}));
        errors = new ArrayList<>();
        tokeniser = new Tokeniser(input, new ArrayList<>());
    }

    @After
    public void tearDown() {
        if (tokeniser != null) {
            tokeniser.errors.clear();
            tokeniser = null;
        }
    }

    @Test
    public void testEmptyInput() {
        Token token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testBasicTokenisation() {
        input = new CharacterInput(new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes()));
        tokeniser = new Tokeniser(input, errors);

        Token token;

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("head", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("head", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("body", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("body", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.tagName);

        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testSelfClosingTags() {
        input = new CharacterInput(new ByteArrayInputStream("<html><img src=\"image.png\"/><body></body></html>".getBytes()));
        tokeniser = new Tokeniser(input, errors);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("img", token.tagName);
        Assert.assertTrue(token.selfClosing);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("img", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("body", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("body", token.tagName);

        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testCharacterReference() {
        input = new CharacterInput(new ByteArrayInputStream("&nbsp;".getBytes()));
        tokeniser = new Tokeniser(input, errors);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("\u00A0", token.text);

        Assert.assertTrue(errors.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testReadAfterEndOfInput() {
        input = new CharacterInput(new ByteArrayInputStream("<html>".getBytes()));
        tokeniser = new Tokeniser(input, errors);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);

        token = tokeniser.read(); // should throw exception
    }

    @Test
    public void testErrorHandling() {
        input = new CharacterInput(new ByteArrayInputStream("<html><body><script>alert('Hello World')</script>"));
        tokeniser = new Tokeniser(input, errors);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("body", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("script", token.tagName);

        token = tokeniser.read(); // should throw error

        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getMessage());
    }
}