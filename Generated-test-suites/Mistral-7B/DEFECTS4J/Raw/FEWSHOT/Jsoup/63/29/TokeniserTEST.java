package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.jsoup.helper.Validate;

public class TokeniserTest {

    @Test
    public void testTokeniserBasicFunctionality() {
        CharacterReader readerMock = new CharacterReader() {
            private String input = "test<tag/>";
            private int position = 0;

            @Override
            public int read() throws IOException {
                if (position >= input.length()) return -1;
                return input.charAt(position++);
            }

            @Override
            public boolean isEmpty() {
                return position >= input.length();
            }

            @Override
            public char current() {
                return input.charAt(position);
            }

            @Override
            public void mark(int readAheadLimit) {}

            @Override
            public void rewind(int readAheadLimit) {
                position -= readAheadLimit;
            }

            @Override
            public boolean matches(char expected) {
                return input.charAt(position) == expected;
            }

            @Override
            public boolean matchesAny(char[] chars) {
                for (char c : chars) {
                    if (matches(c)) return true;
                }
                return false;
            }

            @Override
            public boolean matchesAnySorted(char[] chars) {
                for (char c : chars) {
                    if (input.charAt(position) == c) return true;
                    if (position >= input.length()) return false;
                    position++;
                }
                return false;
            }

            @Override
            public boolean matchesLetter() {
                return Character.isLetter(input.charAt(position));
            }

            @Override
            public boolean matchesDigit() {
                return Character.isDigit(input.charAt(position));
            }

            @Override
            public void advance() {
                position++;
            }
        };

        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(readerMock, errors);

        Token token;
        for (int i = 0; i < 10; i++) {
            token = tokeniser.read();
            if (token == null) break;
            System.out.println(token.toString());
        }

        assertEquals(Token.TokenType.StartTag, token.getType());
        assertEquals("tag", token.tagName());
        assertEquals(false, token.isSelfClosing());
        assertNull(token.attributes());

        assertEquals(Token.TokenType.EndTag, token.getType());
        assertNull(token.tagName());
        assertNull(token.attributes());

        token = tokeniser.read();
        assertEquals(Token.TokenType.EOF, token.getType());

        Validate.assertEquals(ParseError.class, errors.getClass());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character 't' in input state Data", error.getMessage());
    }

    @Test
    public void testCharacterReference() {
        CharacterReader readerMock = new CharacterReader() {
            private String input = "test&#123;";
            private int position = 0;

            @Override
            public int read() throws IOException {
                if (position >= input.length()) return -1;
                return input.charAt(position++);
            }

            @Override
            public boolean isEmpty() {
                return position >= input.length();
            }

            @Override
            public char current() {
                return input.charAt(position);
            }

            @Override
            public void mark(int readAheadLimit) {}

            @Override
            public void rewind(int readAheadLimit) {
                position -= readAheadLimit;
            }

            @Override
            public boolean matches(char expected) {
                return input.charAt(position) == expected;
            }

            @Override
            public boolean matchesAny(char[] chars) {
                for (char c : chars) {
                    if (matches(c)) return true;
                }
                return false;
            }

            @Override
            public boolean matchesAnySorted(char[] chars) {
                for (char c : chars) {
                    if (input.charAt(position) == c) return true;
                    if (position >= input.length()) return false;
                    position++;
                }
                return false;
            }

            @Override
            public boolean matchesLetter() {
                return Character.isLetter(input.charAt(position));
            }

            @Override
            public boolean matchesDigit() {
                return Character.isDigit(input.charAt(position));
            }

            @Override
            public void advance() {
                position++;
            }
        };

        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(readerMock, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals("\u007b", token.data());

        int[] codePoint = tokeniser.consumeCharacterReference(null, false);
        assertArrayEquals(new int[] { '\u007b' }, codePoint);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EOF, token.getType());

        Validate.assertEquals(ParseError.class, errors.getClass());
        assertEquals(0, errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        CharacterReader readerMock = new CharacterReader() {
            private String input = "test&#";
            private int position = 0;

            @Override
            public int read() throws IOException {
                if (position >= input.length()) return -1;
                return input.charAt(position++);
            }

            @Override
            public boolean isEmpty() {
                return position >= input.length();
            }

            @Override
            public char current() {
                return input.charAt(position);
            }

            @Override
            public void mark(int readAheadLimit) {}

            @Override
            public void rewind(int readAheadLimit) {
                position -= readAheadLimit;
            }

            @Override
            public boolean matches(char expected) {
                return input.charAt(position) == expected;
            }

            @Override
            public boolean matchesAny(char[] chars) {
                for (char c : chars) {
                    if (matches(c)) return true;
                }
                return false;
            }

            @Override
            public boolean matchesAnySorted(char[] chars) {
                for (char c : chars) {
                    if (input.charAt(position) == c) return true;
                    if (position >= input.length()) return false;
                    position++;
                }
                return false;
            }

            @Override
            public boolean matchesLetter() {
                return Character.isLetter(input.charAt(position));
            }

            @Override
            public boolean matchesDigit() {
                return Character.isDigit(input.charAt(position));
            }

            @Override
            public void advance() {
                position++;
            }
        };

        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(readerMock, errors);

        tokeniser.consumeCharacterReference(null, false);
        Validate.assertEquals(ParseError.class, errors.getClass());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference: missing semicolon", error.getMessage());
    }
}