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
    public void testLexer() throws IOException {
        // Create test input string
        String input = "abc,def,ghi\n123,456,789\n";
        
        // Create Lexer and CSVFormat objects
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        Lexer lexer = new Lexer(format, reader);
        
        // Test getLineNumber method
        assertEquals(0, lexer.getLineNumber());
        
        // Test readEscape method
        assertEquals(Lexer.CR, lexer.readEscape());
        assertEquals(Lexer.LF, lexer.readEscape());
        assertEquals(Lexer.TAB, lexer.readEscape());
        assertEquals(Lexer.BACKSPACE, lexer.readEscape());
        assertEquals(Lexer.FF, lexer.readEscape());
        assertEquals('r', lexer.readEscape());
        
        // Test trimTrailingSpaces method
        StringBuilder buffer = new StringBuilder("abc ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
        
        // Test readEndOfLine method
        assertTrue(lexer.readEndOfLine(Lexer.LF));
        assertTrue(lexer.readEndOfLine(Lexer.CR));
        assertTrue(lexer.readEndOfLine(Lexer.CR));
        assertFalse(lexer.readEndOfLine(65));
        
        // Test isWhitespace method
        assertTrue(lexer.isWhitespace(32));
        assertFalse(lexer.isWhitespace(44));
        
        // Test isStartOfLine method
        assertTrue(lexer.isStartOfLine(Lexer.LF));
        assertTrue(lexer.isStartOfLine(Lexer.CR));
        assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));
        assertFalse(lexer.isStartOfLine(65));
        
        // Test isEndOfFile method
        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile(65));
        
        // Test isDelimiter method
        assertTrue(lexer.isDelimiter(44));
        assertFalse(lexer.isDelimiter(65));
        
        // Test isEscape method
        assertFalse(lexer.isEscape(65));
        
        // Test isQuoteChar method
        assertFalse(lexer.isQuoteChar(65));
        
        // Test isCommentStart method
        assertFalse(lexer.isCommentStart(65));
    }
}