package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.Comment;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testTokeniser() {
        CharacterReader readerMock = new CharacterReaderMock();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(readerMock, errors);

        Token token = tokeniser.read();
        assertNull(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("H", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("T", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.tagName);

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("head", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("title", token.tagName);

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(" ',", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("T", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("i", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("l", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("e", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("title", token.tagName);

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("/", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("h", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("m", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("l", token.data());

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("head", token.tagName);

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.tagName);

        tokeniser.emit(token);

        token = tokeniser.read();
        assertNull(token);

        Verify.assertErrorCount(errors, 0);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader readerMock = new CharacterReaderMock();
        Tokeniser tokeniser = new Tokeniser(readerMock, null);

        int[] codepoints = tokeniser.consumeCharacterReference('\u00A9', false);
        assertEquals(1, codepoints.length);
        assertEquals('\u00A9', codepoints[0]);

        codepoints = tokeniser.consumeCharacterReference('\u0026', false);
        assertNull(codepoints);

        codepoints = tokeniser.consumeCharacterReference(null, false);
        assertNull(codepoints);

        codepoints = tokeniser.consumeCharacterReference('\u0023', true);
        assertEquals(1, codepoints.length);
        assertEquals('\u0023', codepoints[0]);

        codepoints = tokeniser.consumeCharacterReference("\u00A1\u0327", true);
        assertEquals(2, codepoints.length);
        assertEquals('\u00A1', codepoints[0]);
        assertEquals('\u0327', codepoints[1]);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        Tag tagPending = tokeniser.createTagPending(true);
        assertSame(Tag.StartTag.class, tagPending.getClass());
        tagPending.tagName = "div";
        tagPending.attributes = null;
        tagPending.selfClosing = false;

        tagPending = tokeniser.createTagPending(false);
        assertSame(Tag.EndTag.class, tagPending.getClass());
        tagPending.tagName = "div";
        tagPending.attributes = null;
    }

    @Test
    public void testErrorHandling() {
        CharacterReader readerMock = new CharacterReaderMock();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(readerMock, errors);

        tokeniser.error(TokeniserState.AfterEndTag);
        Verify.assertErrorCount(errors, 1);
        assertEquals("Unexpected character 'X' in input state [AfterEndTag]", errors.get(0).getMessage());

        tokeniser.error(TokeniserState.AfterStartTag);
        Verify.assertErrorCount(errors, 2);
        assertEquals("Unexpected character 'X' in input state [AfterStartTag]", errors.get(1).getMessage());

        tokeniser.eofError(TokeniserState.AfterEndTag);
        Verify.assertErrorCount(errors, 3);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [AfterEndTag]", errors.get(2).getMessage());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        assertFalse(tokeniser.isAppropriateEndTagToken());

        tokeniser.tagPending = new StartTag("div");
        assertTrue(tokeniser.isAppropriateEndTagToken());
        assertEquals("div", tokeniser.appropriateEndTagName());

        tokeniser.tagPending = new EndTag("div");
        assertFalse(tokeniser.isAppropriateEndTagToken());

        tokeniser.tagPending = new StartTag("div");
        tokeniser.tagPending.selfClosing = true;
        assertTrue(tokeniser.isAppropriateEndTagToken());
        assertEquals("div", tokeniser.appropriateEndTagName());

        tokeniser.tagPending = new StartTag("div");
        tokeniser.tagPending.selfClosing = false;
        tokeniser.tagPending.name = "span";
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
}

class CharacterReaderMock implements CharacterReader {
    private String current = "";

    @Override
    public boolean isEmpty() {
        return current.length() == 0;
    }

    @Override
    public char current() {
        return current.charAt(0);
    }

    @Override
    public void mark(int readAheadLimit) {}

    @Override
    public void reset() {
        current = "";
    }

    @Override
    public void advance() {
        current = current.substring(1);
    }

    @Override
    public void match(char expected) {
        if (current.charAt(0) != expected)
            throw new AssertionError("Unexpected character '" + current.charAt(0) + "'");

        current = current.substring(1);
    }

    @Override
    public void consume(int length) {
        current = current.substring(length);
    }

    @Override
    public void consumeLetter() {
        match(Character.isLetter(current.charAt(0)) ? current.charAt(0) : '\u0000');
    }

    @Override
    public void consumeDigit() {
        match(Character.isDigit(current.charAt(0)) ? current.charAt(0) : '\u0000');
    }

    @Override
    public void consume(char expected) {
        match(expected);
    }

    @Override
    public void consumeTo(char expected) {
        int index = current.indexOf(expected);
        if (index < 0)
            throw new AssertionError("Unexpected characters '" + current + "'");

        current = current.substring(index + 1);
    }

    @Override
    public void consumeHexSequence() {
        int start = current.length();
        int index = current.indexOf(">", 0);
        if (index < 0)
            throw new AssertionError("Unexpected characters '" + current + "'");

        String hex = current.substring(start, index);
        int value = Integer.parseInt(hex, 16);

        current = current.substring(index + 1);

        match((char) value);
    }

    @Override
    public void consumeDigitSequence() {
        int start = current.length();
        int index = current.indexOf(" ", 0);
        if (index < 0)
            throw new AssertionError("Unexpected characters '" + current + "'");

        String digits = current.substring(start, index);
        int value = Integer.parseInt(digits);

        current = current.substring(index + 1);

        match((char) value);
    }

    @Override
    public void matches(char... expectedChars) {
        for (char expectedChar : expectedChars) {
            if (current.charAt(0) != expectedChar)
                throw new AssertionError("Unexpected character '" + current.charAt(0) + "'");

            current = current.substring(1);
        }
    }

    @Override
    public void matchesAny(char... expectedChars) {
        if (current.isEmpty() || Arrays.binarySearch(notCharRefCharsSorted, current.charAt(0)) >= 0)
            throw new AssertionError("Unexpected character '" + current.charAt(0) + "'");

        current = current.substring(1);
    }

    @Override
    public void matchesAnyIgnoringCase(char... expectedChars) {
        if (current.isEmpty() || Arrays.binarySearch(notCharRefCharsSorted, Character.toUpperCase(current.charAt(0))) >= 0)
            throw new AssertionError("Unexpected character '" + current.charAt(0) + "'");

        current = current.substring(1);
    }

    @Override
    public void matchesLetter() {
        if (!Character.isLetter(current.charAt(0)))
            throw new AssertionError("Unexpected character '" + current.charAt(0) + "'");

        current = current.substring(1);
    }

    @Override
    public void matchesDigit() {
        if (!Character.isDigit(current.charAt(0)))
            throw new AssertionError("Unexpected character '" + current.charAt(0) + "'");

        current = current.substring(1);
    }
}

class Verify {
    public static void assertErrorCount(ParseErrorList errors, int expectedCount) {
        assertEquals("Unexpected number of errors: " + errors.size(), expectedCount, errors.size());
    }
}