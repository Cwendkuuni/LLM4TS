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
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("line 1\nline 2\nline 3");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = new Lexer(format, in);
        
        assertEquals(0, lexer.getLineNumber());
        
        lexer.nextToken(new Token());
        
        assertEquals(1, lexer.getLineNumber());
        
        lexer.nextToken(new Token());
        lexer.nextToken(new Token());
        
        assertEquals(3, lexer.getLineNumber());
    }
    
    @Test
    public void testReadEscape() throws IOException {
        StringReader reader = new StringReader("r\\nn\\t\\bb\\ff");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = new Lexer(format, in);
        
        int escape = lexer.readEscape();
        assertEquals(Lexer.CR, escape);
        
        escape = lexer.readEscape();
        assertEquals(Lexer.LF, escape);
        
        escape = lexer.readEscape();
        assertEquals(Lexer.TAB, escape);
        
        escape = lexer.readEscape();
        assertEquals(Lexer.BACKSPACE, escape);
        
        escape = lexer.readEscape();
        assertEquals(Lexer.FF, escape);
    }
    
    @Test(expected = IOException.class)
    public void testReadEscapeEndOfStream() throws IOException {
        StringReader reader = new StringReader("EOF");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = new Lexer(format, in);
        
        lexer.readEscape();
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("  spaces  ");
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        lexer.trimTrailingSpaces(buffer);
        
        assertEquals("  spaces", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine() throws IOException {
        StringReader reader = new StringReader("\n\r\n");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = new Lexer(format, in);
        
        boolean endOfLine = lexer.readEndOfLine(lexer.in.read());
        assertTrue(endOfLine);
        
        endOfLine = lexer.readEndOfLine(lexer.in.read());
        assertFalse(endOfLine);
        
        endOfLine = lexer.readEndOfLine(lexer.in.read());
        assertTrue(endOfLine);
    }
    
    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertTrue(lexer.isWhitespace('\n'));
        
        assertFalse(lexer.isWhitespace(','));
        assertFalse(lexer.isWhitespace('a'));
    }
    
    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        assertTrue(lexer.isStartOfLine(Lexer.LF));
        assertTrue(lexer.isStartOfLine(Lexer.CR));
        assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));
        
        assertFalse(lexer.isStartOfLine(' '));
        assertFalse(lexer.isStartOfLine(','));
    }
    
    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
        
        assertFalse(lexer.isEndOfFile(' '));
        assertFalse(lexer.isEndOfFile(','));
    }
    
    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        assertTrue(lexer.isDelimiter(','));
        
        assertFalse(lexer.isDelimiter(' '));
        assertFalse(lexer.isDelimiter('\t'));
    }
    
    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        assertTrue(lexer.isEscape('\\'));
        
        assertFalse(lexer.isEscape(' '));
        assertFalse(lexer.isEscape('\t'));
    }
    
    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        assertFalse(lexer.isQuoteChar(' '));
        assertFalse(lexer.isQuoteChar('\t'));
        assertFalse(lexer.isQuoteChar(','));
        
        assertTrue(lexer.isQuoteChar('"'));

    }
    
    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        assertFalse(lexer.isCommentStart(' '));
        assertFalse(lexer.isCommentStart('\t'));
        assertFalse(lexer.isCommentStart(','));
        assertFalse(lexer.isCommentStart('"'));
        
        assertTrue(lexer.isCommentStart('#'));
    }
}