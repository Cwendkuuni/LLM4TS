package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.apache.commons.csv.Lexer.DISABLED;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class LexerTest {

    @Test
    public void testLexer_isStartOfLine() {
        Lexer lexer = new TestLexer(null, null);
        assertFalse(lexer.isStartOfLine(CR));
        assertFalse(lexer.isStartOfLine(LF));
        assertFalse(lexer.isStartOfLine(UNDEFINED));
        assertTrue(lexer.isStartOfLine(UNDEFINED));
    }

    @Test
    public void testLexer_isEndOfFile() {
        Lexer lexer = new TestLexer(null, null);
        assertFalse(lexer.isEndOfFile(CR));
        assertFalse(lexer.isEndOfFile(LF));
        assertFalse(lexer.isEndOfFile(UNDEFINED));
        assertTrue(lexer.isEndOfFile(END_OF_STREAM));
    }

    @Test
    public void testLexer_isDelimiter() {
        Lexer lexer = new TestLexer(null, null);
        assertFalse(lexer.isDelimiter(CR));
        assertFalse(lexer.isDelimiter(LF));
        assertFalse(lexer.isDelimiter(UNDEFINED));
        assertTrue(lexer.isDelimiter(','));
    }

    @Test
    public void testLexer_isEscape() {
        Lexer lexer = new TestLexer(null, null);
        assertFalse(lexer.isEscape(CR));
        assertFalse(lexer.isEscape(LF));
        assertFalse(lexer.isEscape(UNDEFINED));
        assertFalse(lexer.isEscape('.'));
        assertTrue(lexer.isEscape('\\'));
    }

    @Test
    public void testLexer_isQuoteChar() {
        Lexer lexer = new TestLexer(null, null);
        assertFalse(lexer.isQuoteChar(CR));
        assertFalse(lexer.isQuoteChar(LF));
        assertFalse(lexer.isQuoteChar(UNDEFINED));
        assertFalse(lexer.isQuoteChar('.'));
        assertFalse(lexxer.isQuoteChar(','));
        assertTrue(lexer.isQuoteChar('"'));
    }

    @Test
    public void testLexer_isCommentStart() {
        Lexer lexer = new TestLexer(null, null);
        assertFalse(lexer.isCommentStart(CR));
        assertFalse(lexer.isCommentStart(LF));
        assertFalse(lexer.isCommentStart(UNDEFINED));
        assertFalse(lexer.isCommentStart('.'));
        assertFalse(lexer.isCommentStart(','));
        assertFalse(lexer.isCommentStart(' '));
        assertTrue(lexer.isCommentStart('#'));
    }

    @Test
    public void testLexer_isWhitespace() {
        Lexer lexer = new TestLexer(null, null);
        assertFalse(lexer.isWhitespace(CR));
        assertFalse(lexer.isWhitespace(LF));
        assertFalse(lexer.isWhitespace(UNDEFINED));
        assertFalse(lexer.isWhitespace('.'));
        assertFalse(lexer.isWhitespace(','));
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
    }

    @Test
    public void testLexer_readEndOfLine() {
        Lexer lexer = new TestLexer(null, null);
        int c = CR;
        assertFalse(lexer.readEndOfLine(c));
        c = LF;
        assertFalse(lexer.readEndOfLine(c));
        c = '\n';
        assertFalse(lexer.readEndOfLine(c));
        c = '\r';
        assertFalse(lexer.readEndOfLine(c));
        c = UNDEFINED;
        assertFalse(lexer.readEndOfLine(c));
        c = 'a';
        assertFalse(lexer.readEndOfLine(c));
        c = END_OF_STREAM;
        assertTrue(lexer.readEndOfLine(c));
    }

    @Test
    public void testLexer_trimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("foo bar baz   ");
        Lexer lexer = new TestLexer(null, null);
        lexer.trimTrailingSpaces(buffer);
        assertEquals("foo bar baz", buffer.toString());
    }

    @Test
    public void testLexer_nextToken() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("foo,".getBytes());
        InputStreamReader reader = new InputStreamReader(input);
        Lexer lexer = new TestLexer(null, reader);
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);
        assertEquals("FOO", nextToken.value);
        assertEquals(1, nextToken.column);
        assertEquals(1, nextToken.line);

        token.value = null;
        nextToken = lexer.nextToken(token);
        assertEquals(",", nextToken.value);
        assertEquals(2, nextToken.column);
        assertEquals(1, nextToken.line);
    }

    @Test
    public void testLexer_readEscape() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("foo\\r\\nbar".getBytes());
        InputStreamReader reader = new InputStreamReader(input);
        Lexer lexer = new TestLexer(null, reader);
        int c = lexer.readEscape();
        assertEquals(CR, c);
        c = lexer.readEscape();
        assertEquals(LF, c);
    }

    private static class TestLexer extends Lexer {

        TestLexer(CSVFormat format, ExtendedBufferedReader in) {
            super(format, in);
        }

        @Override
        abstract Token nextToken(Token reusableToken) throws IOException;
    }
}