package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Test;

public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);

        Lexer lexer = new Lexer(format, in);
        assertEquals(0, lexer.getLineNumber());

        reader = new StringReader("\n");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        lexer.in.read();
        assertEquals(1, lexer.getLineNumber());
    }
    
    @Test
    public void testReadEscape() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("r");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        int result = lexer.readEscape();
        assertEquals("\\r", String.valueOf((char)result));
        
        reader = new StringReader("n");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.readEscape();
        assertEquals("\\n", String.valueOf((char)result));
        
        reader = new StringReader("t");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.readEscape();
        assertEquals("\\t", String.valueOf((char)result));
        
        reader = new StringReader("b");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.readEscape();
        assertEquals("\\b", String.valueOf((char)result));
        
        reader = new StringReader("f");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.readEscape();
        assertEquals("\\f", String.valueOf((char)result));
        
        reader = new StringReader("x");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.readEscape();
        assertEquals("\\x", String.valueOf((char)result));
        
        reader = new StringReader(" ");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.readEscape();
        assertEquals(" ", String.valueOf((char)result));
    }
    
    @Test(expected = IOException.class)
    public void testReadEscapeEOF() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        int result = lexer.readEscape();
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("foo ");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        StringBuilder buffer = new StringBuilder("foo ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("foo", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("\n");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.readEndOfLine(10);
        assertTrue(result);
        
        reader = new StringReader("\r");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.readEndOfLine(13);
        assertTrue(result);
        
        reader = new StringReader("x");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.readEndOfLine(120);
        assertFalse(result);
    }
    
    @Test
    public void testIsWhitespace() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader(" ");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isWhitespace(32);
        assertTrue(result);
        
        reader = new StringReader("x");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.isWhitespace(120);
        assertFalse(result);
    }
    
    @Test
    public void testIsStartOfLine() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("\r\f\n");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isStartOfLine(10);
        assertTrue(result);
        
        reader = new StringReader("x");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.isStartOfLine(120);
        assertFalse(result);
    }
    
    @Test
    public void testIsEndOfFile() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("x");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isEndOfFile(120);
        assertFalse(result);
        
        reader = new StringReader("x");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.isEndOfFile(-1);
        assertTrue(result);
    }
    
    @Test
    public void testIsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader(",");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isDelimiter(44);
        assertTrue(result);
        
        reader = new StringReader("x");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.isDelimiter(120);
        assertFalse(result);
    }
    
    @Test
    public void testIsEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("\\");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isEscape(92);
        assertTrue(result);
        
        reader = new StringReader("x");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.isEscape(120);
        assertFalse(result);
    }
    
    @Test
    public void testIsQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("\"");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isQuoteChar(34);
        assertTrue(result);
        
        reader = new StringReader("x");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.isQuoteChar(120);
        assertFalse(result);
    }
    
    @Test
    public void testIsCommentStart() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("#");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isCommentStart(35);
        assertTrue(result);
        
        reader = new StringReader("x");
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        result = lexer.isCommentStart(120);
        assertFalse(result);
    }
    
    @Test
    public void testNextToken() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        Token result = lexer.nextToken(null);
        assertNull(result);
    }

    
}