package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setUp() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("test");
        lexer = new Lexer(format, reader);
    }

    @Test
    public void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape() throws IOException {
        int escapeChar = lexer.readEscape();
        assertEquals(Lexer.END_OF_STREAM, escapeChar);
    }

    @Test(expected = IOException.class)
    public void testReadEscape_EndOfStream() throws IOException {
        lexer.readEscape();
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("   test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("   test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine_LF() throws IOException {
        boolean endOfLine = lexer.readEndOfLine('\n');
        assertTrue(endOfLine);
    }

    @Test
    public void testReadEndOfLine_CR() throws IOException {
        boolean endOfLine = lexer.readEndOfLine('\r');
        assertTrue(endOfLine);
    }

    @Test
    public void testReadEndOfLine_CR_LF() throws IOException {
        boolean endOfLine = lexer.readEndOfLine('\r');
        assertTrue(endOfLine);
    }

    @Test
    public void testReadEndOfLine_NotEndOfLine() throws IOException {
        boolean endOfLine = lexer.readEndOfLine('t');
        assertFalse(endOfLine);
    }

    @Test
    public void testIsWhitespace_True() {
        boolean isWhitespace = lexer.isWhitespace(' ');
        assertTrue(isWhitespace);
    }

    @Test
    public void testIsWhitespace_False() {
        boolean isWhitespace = lexer.isWhitespace('t');
        assertFalse(isWhitespace);
    }

    @Test
    public void testIsStartOfLine_True() {
        boolean isStartOfLine = lexer.isStartOfLine(Lexer.UNDEFINED);
        assertTrue(isStartOfLine);
    }

    @Test
    public void testIsStartOfLine_False() {
        boolean isStartOfLine = lexer.isStartOfLine('t');
        assertFalse(isStartOfLine);
    }

    @Test
    public void testIsEndOfFile_True() {
        boolean isEndOfFile = lexer.isEndOfFile(Lexer.END_OF_STREAM);
        assertTrue(isEndOfFile);
    }

    @Test
    public void testIsEndOfFile_False() {
        boolean isEndOfFile = lexer.isEndOfFile('t');
        assertFalse(isEndOfFile);
    }

    @Test
    public void testIsDelimiter_True() {
        boolean isDelimiter = lexer.isDelimiter(',');
        assertTrue(isDelimiter);
    }

    @Test
    public void testIsDelimiter_False() {
        boolean isDelimiter = lexer.isDelimiter('t');
        assertFalse(isDelimiter);
    }

    @Test
    public void testIsEscape_True() {
        boolean isEscape = lexer.isEscape('\\');
        assertTrue(isEscape);
    }

    @Test
    public void testIsEscape_False() {
        boolean isEscape = lexer.isEscape('t');
        assertFalse(isEscape);
    }

    @Test
    public void testIsQuoteChar_True() {
        boolean isQuoteChar = lexer.isQuoteChar('"');
        assertTrue(isQuoteChar);
    }

    @Test
    public void testIsQuoteChar_False() {
        boolean isQuoteChar = lexer.isQuoteChar('t');
        assertFalse(isQuoteChar);
    }

    @Test
    public void testIsCommentStart_True() {
        boolean isCommentStart = lexer.isCommentStart('#');
        assertTrue(isCommentStart);
    }

    @Test
    public void testIsCommentStart_False() {
        boolean isCommentStart = lexer.isCommentStart('t');
        assertFalse(isCommentStart);
    }

    @Test
    public void testNextToken() throws IOException {
        Token token = lexer.nextToken(null);
        assertNull(token);
    }
}