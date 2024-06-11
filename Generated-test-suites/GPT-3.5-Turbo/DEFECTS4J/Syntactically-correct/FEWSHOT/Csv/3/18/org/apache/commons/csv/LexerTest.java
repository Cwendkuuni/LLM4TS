package org.apache.commons.csv;

import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        // Test when the line number is 0
        CSVFormat format = new CSVFormat.Builder().build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber);
        
        // Test when the line number is not 0
        in.setLineNumber(5);
        lineNumber = lexer.getLineNumber();
        assertEquals(5, lineNumber);
    }

    @Test
    public void testReadEscape() {
        try {
            // Test when the escape sequence is "\r"
            CSVFormat format = new CSVFormat.Builder().build();
            ExtendedBufferedReader in = new ExtendedBufferedReader(null);
            Lexer lexer = new Lexer(format, in);
            int escape = lexer.readEscape();
            assertEquals(Lexer.CR, escape);

            // Test when the escape sequence is "\n"
            escape = lexer.readEscape();
            assertEquals(Lexer.LF, escape);

            // Test when the escape sequence is "\t"
            escape = lexer.readEscape();
            assertEquals(Lexer.TAB, escape);

            // Test when the escape sequence is "\b"
            escape = lexer.readEscape();
            assertEquals(Lexer.BACKSPACE, escape);

            // Test when the escape sequence is "\f"
            escape = lexer.readEscape();
            assertEquals(Lexer.FF, escape);

            // Test when the escape sequence is invalid
            escape = lexer.readEscape();
            assertEquals(Lexer.END_OF_STREAM, escape);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        // Test when there are trailing spaces in the buffer
        CSVFormat format = new CSVFormat.Builder().build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        StringBuilder buffer = new StringBuilder(" Hello   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals(" Hello", buffer.toString());
        
        // Test when there are no trailing spaces in the buffer
        buffer = new StringBuilder("Hello");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("Hello", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine() {
        try {
            // Test when the character is '\n'
            CSVFormat format = new CSVFormat.Builder().build();
            ExtendedBufferedReader in = new ExtendedBufferedReader(null);
            Lexer lexer = new Lexer(format, in);
            boolean isEndOfLine = lexer.readEndOfLine('\n');
            assertTrue(isEndOfLine);

            // Test when the character is '\r'
            isEndOfLine = lexer.readEndOfLine('\r');
            assertTrue(isEndOfLine);

            // Test when the character is '\r' and followed by '\n'
            in.setLookAhead(Lexer.LF);
            isEndOfLine = lexer.readEndOfLine('\r');
            assertTrue(isEndOfLine);

            // Test when the character is not '\n' or '\r'
            isEndOfLine = lexer.readEndOfLine(' ');
            assertFalse(isEndOfLine);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testIsWhitespace() {
        // Test when the character is a whitespace
        CSVFormat format = new CSVFormat.Builder().build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        boolean isWhitespace = lexer.isWhitespace(' ');
        assertTrue(isWhitespace);
        
        // Test when the character is not a whitespace
        isWhitespace = lexer.isWhitespace('A');
        assertFalse(isWhitespace);
    }
    
    @Test
    public void testIsStartOfLine() {
        // Test when the character is '\n'
        CSVFormat format = new CSVFormat.Builder().build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        boolean isStartOfLine = lexer.isStartOfLine('\n');
        assertTrue(isStartOfLine);
        
        // Test when the character is '\r'
        isStartOfLine = lexer.isStartOfLine('\r');
        assertTrue(isStartOfLine);
        
        // Test when the character is UNDEFINED
        isStartOfLine = lexer.isStartOfLine(Lexer.UNDEFINED);
        assertTrue(isStartOfLine);
        
        // Test when the character is not '\n', '\r' or UNDEFINED
        isStartOfLine = lexer.isStartOfLine(' ');
        assertFalse(isStartOfLine);
    }
    
    @Test
    public void testIsEndOfFile() {
        // Test when the character is END_OF_STREAM
        CSVFormat format = new CSVFormat.Builder().build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        boolean isEndOfFile = lexer.isEndOfFile(Lexer.END_OF_STREAM);
        assertTrue(isEndOfFile);
        
        // Test when the character is not END_OF_STREAM
        isEndOfFile = lexer.isEndOfFile(' ');
        assertFalse(isEndOfFile);
    }
    
    @Test
    public void testIsDelimiter() {
        // Test when the character is the delimiter
        CSVFormat format = new CSVFormat.Builder().setDelimiter(',').build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        boolean isDelimiter = lexer.isDelimiter(',');
        assertTrue(isDelimiter);
        
        // Test when the character is not the delimiter
        isDelimiter = lexer.isDelimiter(';');
        assertFalse(isDelimiter);
    }
    
    @Test
    public void testIsEscape() {
        // Test when the character is the escape character
        CSVFormat format = new CSVFormat.Builder().setEscape('\\').build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        boolean isEscape = lexer.isEscape('\\');
        assertTrue(isEscape);
        
        // Test when the character is not the escape character
        isEscape = lexer.isEscape('!');
        assertFalse(isEscape);
    }
    
    @Test
    public void testIsQuoteChar() {
        // Test when the character is the quote character
        CSVFormat format = new CSVFormat.Builder().setQuoteChar('"').build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        boolean isQuoteChar = lexer.isQuoteChar('"');
        assertTrue(isQuoteChar);
        
        // Test when the character is not the quote character
        isQuoteChar = lexer.isQuoteChar('\'');
        assertFalse(isQuoteChar);
    }
    
    @Test
    public void testIsCommentStart() {
        // Test when the character is the comment start character
        CSVFormat format = new CSVFormat.Builder().setCommentStart("#").build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        boolean isCommentStart = lexer.isCommentStart('#');
        assertTrue(isCommentStart);
        
        // Test when the character is not the comment start character
        isCommentStart = lexer.isCommentStart('/');
        assertFalse(isCommentStart);
    }
    
}