package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("test.html");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        while ((token = tokeniser.read()) != null) {
            System.out.println(token.toString());
        }

        reader.close();
        errors.clear();
    }

    @Test
    public void testEmitPending() {
        CharacterReader reader = new CharacterReader("test.html");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        for (int i = 0; i < 10; i++) {
            tokeniser.emit(new Token.Character(Character.forDigit(i, 10)));
            Token token = tokeniser.read();
            assertEquals(String.valueOf((char) (i + 48)), token.toString());
        }

        reader.close();
        errors.clear();
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        Util util = new Util();
        assertEquals("foo", util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo ", util.stripLeadingAndTrailingQuotes("foo \" "));
        assertEquals("\"foo\" bar", util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertNull(util.stripLeadingAndTrailingQuotes(null));
    }

    @Test
    public void testStripLeadingHyphens() {
        Util util = new Util();
        assertEquals("f", util.stripLeadingHyphens("-f"));
        assertEquals("foo", util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", util.stripLeadingHyphens("---foo"));
        assertNull(util.stripLeadingHyphens(null));
    }

    @Test
    public void testReadWithErrors() {
        CharacterReader reader = new CharacterReader("test.html"); // A file with errors
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        while ((token = tokeniser.read()) != null) {
            System.out.println(token.toString());
        }

        assertEquals(5, errors.size()); // Errors should be added

        reader.close();
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("test.html");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);

        character = tokeniser.consumeCharacterReference('&', false);
        assertNull(character);

        character = tokeniser.consumeCharacterReference(null, true);
        assertNull(character);

        character = tokeniser.consumeCharacterReference("amp", false);
        assertEquals('&', character);

        character = tokeniser.consumeCharacterReference("quot", false);
        assertEquals('"', character);

        character = tokeniser.consumeCharacterReference("apos", false);
        assertEquals('\'', character);

        character = tokeniser.consumeCharacterReference("apos", true);
        assertEquals('\'', character);

        character = tokeniser.consumeCharacterReference("quot", true);
        assertEquals('"', character);

        reader.close();
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tagPending.tagName = "div";
        tokeniser.emitTagPending();

        Token tag = tokeniser.read();
        assertEquals("div", tag.toString());

        tagPending = tokeniser.createTagPending(false);
        tagPending.tagName = "script";
        tokeniser.emitTagPending();

        tag = tokeniser.read();
        assertEquals("script", tag.toString());

        reader.close();
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = new CharacterReader("test.html");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // <html>
        tokeniser.read(); // <head>
        tokeniser.read(); // <title>
        tokeniser.read(); // Title text
        tokeniser.read(); // </title>
        tokeniser.read(); // <body>
        tokeniser.read(); // <h1>
        tokeniser.read(); // Heading text
        tokeniser.read(); // </h1>

        assertTrue(tokeniser.isAppropriateEndTagToken());
        tokeniser.read(); // </body>
        assertFalse(tokeniser.isAppropriateEndTagToken());

        reader.close();
        errors.clear();
    }

    @Test
    public void testErrorHandling() {
        CharacterReader reader = new CharacterReader("test.html"); // A file with errors
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        while ((token = tokeniser.read()) != null) {
            System.out.println(token.toString());
        }

        assertEquals(5, errors.size()); // Errors should be added

        reader.close();
    }

    @Test
    public void testAdvanceTransitionWithErrors() {
        CharacterReader reader = new CharacterReader("test.html"); // A file with errors
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.AfterTagName);
        tokeniser.advanceTransition(TokeniserState.AfterTagNameAttribute);
        tokeniser.consumeCharacter('=');
        tokeniser.consumeCharacter('"');
        tokeniser.consumeCharacter('color');
        tokeniser.consumeCharacter('"');
        tokeniser.consumeCharacter('=');
        tokeniser.consumeCharacter('r');
        tokeniser.consumeCharacter('e');
        tokeniser.consumeCharacter('d');
        tokeniser.consumeCharacter(';');
        tokeniser.consumeCharacter('/');

        assertTrue(errors.containsErrorMessage("Unexpected character 'd' in input state [AfterTagNameAttribute]"));

        reader.close();
        errors.clear();
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertEquals("<!DOCTYPE html>", token.toString());

        reader.close();
    }

    @Test
    public void testErrorHandlingWithDoctype() {
        CharacterReader reader = new CharacterReader("test.html"); // A file with errors
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // <!DOCTYPE html>
        tokeniser.transition(TokeniserState.AfterDoctypePublic);
        tokeniser.consumeCharacter('>');

        tokeniser.transition(TokeniserState.AfterTagName);
        tokeniser.advanceTransition(TokeniserState.AfterTagNameAttribute);
        tokeniser.consumeCharacter('=');
        tokeniser.consumeCharacter('"');
        tokeniser.consumeCharacter('color');
        tokeniser.consumeCharacter('"');
        tokeniser.consumeCharacter('=');
        tokeniser.consumeCharacter('r');
        tokeniser.consumeCharacter('e');
        tokeniser.consumeCharacter('d');
        tokeniser.consumeCharacter(';');

        assertTrue(errors.containsErrorMessage("Unexpected character 'd' in input state [AfterTagNameAttribute]"));

        reader.close();
        errors.clear();
    }
}