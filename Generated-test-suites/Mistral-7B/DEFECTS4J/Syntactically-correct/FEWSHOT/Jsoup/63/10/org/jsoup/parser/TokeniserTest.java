package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.Tokeniser.replacementChar;
import static org.jsoup.parser.Tokeniser.codepointHolder;
import static org.jsoup.parser.Tokeniser.multipointHolder;

public class TokeniserTest {
    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Test
    public void testTokeniser() {
        reader = new CharacterReader("") {};
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
        Assert.assertEquals(TokeniserState.Data, tokeniser.getState());

        // Test read method with no tokens emitted
        Token token = tokeniser.read();
        Assert.assertNull(token);

        // Test emit method
        String expectedData = "Hello World";
        tokeniser.emit(new Token.Character().data(expectedData));
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CharacterData, token.type);
        Assert.assertEquals(expectedData, token.data());
    }

    @Test
    public void testReadCharacterReference() {
        reader = new CharacterReader("&amp;") {};
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);

        int[] codePoints = tokeniser.consumeCharacterReference(null, false);
        Assert.assertArrayEquals(new int[]{0x0026}, codePoints);

        tokeniser.consume(1); // consume &
        tokeniser.consume(1); // consume amp;
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CharacterData, token.type);
        Assert.assertEquals("", token.data());
    }

    @Test
    public void testReadNamedCharacterReference() {
        reader = new CharacterReader("&copy;") {};
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);

        int[] codePoints = tokeniser.consumeCharacterReference('c', false);
        Assert.assertArrayEquals(new int[]{0x00A9}, codePoints);

        tokeniser.consume(1); // consume &
        tokeniser.consume(4); // consume copy;
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CharacterData, token.type);
        Assert.assertEquals("", token.data());
    }

    @Test
    public void testReadCharacterReferenceInAttribute() {
        reader = new CharacterReader("abc&def") {};
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);

        int[] codePoints = tokeniser.consumeCharacterReference(null, true);
        Assert.assertNull(codePoints);

        tokeniser.consume(1); // consume a
        tokeniser.consume(1); // consume b
        tokeniser.consume(1); // consume c
        tokeniser.consume(1); // consume &
        tokeniser.consume(3); // consume def
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CharacterData, token.type);
        Assert.assertEquals("abc", token.data());
    }

    @Test
    public void testReadCharacterReferenceWithInvalidCharacter() {
        reader = new CharacterReader("&invalid") {};
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);

        int[] codePoints = tokeniser.consumeCharacterReference(null, false);
        Assert.assertNull(codePoints);

        tokeniser.consume(1); // consume &
        tokeniser.consume(6); // consume invalid
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CharacterData, token.type);
        Assert.assertEquals("&invalid", token.data());

        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testReadCharacterReferenceWithInvalidCharacterInAttribute() {
        reader = new CharacterReader("abc&def&invalid") {};
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);

        int[] codePoints = tokeniser.consumeCharacterReference(null, true);
        Assert.assertNull(codePoints);

        tokeniser.consume(1); // consume a
        tokeniser.consume(1); // consume b
        tokeniser.consume(1); // consume c
        tokeniser.consume(1); // consume &
        tokeniser.consume(3); // consume def
        tokeniser.consume(1); // consume &
        tokeniser.consume(7); // consume invalid
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CharacterData, token.type);
        Assert.assertEquals("abcdef", token.data());

        Assert.assertTrue(errors.hasErrors());
    }
}