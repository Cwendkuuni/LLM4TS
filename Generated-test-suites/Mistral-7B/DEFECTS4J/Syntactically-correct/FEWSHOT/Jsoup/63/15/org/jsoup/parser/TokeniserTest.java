package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testRead() {
        CharacterReader reader = new MockCharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        while ((token = tokeniser.read()) != null) {
            System.out.println(token.toString());
        }

        assertEquals(13, errors.size()); // Expected number of errors
    }

    @Test
    public void testEmit() {
        CharacterReader reader = new MockCharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        String expected = "test";
        tokeniser.emit(new Token.Character());
        tokeniser.emit(expected);
        Token token = tokeniser.read();
        assertEquals(expected, token.data());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new MockCharacterReader("test&#x1F60A;");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        int[] result = tokeniser.consumeCharacterReference(null, false);
        assertEquals(0x1F60A, result[0]);
    }

    @Test(expected = AssertionError.class)
    public void testConsumeCharacterReferenceInvalidCharacter() {
        CharacterReader reader = new MockCharacterReader("test&#x0;");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        tokeniser.consumeCharacterReference(null, false);
    }

    @Test(expected = AssertionError.class)
    public void testConsumeCharacterReferenceInvalidSequence() {
        CharacterReader reader = new MockCharacterReader("test&#x1F60A;&#x1F60B;");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        tokeniser.consumeCharacterReference(null, false);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new MockCharacterReader(""), new ParseErrorList());
        Token.StartTag startTag = tokeniser.createTagPending(true);
        assertEquals("", startTag.tagName());
        Token.EndTag endTag = tokeniser.createTagPending(false);
        assertEquals("", endTag.tagName());
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new MockCharacterReader("test<tag>test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertEquals("tag", token.tagName());
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new MockCharacterReader(""), new ParseErrorList());
        Token.Doctype doctype = tokeniser.createDoctypePending();
        assertEquals("", doctype.systemId());
        assertEquals("", doctype.publicId());
    }

    @Test
    public void testEmitDoctypePending() {
        CharacterReader reader = new MockCharacterReader("<!DOCTYPE html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertEquals("html", token.tagName());
    }

    @Test
    public void testUnescapeEntities() {
        CharacterReader reader = new MockCharacterReader("&#x1F60A;&#x1F60B;");
        String result = new Tokeniser(reader, new ParseErrorList()).unescapeEntities(false);
        assertEquals("\uD83C\uDF0A\uD83C\uDF0B", result);
    }

    @Test
    public void testUnescapeEntitiesInAttribute() {
        CharacterReader reader = new MockCharacterReader("class=\"foo&bar\"");
        String result = new Tokeniser(reader, new ParseErrorList()).unescapeEntities(true);
        assertEquals("class=\"foo&amp;bar\"", result);
    }
}

class MockCharacterReader implements CharacterReader {
    private String input;
    private int index;

    public MockCharacterReader(String input) {
        this.input = input;
        this.index = 0;
    }

    @Override
    public boolean isEmpty() {
        return index >= input.length();
    }

    @Override
    public char current() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        return input.charAt(index);
    }

    @Override
    public void mark(int readAheadLimit) {
        // No-op
    }

    @Override
    public void reset() {
        index = 0;
    }

    @Override
    public void advance() {
        index++;
    }

    @Override
    public boolean matches(char expected) {
        if (isEmpty()) {
            return false;
        }
        return current() == expected;
    }

    @Override
    public boolean matchesAny(char... expected) {
        if (isEmpty()) {
            return false;
        }
        return Arrays.binarySearch(expected, current()) >= 0;
    }

    @Override
    public boolean matchesAnySorted(char[] expected) {
        if (isEmpty()) {
            return false;
        }
        int index = Arrays.binarySearch(expected, current());
        return index >= 0;
    }

    @Override
    public boolean matchesLetter() {
        if (isEmpty()) {
            return false;
        }
        return Character.isLetter(current());
    }

    @Override
    public boolean matchesDigit() {
        if (isEmpty()) {
            return false;
        }
        return Character.isDigit(current());
    }

    @Override
    public boolean matchesIgnoreCase(String expected) {
        if (isEmpty()) {
            return false;
        }
        return input.regionMatches(true, index, expected, 0, expected.length());
    }

    @Override
    public boolean matchesIgnoreCase(char expected) {
        if (isEmpty()) {
            return false;
        }
        return current() == expected || Character.toLowerCase(current()) == Character.toLowerCase(expected);
    }

    @Override
    public String consumeLetterThenDigitSequence() {
        StringBuilder result = new StringBuilder();
        while (matchesLetter()) {
            result.append(current());
            advance();
        }
        if (!matchesDigit()) {
            throw new AssertionError("Expected digit after letters.");
        }
        result.append(current());
        advance();
        return result.toString();
    }

    @Override
    public String consumeDigitSequence() {
        StringBuilder result = new StringBuilder();
        while (matchesDigit()) {
            result.append(current());
            advance();
        }
        if (!isEmpty()) {
            throw new AssertionError("Unexpected character found: " + current());
        }
        return result.toString();
    }

    @Override
    public String consumeHexSequence() {
        StringBuilder result = new StringBuilder();
        boolean inHex = true;
        while (inHex) {
            char c = current();
            if (Character.isDigit(c)) {
                result.append(c);
                advance();
            } else if (Character.isLetter(c)) {
                result.append((char) (Character.toUpperCase(c) - 'A' + 0x10));
                advance();
            } else if (c == ';') {
                inHex = false;
                advance();
            } else {
                throw new AssertionError("Unexpected character found: " + c);
            }
        }
        return result.toString();
    }
}