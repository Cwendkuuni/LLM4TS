package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class LexerTest {
    
    private CSVFormat format;
    private ExtendedBufferedReader in;
    private Lexer lexer;
    
    @Before
    public void setup() {
        format = CSVFormat.DEFAULT;
        in = new ExtendedBufferedReader(null); // replace null with input stream object
        lexer = new Lexer(format, in);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(format, lexer.format);
        assertEquals(in, lexer.in);
        assertEquals(format.getDelimiter(), lexer.delimiter);
        assertEquals(format.getEscape().charValue(), lexer.escape);
        assertEquals(format.getQuoteChar().charValue(), lexer.quoteChar);
        assertEquals(format.getCommentStart().charValue(), lexer.commmentStart);
        assertEquals(format.getIgnoreSurroundingSpaces(), lexer.ignoreSurroundingSpaces);
        assertEquals(format.getIgnoreEmptyLines(), lexer.ignoreEmptyLines);
    }
    
    @Test
    public void testGetLineNumber() {
        assertEquals(in.getLineNumber(), lexer.getLineNumber());
    }
    
    @Test
    public void testMapNullToDisabled() {
        assertEquals(Lexer.DISABLED, lexer.mapNullToDisabled(null));
        assertEquals('a', lexer.mapNullToDisabled('a'));
        assertEquals('b', lexer.mapNullToDisabled(new Character('b')));
    }
    
    @Test
    public void testReadEscape() throws IOException {
        in.setLastChar('r');
        assertEquals(Lexer.CR, lexer.readEscape());
        in.setLastChar('n');
        assertEquals(Lexer.LF, lexer.readEscape());
        in.setLastChar('t');
        assertEquals(Lexer.TAB, lexer.readEscape());
        in.setLastChar('b');
        assertEquals(Lexer.BACKSPACE, lexer.readEscape());
        in.setLastChar('f');
        assertEquals(Lexer.FF, lexer.readEscape());
        in.setLastChar(Lexer.CR);
        assertEquals(Lexer.CR, lexer.readEscape());
        in.setLastChar(Lexer.LF);
        assertEquals(Lexer.LF, lexer.readEscape());
        in.setLastChar(Lexer.FF);
        assertEquals(Lexer.FF, lexer.readEscape());
        in.setLastChar(Lexer.TAB);
        assertEquals(Lexer.TAB, lexer.readEscape());
        in.setLastChar(Lexer.BACKSPACE);
        assertEquals(Lexer.BACKSPACE, lexer.readEscape());
    }
    
    @Test(expected = IOException.class)
    public void testReadEscapeEOF() throws IOException {
        in.setLastChar(ExtendedBufferedReader.END_OF_STREAM);
        lexer.readEscape();
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("   test  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("   test", buffer.toString());
        
        buffer = new StringBuilder("   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
        
        buffer = new StringBuilder("");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLineLF() throws IOException {
        assertTrue(lexer.readEndOfLine(Lexer.LF));
    }
    
    @Test
    public void testReadEndOfLineCR() throws IOException {
        assertFalse(lexer.readEndOfLine(Lexer.CR));
    }
    
    @Test
    public void testReadEndOfLineCR_LF() throws IOException {
        assertTrue(lexer.readEndOfLine(Lexer.CR));
        in.setLastChar(Lexer.LF);
        assertTrue(lexer.readEndOfLine(in.read()));
    }
    
    @Test
    public void testReadEndOfLineOtherChar() throws IOException {
        assertFalse(lexer.readEndOfLine('a'));
    }
    
    @Test
    public void testNextToken() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);
        assertEquals(token, nextToken);
    }
    
    @Test
    public void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(' '));
        assertFalse(lexer.isWhitespace('a'));
    }
    
    @Test
    public void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine(Lexer.LF));
        assertTrue(lexer.isStartOfLine(Lexer.CR));
        assertFalse(lexer.isStartOfLine('a'));
    }
    
    @Test
    public void testIsEndOfFile() {
        assertFalse(lexer.isEndOfFile('a'));
        assertTrue(lexer.isEndOfFile(ExtendedBufferedReader.END_OF_STREAM));
    }
    
    @Test
    public void testIsDelimiter() {
        assertFalse(lexer.isDelimiter('a'));
        assertTrue(lexer.isDelimiter(format.getDelimiter()));
    }
    
    @Test
    public void testIsEscape() {
        assertFalse(lexer.isEscape('a'));
        assertTrue(lexer.isEscape(format.getEscape().charValue()));
    }
    
    @Test
    public void testIsQuoteChar() {
        assertFalse(lexer.isQuoteChar('a'));
        assertTrue(lexer.isQuoteChar(format.getQuoteChar().charValue()));
    }
    
    @Test
    public void testIsCommentStart() {
        assertFalse(lexer.isCommentStart('a'));
        assertTrue(lexer.isCommentStart(format.getCommentStart().charValue()));
    }
}