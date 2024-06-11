package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.apache.commons.csv.Lexer.IS_START_OF_LINE;
import static org.apache.commons.csv.Lexer.IS_WHITESPACE;
import static org.apache.commons.csv.Lexer.IS_DELIMITER;
import static org.apache.commons.csv.Lexer.IS_ESCAPE;
import static org.apache.commons.csv.Lexer.IS_QUOTE_CHAR;
import static org.apache.commons.csv.Lexer.IS_COMMENT_START;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.test.utils.TestUtils;
import org.junit.Test;

public class LexerTest {

    @Test
    public void testIsStartOfLine() {
        final int[] inputs = { 'A', CR, LF, UNDEFINED };
        for (final int c : inputs) {
            assertTrue("isStartOfLine should return true for " + c, IS_START_OF_LINE(c));
        }
    }

    @Test
    public void testIsEndOfFile() {
        final int[] inputs = { 'A', CR, LF, END_OF_STREAM };
        for (final int c : inputs) {
            assertFalse("isEndOfFile should return false for " + c, IS_END_OF_FILE(c));
        }
        assertTrue("isEndOfFile should return true for END_OF_STREAM", IS_END_OF_FILE(END_OF_STREAM));
    }

    @Test
    public void testIsWhitespace() {
        final int[] inputs = { '\t', ' ', '\n', '\r', 'A' };
        for (final int c : inputs) {
            assertTrue("isWhitespace should return true for " + ((char) c), IS_WHITESPACE(c));
        }
        assertFalse("isWhitespace should return false for A", IS_WHITESPACE('A'));
    }

    @Test
    public void testIsDelimiter() {
        final char delimiter = ',';
        final Lexer lexer = new TestLexer(new CSVFormat(new CSVParserDefaultConfiguration(), delimiter),
                new ByteArrayInputStream(TestUtils.createLineWithDelimiter("A,B,C").getBytes()));
        final int c = lexer.in.read();
        assertTrue("isDelimiter should return true for delimiter " + delimiter, IS_DELIMITER(c));
    }

    @Test
    public void testIsEscape() {
        final Lexer lexer = new TestLexer(new CSVFormat(new CSVParserDefaultConfiguration(), '\u0001'),
                new ByteArrayInputStream(TestUtils.createLineWithEscapedCharacter("A\\rB").getBytes()));
        final int c = lexer.in.read();
        assertFalse("isEscape should return false for non-escape character A", IS_ESCAPE(c));
        final int escape = lexer.readEscape();
        assertEquals("isEscape should return true for escape character ", CR, escape);
    }

    @Test
    public void testIsQuoteChar() {
        final Lexer lexer = new TestLexer(new CSVFormat(new CSVParserDefaultConfiguration(), '"'),
                new ByteArrayInputStream(TestUtils.createLineWithQuotedString("\"A\"B").getBytes()));
        final int c = lexer.in.read();
        assertFalse("isQuoteChar should return false for non-quote character A", IS_QUOTE_CHAR(c));
        final int quote = lexer.in.read();
        assertTrue("isQuoteChar should return true for quote character ", quote, IS_QUOTE_CHAR(quote));
    }

    @Test
    public void testIsCommentStart() {
        final Lexer lexer = new TestLexer(new CSVFormat(new CSVParserDefaultConfiguration(), '#'),
                new ByteArrayInputStream(TestUtils.createLineWithComment("#A")
                        .getBytes()));
        final int c = lexer.in.read();
        assertFalse("isCommentStart should return false for non-comment character A", IS_COMMENT_START(c));
        final int commentStart = lexer.in.read();
        assertTrue("isCommentStart should return true for comment start character #", IS_COMMENT_START(commentStart));
    }

    @Test
    public void testTrimTrailingSpaces() {
        final StringBuilder buffer = new StringBuilder("  foo  ");
        Lexer lexer = new TestLexer(new CSVFormat(new CSVParserDefaultConfiguration(), ','),
                new ByteArrayInputStream(TestUtils.createLineWithQuotedString("\"foo\"\n").getBytes()));
        lexer.nextToken(null);
        lexer.trimTrailingSpaces(buffer);
        assertEquals("trimTrailingSpaces should remove trailing spaces", "foo", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(TestUtils.createLineWithDelimiter("A\n").getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(input));
        final Lexer lexer = new TestLexer(new CSVFormat(new CSVParserDefaultConfiguration(), '\n'), reader);
        assertTrue("readEndOfLine should return true for LF", lexer.readEndOfLine(LF));
        assertTrue("readEndOfLine should return true for CR", lexer.readEndOfLine(CR));
        assertFalse("readEndOfLine should return false for other characters", lexer.readEndOfLine('A'));
    }

    private static class TestLexer extends Lexer {

        TestLexer(final CSVFormat format, final ExtendedBufferedReader in) {
            super(format, in);
        }

        @Override
        public Token nextToken(final Token reusableToken) throws IOException {
            return null;
        }
    }
}