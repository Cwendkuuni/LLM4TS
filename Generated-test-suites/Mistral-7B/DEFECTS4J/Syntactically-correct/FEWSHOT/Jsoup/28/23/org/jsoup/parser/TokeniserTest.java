package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Tokeniser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniser() {
        CharacterReader reader = new CharacterReader() {
            private int position = 0;
            private char[] data = {'<', 'h', 't', 'm', 'l', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', '<', '/', 'h', 't', 'm', 'l', ' ', '>', '\n'};
            @Override
            public int pos() {
                return position;
            }

            @Override
            public char current() {
                return data[position];
            }

            @Override
            public void mark(int readAheadLimit) {
            }

            @Override
            public void rewind(int readAheadLimit) {
                position -= 1;
            }

            @Override
            public void advance() {
                position++;
            }

            @Override
            public boolean isEmpty() {
                return position >= data.length;
            }

            @Override
            public boolean matches(char c) {
                return current() == c;
            }

            @Override
            public boolean matchesIgnoreCase(char c) {
                return Character.toUpperCase(current()) == Character.toUpperCase(c);
            }

            @Override
            public boolean matchesAny(char... allowed) {
                for (char c : allowed) {
                    if (matches(c)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean matchesLetter() {
                return Character.isLetter(current());
            }

            @Override
            public boolean matchesDigit() {
                return Character.isDigit(current());
            }

            @Override
            public String consumeLetterThenDigitSequence() {
                StringBuilder sb = new StringBuilder();
                while (Character.isAlphabetic(current())) {
                    sb.append(current());
                    advance();
                }
                if (Character.isDigit(current())) {
                    sb.append(consumeDigitSequence());
                }
                return sb.toString();
            }

            @Override
            public String consumeDigitSequence() {
                StringBuilder sb = new StringBuilder();
                while (Character.isDigit(current())) {
                    sb.append(current());
                    advance();
                }
                return sb.toString();
            }

            @Override
            public String consumeHexSequence() {
                StringBuilder sb = new StringBuilder();
                boolean inHex = true;
                while (inHex) {
                    char c = current();
                    if (Character.isDigit(c)) {
                        sb.append(c);
                        advance();
                    } else if (Character.isLetter(c)) {
                        sb.append((char) (Character.digit(Character.toUpperCase(c), 16)));
                        advance();
                    } else {
                        inHex = false;
                    }
                }
                return sb.toString();
            }
        };

        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        List<Token> tokens = new ArrayList<>();

        while ((token = tokeniser.read()) != null) {
            tokens.add(token);
        }

        // Test reading of characters
        assertEquals("html is a test", tokens.get(0).toString());
        assertEquals(" ", tokens.get(1).toString());
        assertEquals("a", tokens.get(2).toString());
        assertEquals(" ", tokens.get(3).toString());
        assertEquals("t", tokens.get(4).toString());
        assertEquals("e", tokens.get(5).toString());
        assertEquals("s", tokens.get(6).toString());
        assertEquals("t", tokens.get(7).toString());
        assertEquals("/html", tokens.get(8).toString());
        assertNull(tokens.get(9)); // end of file

        // Test reading of start tag
        Token startTag = tokens.get(2);
        assertEquals("a", startTag.tagName);
        assertEquals("a", startTag.tagNameLowercase);
        assertEquals(1, startTag.attributes.size());
        assertEquals("", startTag.tagText);

        // Test reading of end tag
        Token endTag = tokens.get(8);
        assertEquals("/html", endTag.tagName);
        assertEquals("/html", endTag.tagNameLowercase);
        assertNull(endTag.tagText);
        assertNull(endTag.attributes);

        // Test error handling
        reader.data = new char[]{'<', '/', 'h', 't', 'm', 'l', '>', '\n'}; // missing 't' in 'html'
        tokeniser = new Tokeniser(new CharacterReader() { /* implementation */ }, errors);
        tokeniser.read();
        Token errorToken = tokeniser.read();
        assertEquals("Unexpected character '<' in input state [Data]", errorToken.errorMessage);
        assertNull(errorToken.tagName);
        assertNull(errorToken.tagNameLowercase);
        assertNull(errorToken.tagText);
        assertNull(errorToken.attributes);
    }
}