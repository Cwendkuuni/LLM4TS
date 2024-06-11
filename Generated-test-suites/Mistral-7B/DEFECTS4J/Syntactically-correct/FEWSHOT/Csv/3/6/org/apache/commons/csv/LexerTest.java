package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class LexerTest {

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0A }), LF, null); // LF
        assertTrue(lexer.isStartOfLine(0));
        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0D, 0x0A }), CR, LF); // CR LF
        assertTrue(lexer.isStartOfLine(0));
        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0D }), CR); // CR
        assertTrue(lexer.isStartOfLine(0));
        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x00 }), UNDEFINED); // start of file
        assertTrue(lexer.isStartOfLine(0));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x20 }), ' '); // space
        assertFalse(lexer.isStartOfLine(0));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0B }), FF); // form feed
        assertFalse(lexer.isStartOfLine(0));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x08 }), BACKSPACE); // backspace
        assertFalse(lexer.isStartOfLine(0));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x00 }), UNDEFINED); // end of file
        assertFalse(lexer.isStartOfLine(0));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0A }), LF, null); // LF
        assertFalse(lexer.isEndOfFile(0));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0D, 0x0A }), CR, LF); // CR LF
        assertFalse(lexer.isEndOfFile(0));
        assertFalse(lexer.isEndOfFile(1));
        assertTrue(lexer.isEndOfFile(2));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0D }), CR); // CR
        assertFalse(lexer.isEndOfFile(0));
        assertFalse(lexer.isEndOfFile(1));
        assertTrue(lexer.isEndOfFile(2));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x00 }), UNDEFINED); // start of file
        assertFalse(lexer.isEndOfFile(0));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x00 }), UNDEFINED); // end of file
        assertTrue(lexer.isEndOfFile(0));
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x20 }), ' '); // space
        for (int i = 0; i < 256; i++) {
            if (Character.isWhitespace((char) i)) {
                assertTrue(lexer.isWhitespace(i));
            } else {
                assertFalse(lexer.isWhitespace(i));
            }
        }
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x2C }), ','); // comma
        assertTrue(lexer.isDelimiter(0));
        assertFalse(lexer.isDelimiter(1));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0A }), LF); // line feed
        assertFalse(lexer.isDelimiter(0));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0D, 0x0A }), CR, LF); // CR LF
        assertFalse(lexer.isDelimiter(0));
        assertFalse(lexer.isDelimiter(1));
        assertFalse(lexer.isDelimiter(2));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x5C, 0x0A }), '\\', LF); // backslash LF
        assertTrue(lexer.isEscape(0));
        assertFalse(lexer.isEscape(1));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0A }), LF, null); // LF
        assertFalse(lexer.isEscape(0));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0D, 0x0A }), CR, LF); // CR LF
        assertFalse(lexer.isEscape(0));
        assertFalse(lexer.isEscape(1));
        assertFalse(lexer.isEscape(2));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x5C }), '\\'); // backslash
        assertTrue(lexer.isEscape(0));
        assertFalse(lexer.isEscape(1));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x00 }), UNDEFINED); // start of file
        assertFalse(lexer.isEscape(0));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x22 }), '\"'); // double quote
        assertTrue(lexer.isQuoteChar(0));
        assertFalse(lexer.isQuoteChar(1));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0A }), LF, null); // LF
        assertFalse(lexer.isQuoteChar(0));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0D, 0x0A }), CR, LF); // CR LF
        assertFalse(lexer.isQuoteChar(0));
        assertFalse(lexer.isQuoteChar(1));
        assertFalse(lexer.isQuoteChar(2));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x23 }), '#'); // hash
        assertTrue(lexer.isCommentStart(0));
        assertFalse(lexer.isCommentStart(1));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0A }), LF, null); // LF
        assertFalse(lexer.isCommentStart(0));

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x0D, 0x0A }), CR, LF); // CR LF
        assertFalse(lexer.isCommentStart(0));
        assertFalse(lexer.isCommentStart(1));
        assertFalse(lexer.isCommentStart(2));
    }

    @Test
    public void testReadEscape() throws IOException {
        Lexer lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x5C, 0x62 }), '\\', 'b'); // backslash b
        assertEquals(BACKSPACE, lexer.readEscape());

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x5C, 0x61 }), '\\', 'a'); // backslash a
        assertEquals('a', lexer.readEscape());

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x62 }), 'b'); // b
        assertEquals('b', lexer.readEscape());

        lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x00 }), UNDEFINED); // end of file
        throw new IOException("EOF whilst processing escape sequence");
    }

    @Test(expected = IOException.class)
    public void testReadEscapeWithUnexpectedChar() throws IOException {
        Lexer lexer = new FixedLengthLexer(new ByteArrayInputStream(new byte[] { 0x5C, 0x66 }), '\\', 'f'); // backslash f
        // should throw an IOException because 'f' is not a valid escape character
    }

    private static class FixedLengthLexer extends Lexer {
        private final int length;
        private int index;

        public FixedLengthLexer(InputStreamReader input, char delimiter, Character escape, Character quoteChar, Character commentStart) {
            super(new CSVFormat(), input);
            this.delimiter = delimiter;
            this.escape = mapNullToDisabled(escape);
            this.quoteChar = mapNullToDisabled(quoteChar);
            this.commmentStart = mapNullToDisabled(commentStart);
            this.length = 1;
        }

        public FixedLengthLexer(ByteArrayInputStream input, char delimiter, char escape, Character quoteChar, Character commentStart) {
            super(new CSVFormat(), new ExtendedBufferedReader(new InputStreamReader(input), 8192));
            this.delimiter = delimiter;
            this.escape = mapNullToDisabled(escape);
            this.quoteChar = mapNullToDisabled(quoteChar);
            this.commmentStart = mapNullToDisabled(commentStart);
            this.length = 1;
        }

        @Override
        public Token nextToken(Token reusableToken) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() throws IOException {
            if (index < length) {
                return super.read();
            }
            index = 0;
            return read();
        }
    }
}