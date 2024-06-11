package org.apache.commons.csv;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class LexerTest {

    private Lexer lexer;
    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        StringReader stringReader = new StringReader("test,string,1\n123,456,789\n");
        reader = new ExtendedBufferedReader(new BufferedReader(stringReader));
        CSVFormat csvFormat = new CSVFormat.Builder()
                .withDelimiter(',')
                .withIgnoreEmptyLines(false)
                .withIgnoreSurroundingSpaces(false)
                .build();
        lexer = new Lexer(csvFormat, reader){};

    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(0, lexer.getLineNumber());

        reader.readLine();
        assertEquals(1, lexer.getLineNumber());

        reader.readLine();
        assertEquals(2, lexer.getLineNumber());
    }

    @Test
    public void testIsWhitespace() {
        assertFalse(lexer.isWhitespace('t'));
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertTrue(lexer.isWhitespace('\r'));
        assertTrue(lexer.isWhitespace('\n'));
    }

    @Test
    public void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));
        assertFalse(lexer.isStartOfLine(','));
    }

    @Test
    public void testIsEndOfFile() {
        assertFalse(lexer.isEndOfFile(' '));
        assertFalse(lexer.isEndOfFile('\n'));
        assertFalse(lexer.isEndOfFile('\r'));
        assertFalse(lexer.isEndOfFile(','));

        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
    }

    @Test
    public void testIsDelimiter() {
        assertFalse(lexer.isDelimiter(' '));
        assertFalse(lexer.isDelimiter('\n'));
        assertFalse(lexer.isDelimiter('\r'));

        assertTrue(lexer.isDelimiter(','));
    }

    @Test
    public void testIsEscape() {
        assertFalse(lexer.isEscape(' '));
        assertFalse(lexer.isEscape('\n'));
        assertFalse(lexer.isEscape('\r'));

        assertTrue(lexer.isEscape(Lexer.DISABLED));
    }

    @Test
    public void testIsQuoteChar() {
        assertFalse(lexer.isQuoteChar(' '));
        assertFalse(lexer.isQuoteChar('\n'));
        assertFalse(lexer.isQuoteChar('\r'));

        assertTrue(lexer.isQuoteChar(Lexer.DISABLED));
    }

    @Test
    public void testIsCommentStart() {
        assertFalse(lexer.isCommentStart(' '));
        assertFalse(lexer.isCommentStart('\n'));
        assertFalse(lexer.isCommentStart('\r'));

        assertTrue(lexer.isCommentStart(Lexer.DISABLED));
    }

    // Other test methods for nextToken, readEscape, trimTrailingSpaces, and readEndOfLine methods...

}