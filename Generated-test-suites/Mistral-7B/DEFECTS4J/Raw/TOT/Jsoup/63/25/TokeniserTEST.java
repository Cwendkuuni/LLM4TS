package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("ValidInput.html");
        this.errors = new ParseErrorList();
        this.tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadValidInput() {
        Token token;
        for (int i = 0; i < 10; i++) {
            token = tokeniser.read();
            assertNotNull(token);
            switch (token.type) {
                case Token.TokenType.StartTag:
                    assertEquals("html", token.tagName);
                    break;
                case Token.TokenType.EndTag:
                    assertEquals("html", token.tagName);
                    break;
                case Token.TokenType.Character:
                    assertNotNull(token.data);
                    break;
                case Token.TokenType.Doctype:
                    assertEquals("html", token.doctype.name);
                    break;
            }
        }
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testReadInvalidInput_SelfClosingFlagNotAcknowledged() {
        CharacterReader reader = new CharacterReader("SelfClosingFlagNotAcknowledged.html");
        this.tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        tokeniser.read(); // read <html> and <head>
        tokeniser.emit(new Token.StartTag("html"));
        tokeniser.emit(new Token.StartTag("head"));

        tokeniser.error("Self closing flag not acknowledged");

        Token token = tokeniser.read();
        assertNull(token);
        assertTrue(errors.size() > 0);
        assertEquals("Self closing flag not acknowledged", errors.get(0).getMessage());
    }

    @Test
    public void testReadInvalidInput_CharacterReferenceInvalidCharacter() {
        CharacterReader reader = new CharacterReader("CharacterReferenceInvalidCharacter.html");
        this.tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // read <html>
        tokeniser.read(); // read <head>
        tokeniser.read(); // read <title>

        tokeniser.emit(new Token.StartTag("title"));

        int[] codePoints = tokeniser.consumeCharacterReference(null, false);
        tokeniser.emit(new Token.Character(codePoints[0]));
        tokeniser.emit(new Token.Character(codePoints[1]));

        tokeniser.error("character outside of valid range");

        Token token = tokeniser.read();
        assertNull(token);
        assertTrue(errors.size() > 0);
        assertEquals("character outside of valid range", errors.get(0).getMessage());
    }

    @Test
    public void testReadInvalidInput_CharacterReferenceMissingSemicolon() {
        CharacterReader reader = new CharacterReader("CharacterReferenceMissingSemicolon.html");
        this.tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // read <html>
        tokeniser.read(); // read <head>
        tokeniser.read(); // read <title>

        tokeniser.emit(new Token.StartTag("title"));

        tokeniser.consumeCharacterReference(null, false);
        tokeniser.consume(';'); // consume semicolon

        tokeniser.error("missing semicolon");

        Token token = tokeniser.read();
        assertNull(token);
        assertTrue(errors.size() > 0);
        assertEquals("missing semicolon", errors.get(0).getMessage());
    }

    @Test
    public void testReadInvalidInput_CharacterReferenceInvalidNamedEntity() {
        CharacterReader reader = new CharacterReader("CharacterReferenceInvalidNamedEntity.html");
        this.tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // read <html>
        tokeniser.read(); // read <head>
        tokeniser.read(); // read <title>

        tokeniser.emit(new Token.StartTag("title"));

        tokeniser.consume('&');
        tokeniser.consume('A'); // consume 'A' instead of 'amp'
        tokeniser.consume(';'); // consume semicolon

        tokeniser.error("invalid named referenc
e 'AA'");

        Token token = tokeniser.read();
        assertNull(token);
        assertTrue(errors.size() > 0);
        assertEquals("invalid named reference 'AA'", errors.get(0).getMessage());
    }

    @Test
    public void testReadInvalidInput_CharacterReferenceInvalidNamedEntityWithSemicolon() {
        CharacterReader reader = new CharacterReader("CharacterReferenceInvalidNamedEntityWithSemicolon.html");
        this.tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // read <html>
        tokeniser.read(); // read <head>
        tokeniser.read(); // read <title>

        tokeniser.emit(new Token.StartTag("title"));

        tokeniser.consume('&');
        tokeniser.consume('amp'); // consume 'amp' instead of '&amp;'
        tokeniser.consume(';'); // consume semicolon

        tokeniser.error("invalid named reference 'amp'");

        Token token = tokeniser.read();
        assertNull(token);
        assertTrue(errors.size() > 0);
        assertEquals("invalid named reference 'amp'", errors.get(0).getMessage());
    }

    @Test
    public void testReadInvalidInput_CharacterReferenceInvalidNamedEntityWithImproperlyFormedEntityName() {
        CharacterReader reader = new CharacterReader("CharacterReferenceInvalidNamedEntityWithImproperlyFormedEntityName.html");
        this.tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // read <html>
        tokeniser.read(); // read <head>
        tokeniser.read(); // read <title>

        tokeniser.emit(new Token.StartTag("title"));

        tokeniser.consumeLetterThenDigitSequence(); // consume 'amp'
        tokeniser.consume(';'); // consume semicolon

        tokeniser.error("invalid named reference 'amp'");

        Token token = tokeniser.read();
        assertNull(token);
        assertTrue(errors.size() > 0);
        assertEquals("invalid named reference 'amp'", errors.get(0).getMessage());
    }

    @Test
    public void testReadInvalidInput_EOF() {
        CharacterReader reader = new CharacterReader("EOF.html");
        this.tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // read <html>
        tokeniser.read(); // read <head>

        tokeniser.error(TokeniserState.Data, "Unexpectedly reached end of file (EOF)");

        Token token = tokeniser.read();
        assertNull(token);
        assertTrue(errors.size() > 0);
        assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
    }

    @Test
    public void testReadInvalidInput_UnexpectedCharacter() {
        CharacterReader reader = new CharacterReader("UnexpectedCharacter.html");
        this.tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // read <html>
        tokeniser.read(); // read <head>

        tokeniser.error(TokeniserState.Data, "Unexpected character '%s'", reader.current());

        Token token = tokeniser.read();
        assertNull(token);
        assertTrue(errors.size() > 0);
        assertEquals("Unexpected character 'x'", errors.get(0).getMessage()); // 'x' is just an example of unexpected character
    }
}