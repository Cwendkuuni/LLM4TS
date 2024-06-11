package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class LexerTest {
    private CSVFormat format = CSVFormat.DEFAULT;
    private ExtendedBufferedReader in = new ExtendedBufferedReader();
    
    @Test
    public void testConstructor() {
        Lexer lexer = new Lexer(format, in);
        assertEquals(format, lexer.format);
        assertEquals(in, lexer.in);
        assertEquals(format.getDelimiter(), lexer.delimiter);
        assertEquals(format.getEscape(), lexer.escape);
        assertEquals(format.getQuoteChar(), lexer.quoteChar);
        assertEquals(format.getCommentStart(), lexer.commmentStart);
        assertEquals(format.getIgnoreSurroundingSpaces(), lexer.ignoreSurroundingSpaces);
        assertEquals(format.getIgnoreEmptyLines(), lexer.ignoreEmptyLines);
    }
    
    @Test
    public void testGetLineNumber() {
        in.setLineNumber(10);
        Lexer lexer = new Lexer(format, in);
        assertEquals(10, lexer.getLineNumber());
    }
    
    @Test
    public void testMapNullToDisabled() {
        Lexer lexer = new Lexer(format, in);
        assertEquals(Lexer.DISABLED, lexer.mapNullToDisabled(null));
        assertEquals('a', lexer.mapNullToDisabled('a'));
    }
    
    @Test
    public void testReadEscape() throws IOException {
        in.addCharSequence("r");
        Lexer lexer = new Lexer(format, in);
        int result = lexer.readEscape();
        assertEquals('\r', result);
        assertEquals(Lexer.END_OF_STREAM, in.getLastChar());
        
        in.addCharSequence("n");
        result = lexer.readEscape();
        assertEquals('\n', result);
        assertEquals(Lexer.END_OF_STREAM, in.getLastChar());
        
        in.addCharSequence("t");
        result = lexer.readEscape();
        assertEquals('\t', result);
        assertEquals(Lexer.END_OF_STREAM, in.getLastChar());
        
        in.addCharSequence("b");
        result = lexer.readEscape();
        assertEquals('\b', result);
        assertEquals(Lexer.END_OF_STREAM, in.getLastChar());
        
        in.addCharSequence("f");
        result = lexer.readEscape();
        assertEquals('\f', result);
        assertEquals(Lexer.END_OF_STREAM, in.getLastChar());
        
        in.addCharSequence("c");
        in.addCharSequence("r");
        result = lexer.readEscape();
        assertEquals('c', result);
        assertEquals('r', in.getLastChar());
        
        in.addCharSequence("\r");
        in.addCharSequence("n");
        result = lexer.readEscape();
        assertEquals('\n', result);
        assertEquals(Lexer.END_OF_STREAM, in.getLastChar());
        
        in.addCharSequence(Lexer.END_OF_STREAM);
        try {
            result = lexer.readEscape();
        } catch(IOException e) {
            assertEquals("EOF whilst processing escape sequence", e.getMessage());
        }
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("   test   ");
        Lexer lexer = new Lexer(format, in);
        lexer.trimTrailingSpaces(buffer);
        assertEquals("   test", buffer.toString());
        
        buffer = new StringBuilder("test");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
        
        buffer = new StringBuilder("   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new Lexer(format, in);
        
        assertFalse(lexer.readEndOfLine(10));
        assertFalse(lexer.readEndOfLine(13));
        
        in.addCharSequence("\n");
        assertTrue(lexer.readEndOfLine(10));
        
        in.addCharSequence("\r\n");
        assertTrue(lexer.readEndOfLine(13));
    }
    
    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(format, in);
        
        assertFalse(lexer.isWhitespace(10));
        assertFalse(lexer.isWhitespace(13));
        assertFalse(lexer.isWhitespace(9));
        
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
    }
    
    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(format, in);
        
        assertFalse(lexer.isStartOfLine(10));
        assertFalse(lexer.isStartOfLine(13));
        assertFalse(lexer.isStartOfLine(9));
        
        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));
    }
    
    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(format, in);
        
        assertFalse(lexer.isEndOfFile(10));
        assertFalse(lexer.isEndOfFile(13));
        assertFalse(lexer.isEndOfFile(9));
        assertFalse(lexer.isEndOfFile(Lexer.UNDEFINED));
        
        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
    }
    
    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(format, in);
        
        assertFalse(lexer.isDelimiter(10));
        assertFalse(lexer.isDelimiter(13));
        assertFalse(lexer.isDelimiter(9));
        assertFalse(lexer.isDelimiter(Lexer.END_OF_STREAM));
        
        assertTrue(lexer.isDelimiter(','));
    }
    
    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(format, in);
        
        assertFalse(lexer.isEscape(10));
        assertFalse(lexer.isEscape(13));
        assertFalse(lexer.isEscape(9));
        assertFalse(lexer.isEscape(Lexer.END_OF_STREAM));
        
        assertTrue(lexer.isEscape('\\'));
    }
    
    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(format, in);
        
        assertFalse(lexer.isQuoteChar(10));
        assertFalse(lexer.isQuoteChar(13));
        assertFalse(lexer.isQuoteChar(9));
        assertFalse(lexer.isQuoteChar(Lexer.END_OF_STREAM));
        
        assertTrue(lexer.isQuoteChar('"'));
    }
    
    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(format, in);
        
        assertFalse(lexer.isCommentStart(10));
        assertFalse(lexer.isCommentStart(13));
        assertFalse(lexer.isCommentStart(9));
        assertFalse(lexer.isCommentStart(Lexer.END_OF_STREAM));
        
        assertTrue(lexer.isCommentStart('#'));
    }
    
    @Test
    public void testNextToken() throws IOException {
        Lexer lexer = new Lexer(format, in);
        
        Token token = new Token();
        Token result = lexer.nextToken(token);
        assertNull(result.getCharSequence());
        assertEquals(-1, result.getType());

    }
}