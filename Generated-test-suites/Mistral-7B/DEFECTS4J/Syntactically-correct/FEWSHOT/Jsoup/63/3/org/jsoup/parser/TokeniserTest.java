package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testTokeniserInitialisation() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        assertNotNull(tokeniser);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokeniserNullReader() {
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(null, errors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokeniserNullErrors() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader, null);
    }

    @Test
    public void testTokeniserRead() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token1 = tokeniser.read();
        Token token2 = tokeniser.read();
        Token token3 = tokeniser.read();

        assertEquals(Token.TokenType.Character, token1.getType());
        assertEquals('a', token1.getData()[0]);

        assertEquals(Token.TokenType.Character, token2.getType());
        assertEquals('b', token2.getData()[0]);

        assertEquals(Token.TokenType.Character, token3.getType());
        assertEquals('c', token3.getData()[0]);

        assertEquals(null, tokeniser.read());
    }

    @Test
    public void testTokeniserReadError() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.error(TokeniserState.ErrorHandling);
        Token token1 = tokeniser.read();

        assertEquals(Token.TokenType.Error, token1.getType());
        assertEquals("Unexpected character 'a' in input state [ErrorHandling]", token1.getData()[0].toString());

        assertEquals(null, tokeniser.read());
    }

    @Test
    public void testTokeniserReadEof() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.error(TokeniserState.Data);
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        tokeniser.error(TokeniserState.ErrorHandling);
        Token token1 = tokeniser.read();

        assertEquals(Token.TokenType.Error, token1.getType());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [ErrorHandling]", token1.getData()[0].toString());

        assertEquals(null, tokeniser.read());
    }

    @Test
    public void testTokeniserEmit() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // read 'a'
        tokeniser.emit(new Token.Character('a'));

        tokeniser.read(); // read 'b'
        tokeniser.emit(new Token.Character('b'));

        tokeniser.read(); // read 'c'
        tokeniser.emit(new Token.Character('c'));

        assertEquals(null, tokeniser.read());
    }

    @Test
    public void testTokeniserEmitTagPending() {
        CharacterReader reader = new CharacterReader("<html><body><p>Hello World</p></body></html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // <
        tokeniser.read(); // h
        tokeniser.read(); // t
        tokeniser.read(); // m
        tokeniser.read(); // l
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.createTagPending(true);

        tokeniser.read(); // l
        tokeniser.read(); // i
        tokeniser.read(); // t
        tokeniser.read(); // e
        tokeniser.transition(TokeniserState.TagName);

        tokeniser.read(); // /
        tokeniser.read(); // p
        tokeniser.read(); // >
        tokeniser.transition(TokeniserState.EndTag);

        tokeniser.emitTagPending();

        tokeniser.read(); // </
        tokeniser.read(); // b
        tokeniser.read(); // o
        tokeniser.read(); // d
        tokeniser.read(); // y
        tokeniser.transition(TokeniserState.TagName);

        tokeniser.read(); // >
        tokeniser.emitTagPending();

        tokeniser.read(); // </
        tokeniser.read(); // h
        tokeniser.read(); // t
        tokeniser.read(); // m
        tokeniser.read(); // l
        tokeniser.transition(TokeniserState.EndTag);

        tokeniser.emitTagPending();

        assertEquals(null, tokeniser.read());
    }

    @Test
    public void testTokeniserConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&nbsp;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        int[] result = tokeniser.consumeCharacterReference(null, false);

        assertEquals(2, result.length);
        assertEquals(0x00A0, result[0]);
        assertEquals(0, result[1]);
    }

    @Test
    public void testTokeniserConsumeCharacterReferenceInvalid() {
        CharacterReader reader = new CharacterReader("&");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        int[] result = tokeniser.consumeCharacterReference(null, false);

        assertNull(result);
    }

    @Test
    public void testTokeniserConsumeCharacterReferenceInvalidNamed() {
        CharacterReader reader = new CharacterReader("&foo;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        int[] result = tokeniser.consumeCharacterReference(null, false);

        assertNull(result);
    }

    @Test
    public void testTokeniserConsumeCharacterReferenceValidNamed() {
        CharacterReader reader = new CharacterReader("&nbsp;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        int[] result = tokeniser.consumeCharacterReference(null, true);

        assertEquals(1, result.length);
        assertEquals(0x00A0, result[0]);
    }

    @Test
    public void testTokeniserUnescapeEntities() {
        CharacterReader reader = new CharacterReader("&nbsp; &eacute; &oelig; &Ouml;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        String result = tokeniser.unescapeEntities(false);

        assertEquals("&#160; &#233; &#246; &#214;", result);
    }

    @Test
    public void testTokeniserUnescapeEntitiesInAttribute() {
        CharacterReader reader = new CharacterReader("class='col&sharp;or-&eacute;-&oelig;ld'");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        String result = tokeniser.unescapeEntities(true);

        assertEquals("class='col$&sharp;or-é-öld'", result);
    }
}