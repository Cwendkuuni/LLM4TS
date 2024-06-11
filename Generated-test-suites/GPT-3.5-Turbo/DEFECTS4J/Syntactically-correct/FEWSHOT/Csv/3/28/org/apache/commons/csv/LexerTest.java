package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class LexerTest {
    @Test
    public void testGetLineNumber() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader, format);
        Lexer lexer = new Lexer(format, ebr);
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader, format);
        Lexer lexer = new Lexer(format, ebr);
        assertEquals(Lexer.END_OF_STREAM, lexer.readEscape());
    }

    @Test
    public void testTrimTrailingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader, format);
        Lexer lexer = new Lexer(format, ebr);
        StringBuilder sb = new StringBuilder("example    ");
        lexer.trimTrailingSpaces(sb);
        assertEquals("example", sb.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader, format);
        Lexer lexer = new Lexer(format, ebr);
        assertFalse(lexer.readEndOfLine(CSVFormat.CR));
        assertTrue(lexer.readEndOfLine(CSVFormat.LF));
    }

    @Test
    public void testIsWhitespace() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader, format);
        Lexer lexer = new Lexer(format, ebr);
        assertTrue(lexer.isWhitespace(' '));
        assertFalse(lexer.isWhitespace(CSVFormat.DEFAULT.getDelimiter()));
    }

    @Test
    public void testIsStartOfLine() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader, format);
        Lexer lexer = new Lexer(format, ebr);
        assertTrue(lexer.isStartOfLine(CSVFormat.LF));
        assertTrue(lexer.isStartOfLine(CSVFormat.CR));
        assertTrue(lexer.isStartOfLine(CSVFormat.UNDEFINED));
        assertFalse(lexer.isStartOfLine(' '));
    }

    @Test
    public void testIsEndOfFile() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader, format);
        Lexer lexer = new Lexer(format, ebr);
        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile(' '));
    }

    @Test
    public void testIsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader, format);
        Lexer lexer = new Lexer(format, ebr);
        assertTrue(lexer.isDelimiter(CSVFormat.DEFAULT.getDelimiter()));
        assertFalse(lexer.isDelimiter(' '));
    }

    @Test
    public void testIsEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader, format);
        Lexer lexer = new Lexer(format, ebr);
        assertTrue(lexer.isEscape(CSVFormat.DEFAULT.getEscape()));
        assertFalse(lexer.isEscape(' '));
    }

    @Test
    public void testIsQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader, format);
        Lexer lexer = new Lexer(format, ebr);
        assertTrue(lexer.isQuoteChar(CSVFormat.DEFAULT.getQuoteChar()));
        assertFalse(lexer.isQuoteChar(' '));
    }

    @Test
    public void testIsCommentStart() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader, format);
        Lexer lexer = new Lexer(format, ebr);
        assertTrue(lexer.isCommentStart(CSVFormat.DEFAULT.getCommentStart()));
        assertFalse(lexer.isCommentStart(' '));
    }
}