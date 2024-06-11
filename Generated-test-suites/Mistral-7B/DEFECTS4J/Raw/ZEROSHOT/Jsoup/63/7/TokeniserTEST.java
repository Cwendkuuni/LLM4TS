package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_EmptyInput() {
        Token token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_SingleCharacter() {
        reader.setCurrentPosition(0);
        reader.setCurrentCharacter('a');

        Token token = tokeniser.read();
        Token expectedToken = new Token.Character('a');
        Assert.assertEquals(expectedToken, token);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_SelfClosingTag() {
        reader.setCurrentPosition(0);
        reader.setCurrentCharacter('<');
        reader.consume();
        reader.setCurrentCharacter('i');
        reader.consume();
        reader.setCurrentCharacter('m');
        reader.consume();
        reader.setCurrentCharacter('g');
        reader.consume();
        reader.setCurrentCharacter('/>');

        tokeniser.read();

        Token expectedToken = new Token.StartTag(new Token.Tag("img")); {
            this.selfClosing = true;
        };
        Assert.assertEquals(expectedToken, tokeniser.emitPending);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_NormalTag() {
        reader.setCurrentPosition(0);
        reader.setCurrentCharacter('<');
        reader.consume();
        reader.setCurrentCharacter('h');
        reader.consume();
        reader.setCurrentCharacter('1');
        reader.consume();
        reader.setCurrentCharacter(' ');
        reader.consume();
        reader.setCurrentCharacter('t');
        reader.consume();
        reader.setCurrentCharacter('a');
        reader.consume();
        reader.setCurrentCharacter('g');
        reader.consume();
        reader.setCurrentCharacter('>');

        tokeniser.read();

        Token expectedToken = new Token.StartTag(new Token.Tag("html"));
        Assert.assertEquals(expectedToken, tokeniser.emitPending);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_CharacterReference() {
        reader.setCurrentPosition(0);
        reader.setCurrentCharacter('&');
        reader.consume();
        reader.setCurrentCharacter('#');
        reader.consume();
        reader.setCurrentCharacter('x');
        reader.consume();
        reader.setCurrentCharacter('a');
        reader.consume();
        reader.setCurrentCharacter('m');
        reader.consume();
        reader.setCurrentCharacter('p');
        reader.consume();
        reader.setCurrentCharacter(';');

        tokeniser.read();

        Token expectedToken = new Token.Character('\u0026');
        Assert.assertEquals(expectedToken, tokeniser.emitPending);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_InvalidCharacterReference() {
        reader.setCurrentPosition(0);
        reader.setCurrentCharacter('&');
        reader.consume();
        reader.setCurrentCharacter('i');
        reader.consume();
        reader.setCurrentCharacter('n');
        reader.consume();
        reader.setCurrentCharacter('v');
        reader.consume();
        reader.setCurrentCharacter('i');
        reader.consume();
        reader.setCurrentCharacter('d');
        reader.consume();
        reader.setCurrentCharacter(';');

        tokeniser.read();

        Assert.assertNull(tokeniser.emitPending);
        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference: invalid named referenece 'invalid'", error.getMessage());
    }

    @Test
    public void testRead_NamedCharacterReference() {
        reader.setCurrentPosition(0);
        reader.setCurrentCharacter('&');
        reader.consume();
        reader.setCurrentCharacter('a');
        reader.consume();
        reader.setCurrentCharacter('m');
        reader.consume();
        reader.setCurrentCharacter('p');
        reader.consume();
        reader.setCurrentCharacter(';');

        tokeniser.read();

        Token expectedToken = new Token.Character('&');
        Assert.assertEquals(expectedToken, tokeniser.emitPending);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_EOF() {
        reader.setCurrentPosition(0);
        reader.setCurrentCharacter('a');
        reader.consume();
        reader.consume();

        tokeniser.read();
        tokeniser.read();

        tokeniser.read();

        Assert.assertNull(tokeniser.emitPending);
        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.getMessage());
    }
}