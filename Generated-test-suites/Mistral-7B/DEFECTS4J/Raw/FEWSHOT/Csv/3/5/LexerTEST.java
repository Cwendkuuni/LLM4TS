package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LexerTest {

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new TestLexer(Constants.COMMA, '\u0009'); // tab char
        for (int i = 0; i < 256; i++) {
            boolean result = lexer.isWhitespace((char) i);
            if (Character.isWhitespace((char) i)) {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
        }
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new TestLexer(Constants.COMMA, '\n');
        for (int i = 0; i < 256; i++) {
            boolean result = lexer.isStartOfLine((char) i);
            if (i == '\n' || i == '\r') {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
        }
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new TestLexer(Constants.COMMA, '\u0000');
        for (int i = 0; i < 256; i++) {
            boolean result = lexer.isEndOfFile((char) i);
            if (i == -1) {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
        }
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new TestLexer(',', ',');
        for (int i = 0; i < 256; i++) {
            boolean result = lexer.isDelimiter((char) i);
            boolean expected = i == lexer.delimiter;
            assertTrue(result == expected);
        }
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new TestLexer(',', '\\');
        for (int i = 0; i < 256; i++) {
            boolean result = lexer.isEscape((char) i);
            boolean expected = i == lexer.escape;
            assertTrue(result == expected);
        }
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new TestLexer(',', '\'');
        for (int i = 0; i < 256; i++) {
            boolean result = lexer.isQuoteChar((char) i);
            boolean expected = i == lexer.quoteChar;
            assertTrue(result == expected);
        }
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new TestLexer(',', '%');
        for (int i = 0; i < 256; i++) {
            boolean result = lexer.isCommentStart((char) i);
            boolean expected = i == lexer.commmentStart;
            assertTrue(result == expected);
        }
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new TestLexer(Constants.COMMA, '\n');
        assertTrue(lexer.readEndOfLine('\n'));
        assertTrue(lexer.readEndOfLine('\r'));
        assertTrue(lexer.readEndOfLine('\r' + '\n'));
        assertFalse(lexer.readEndOfLine(' '));
        assertFalse(lexer.readEndOfLine('\u0000'));
        assertFalse(lexer.readEndOfLine(0x10FFFF)); // max unicode code point
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("foo bar\t baz\n".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream), null, null, null, null, null);
        StringBuilder buffer = new StringBuilder();
        buffer.append(reader.readLine());

        Lexer lexer = new TestLexer(Constants.COMMA, ' ');
        lexer.in = reader;
        lexer.trimTrailingSpaces(buffer);

        assertEquals("foo bar baz", buffer.toString());
    }

    private static class TestLexer extends Lexer {
        TestLexer(char delimiter, char escape) {
            super(null, null, delimiter, escape, '\u0000', null, null, null, null, null, null, null, null);
        }

        @Override
        Token nextToken(Token reusableToken) throws IOException {
            throw new UnsupportedOperationException();
        }
    }

    private void assertEquals(String expected, StringBuilder actual) {
        assertEquals(expected, actual.toString());
    }
}