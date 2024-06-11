package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class LexerTest {
    private Lexer lexer;
    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        CSVFormat format = new CSVFormat.Builder().build();
        reader = new ExtendedBufferedReader(null);
        lexer = new Lexer(format, reader) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, lexer.getLineNumber());

        reader.setLineNumber(10);
        assertEquals(10, lexer.getLineNumber());

        reader.setLineNumber(-1);
        assertEquals(-1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        reader.setLastChar('r');
        int result = lexer.readEscape();
        assertEquals(13, result);

        reader.setLastChar('n');
        result = lexer.readEscape();
        assertEquals(10, result);

        reader.setLastChar('t');
        result = lexer.readEscape();
        assertEquals(9, result);

        reader.setLastChar('b');
        result = lexer.readEscape();
        assertEquals(8, result);

        reader.setLastChar('f');
        result = lexer.readEscape();
        assertEquals(12, result);

        reader.setLastChar('x');
        result = lexer.readEscape();
        assertEquals(0x78, result);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeAtEof() throws IOException {
        reader.setLastChar(ExtendedBufferedReader.END_OF_STREAM);
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("abc   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());

        buffer = new StringBuilder("abc");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());

        buffer = new StringBuilder("");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());

        buffer = new StringBuilder("  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        reader.setLastChar(Lexer.LF);
        reader.setLookAhead(Lexer.CR);
        boolean result = lexer.readEndOfLine(Lexer.LF);
        assertTrue(result);

        reader.setLastChar(Lexer.LF);
        reader.setLookAhead('a');
        result = lexer.readEndOfLine(Lexer.LF);
        assertTrue(result);

        reader.setLastChar(Lexer.CR);
        reader.setLookAhead(Lexer.LF);
        result = lexer.readEndOfLine(Lexer.CR);
        assertTrue(result);

        reader.setLastChar(Lexer.CR);
        reader.setLookAhead('a');
        result = lexer.readEndOfLine(Lexer.CR);
        assertTrue(result);

        result = lexer.readEndOfLine('a');
        assertFalse(result);
    }

    @Test
    public void testIsWhitespace() {
        boolean result = lexer.isWhitespace(' ');
        assertTrue(result);

        result = lexer.isWhitespace('\t');
        assertTrue(result);

        result = lexer.isWhitespace('\n');
        assertFalse(result);

        result = lexer.isWhitespace('a');
        assertFalse(result);
    }

    @Test
    public void testIsStartOfLine() {
        boolean result = lexer.isStartOfLine(Lexer.LF);
        assertTrue(result);

        result = lexer.isStartOfLine(Lexer.CR);
        assertTrue(result);

        result = lexer.isStartOfLine(Lexer.UNDEFINED);
        assertTrue(result);

        result = lexer.isStartOfLine('a');
        assertFalse(result);
    }

    @Test
    public void testIsEndOfFile() {
        boolean result = lexer.isEndOfFile(ExtendedBufferedReader.END_OF_STREAM);
        assertTrue(result);

        result = lexer.isEndOfFile('a');
        assertFalse(result);
    }

    @Test
    public void testIsDelimiter() {
        boolean result = lexer.isDelimiter(',');
        assertFalse(result);

        result = lexer.isDelimiter('a');
        assertFalse(result);
    }

    @Test
    public void testIsEscape() {
        boolean result = lexer.isEscape('\\');
        assertFalse(result);

        result = lexer.isEscape('a');
        assertFalse(result);
    }

    @Test
    public void testIsQuoteChar() {
        boolean result = lexer.isQuoteChar('"');
        assertFalse(result);

        result = lexer.isQuoteChar('a');
        assertFalse(result);
    }

    @Test
    public void testIsCommentStart() {
        boolean result = lexer.isCommentStart('#');
        assertFalse(result);

        result = lexer.isCommentStart('a');
        assertFalse(result);
    }
}