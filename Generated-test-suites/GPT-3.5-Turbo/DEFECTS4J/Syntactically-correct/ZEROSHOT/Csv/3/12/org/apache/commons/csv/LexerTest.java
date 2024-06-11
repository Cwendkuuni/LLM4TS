package org.apache.commons.csv;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class LexerTest {
    
    @Test
    public void testConstructor() {
        try {
            ExtendedBufferedReader in = new ExtendedBufferedReader();
            CSVFormat format = new CSVFormat();
            Lexer lexer = new Lexer(format, in);
            assertEquals(format, lexer.format);
            assertEquals(in, lexer.in);
            assertEquals(format.getDelimiter(), lexer.delimiter);
            assertEquals(format.getEscape().charValue(), lexer.escape);
            assertEquals(format.getQuoteChar().charValue(), lexer.quoteChar);
            assertEquals(format.getCommentStart().charValue(), lexer.commmentStart);
            assertEquals(format.getIgnoreSurroundingSpaces(), lexer.ignoreSurroundingSpaces);
            assertEquals(format.getIgnoreEmptyLines(), lexer.ignoreEmptyLines);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testGetLineNumber() {
        try {
            ExtendedBufferedReader in = new ExtendedBufferedReader();
            CSVFormat format = new CSVFormat();
            Lexer lexer = new Lexer(format, in);
            assertEquals(in.getLineNumber(), lexer.getLineNumber());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test   ");
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine() {
        try {
            ExtendedBufferedReader in = new ExtendedBufferedReader();
            in.setLookAhead(LF);
            CSVFormat format = new CSVFormat();
            Lexer lexer = new Lexer(format, in);
            assertTrue(lexer.readEndOfLine(CR));
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isWhitespace(' '));
        assertFalse(lexer.isWhitespace(','));
    }
    
    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isStartOfLine(CR));
        assertTrue(lexer.isStartOfLine(LF));
        assertTrue(lexer.isStartOfLine(UNDEFINED));
        assertFalse(lexer.isStartOfLine(' '));
    }
    
    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isEndOfFile(END_OF_STREAM));
        assertFalse(lexer.isEndOfFile(' '));
    }
    
    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter(' '));
    }
    
    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape(' '));
    }
    
    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isQuoteChar('"'));
        assertFalse(lexer.isQuoteChar(' '));
    }
    
    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isCommentStart('#'));
        assertFalse(lexer.isCommentStart(' '));
    }
}