package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class LexerTest {

    @Test
    public void testConstructor() throws IOException {
        try (Reader reader = new StringReader("test")) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            CSVFormat format = CSVFormat.DEFAULT;
            ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(bufferedReader);
            
            Lexer lexer = new Lexer(format, extendedBufferedReader);
            
            assertEquals(format, lexer.format);
            assertEquals(extendedBufferedReader, lexer.in);
            assertEquals(format.getDelimiter(), lexer.delimiter);
            assertEquals(Util.mapNullToDisabled(format.getEscape()), lexer.escape);
            assertEquals(Util.mapNullToDisabled(format.getQuoteChar()), lexer.quoteChar);
            assertEquals(Util.mapNullToDisabled(format.getCommentStart()), lexer.commmentStart);
            assertEquals(format.getIgnoreSurroundingSpaces(), lexer.ignoreSurroundingSpaces);
            assertEquals(format.getIgnoreEmptyLines(), lexer.ignoreEmptyLines);
        }
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        try (Reader reader = new StringReader("test")) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            CSVFormat format = CSVFormat.DEFAULT;
            ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(bufferedReader);
            
            Lexer lexer = new Lexer(format, extendedBufferedReader);
            
            assertEquals(0, lexer.getLineNumber());
            
            // Test line number after reading a line
            lexer.in.readLine();
            
            assertEquals(1, lexer.getLineNumber());
        }
    }
    
    @Test
    public void testMapNullToDisabled() {
        Character character = null;
        char result = Lexer.mapNullToDisabled(character);
        
        assertEquals('\ufffe', result);
        
        character = 'a';
        result = Lexer.mapNullToDisabled(character);
        
        assertEquals('a', result);
    }
    
    @Test
    public void testReadEscape() throws IOException {
        try (Reader reader = new StringReader("test")) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            CSVFormat format = CSVFormat.DEFAULT;
            ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(bufferedReader);
            
            Lexer lexer = new Lexer(format, extendedBufferedReader);
            
            assertEquals(-1, lexer.readEscape());
            
            lexer.in.setLastChar('r');
            assertEquals(Constants.CR, lexer.readEscape());
            
            lexer.in.setLastChar('n');
            assertEquals(Constants.LF, lexer.readEscape());
            
            lexer.in.setLastChar('t');
            assertEquals(Constants.TAB, lexer.readEscape());
            
            lexer.in.setLastChar('b');
            assertEquals(Constants.BACKSPACE, lexer.readEscape());
            
            lexer.in.setLastChar('f');
            assertEquals(Constants.FF, lexer.readEscape());
            
            lexer.in.setLastChar(Constants.CR);
            assertEquals(Constants.CR, lexer.readEscape());
            
            lexer.in.setLastChar(Constants.LF);
            assertEquals(Constants.LF, lexer.readEscape());
            
            lexer.in.setLastChar(Constants.FF);
            assertEquals(Constants.FF, lexer.readEscape());
            
            lexer.in.setLastChar(Constants.TAB);
            assertEquals(Constants.TAB, lexer.readEscape());
            
            lexer.in.setLastChar(Constants.BACKSPACE);
            assertEquals(Constants.BACKSPACE, lexer.readEscape());
            
            lexer.in.setLastChar(Constants.END_OF_STREAM);
            
            try {
                lexer.readEscape();
            } catch (IOException e) {
                assertEquals("EOF whilst processing escape sequence", e.getMessage());
            }
        }
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test   ");
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        lexer.trimTrailingSpaces(buffer);
        
        assertEquals("test", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine() throws IOException {
        try (Reader reader = new StringReader("test")) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            CSVFormat format = CSVFormat.DEFAULT;
            ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(bufferedReader);
            
            Lexer lexer = new Lexer(format, extendedBufferedReader);
            
            assertFalse(lexer.readEndOfLine('t'));
            
            assertTrue(lexer.readEndOfLine(Lexer.EOF));
            
            lexer.in.setLastChar(Constants.CR);
            lexer.in.buf[0] = Constants.LF;
        
            assertTrue(lexer.readEndOfLine(Constants.CR));
        }
    }
    
    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        assertTrue(lexer.isWhitespace(' '));
        assertFalse(lexer.isWhitespace('a'));
    }
    
    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        assertTrue(lexer.isStartOfLine(Constants.LF));
        assertTrue(lexer.isStartOfLine(Constants.CR));
        assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));
        assertFalse(lexer.isStartOfLine('a'));
    }
    
    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('a'));
    }
    
    @Test
    public void testIsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = new Lexer(format, null);
        
        assertTrue(lexer.isDelimiter(format.getDelimiter()));
        assertFalse(lexer.isDelimiter('a'));
    }
    
    @Test
    public void testIsEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = new Lexer(format, null);
        
        assertTrue(lexer.isEscape(Util.mapNullToDisabled(format.getEscape())));
        assertFalse(lexer.isEscape('a'));
    }
    
    @Test
    public void testIsQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = new Lexer(format, null);
        
        assertTrue(lexer.isQuoteChar(Util.mapNullToDisabled(format.getQuoteChar())));
        assertFalse(lexer.isQuoteChar('a'));
    }
    
    @Test
    public void testIsCommentStart() {
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = new Lexer(format, null);
        
        assertTrue(lexer.isCommentStart(Util.mapNullToDisabled(format.getCommentStart())));
        assertFalse(lexer.isCommentStart('a'));
    }
    
    @Test
    public void testNextToken() throws IOException {
        try (Reader reader = new StringReader("test")) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            CSVFormat format = CSVFormat.DEFAULT;
            ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(bufferedReader);
            
            Lexer lexer = new Lexer(format, extendedBufferedReader);
            
            assertNull(lexer.nextToken(null));
        }
    }

}