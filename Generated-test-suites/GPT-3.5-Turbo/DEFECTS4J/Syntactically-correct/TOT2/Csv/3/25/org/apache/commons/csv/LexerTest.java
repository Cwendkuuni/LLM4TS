package org.apache.commons.csv;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setup() {
        lexer = new MockLexer();
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() {
        assertEquals(Lexer.CR, lexer.readEscape());
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        assertTrue(lexer.readEndOfLine(Lexer.LF));
        assertTrue(lexer.readEndOfLine(Lexer.CR));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(' '));
    }

    @Test
    public void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine(Lexer.CR));
    }

    @Test
    public void testIsEndOfFile() {
        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
    }

    @Test
    public void testIsDelimiter() {
        assertTrue(lexer.isDelimiter(','));
    }

    @Test
    public void testIsEscape() {
        assertTrue(lexer.isEscape('\\'));
    }

    @Test
    public void testIsQuoteChar() {
        assertTrue(lexer.isQuoteChar('"'));
    }

    @Test
    public void testIsCommentStart() {
        assertTrue(lexer.isCommentStart('#'));
    }

    // Additional Tests for Edge Cases and Exception Handling should be added as required

    private class MockLexer extends Lexer {
        public MockLexer() {
            super(CSVFormat.DEFAULT, new ExtendedBufferedReader(new StringReader("")));
        }

        @Override
        Token nextToken(Token reusableToken) {
            return null;
        }
    }
}