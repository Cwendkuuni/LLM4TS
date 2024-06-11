package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class LexerTest {
    private ExtendedBufferedReader reader;
    private CSVFormat csvFormat;

    @Before
    public void setUp() throws IOException {
        csvFormat = CSVFormat.DEFAULT;
        String input = "abc,def,ghi\r\n123,456,789";
        reader = new ExtendedBufferedReader(new StringReader(input));
    }

    @Test
    public void testConstructor() {
        Lexer lexer = new Lexer(csvFormat, reader);
        assertEquals(csvFormat, lexer.format);
        assertEquals(reader, lexer.in);
        assertEquals(',', lexer.delimiter);
        assertEquals('"', lexer.escape);
        assertEquals('"', lexer.quoteChar);
        assertEquals('\ufffe', lexer.commmentStart);
        assertFalse(lexer.ignoreSurroundingSpaces);
        assertFalse(lexer.ignoreEmptyLines);
    }

    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer(csvFormat, reader);
        assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        Lexer lexer = new Lexer(csvFormat, reader);
        int escape = lexer.readEscape();
        assertEquals(-1, lexer.in.getLastChar());
        assertEquals('\\', escape);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeThrowsIOException() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withEscape(null);
        Lexer lexer = new Lexer(csvFormat, reader);
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new Lexer(csvFormat, reader);
        StringBuilder buffer = new StringBuilder("   abc   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("   abc", buffer.toString());
        buffer = new StringBuilder("abc");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new Lexer(csvFormat, reader);
        assertTrue(lexer.readEndOfLine('\n'));
        assertFalse(lexer.readEndOfLine('a'));
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(csvFormat, reader);
        assertTrue(lexer.isWhitespace(' '));
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(csvFormat, reader);
        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine(-1));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(csvFormat, reader);
        assertTrue(lexer.isEndOfFile(-1));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(csvFormat, reader);
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(csvFormat, reader);
        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(csvFormat, reader);
        assertTrue(lexer.isQuoteChar('"'));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(csvFormat, reader);
        assertTrue(lexer.isCommentStart('\ufffe'));
        assertFalse(lexer.isCommentStart('a'));
    }

    @Test
    public void testNextToken() throws IOException {
        Lexer lexer = new Lexer(csvFormat, reader);
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);
        assertEquals("abc", nextToken.content);
        assertEquals(0, nextToken.start);
        assertEquals(3, nextToken.end);
        assertTrue(nextToken.isReady());
        nextToken = lexer.nextToken(token);
        assertEquals(',', nextToken.content);
        assertEquals(3, nextToken.start);
        assertEquals(4, nextToken.end);
        assertTrue(nextToken.isDelimiter());
        nextToken = lexer.nextToken(token);
        assertEquals("def", nextToken.content);
        assertEquals(4, nextToken.start);
        assertEquals(7, nextToken.end);
        assertTrue(nextToken.isReady());
    }
}