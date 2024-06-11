package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setUp() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';');
        InputStream inputStream = new ByteArrayInputStream("1;2;3".getBytes(StandardCharsets.UTF_8));
        ExtendedBufferedReader reader = new ExtendedBufferedReader(inputStream);
        this.lexer = new Lexer(format, reader) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                return null; // Implement per specific test case
            }
        };
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        assertFalse(lexer.readEndOfLine(';'));
        assertFalse(lexer.readEndOfLine('2'));
        assertTrue(lexer.readEndOfLine('\n'));
        assertFalse(lexer.readEndOfLine('3'));
    }

    @Test(expected = IOException.class)
    public void testReadEndOfLineException() throws IOException {
        lexer.readEndOfLine('\r');
    }

    @Test
    public void testIsWhitespace() {
        assertFalse(lexer.isWhitespace(';'));
        assertTrue(lexer.isWhitespace(' '));
    }

    @Test
    public void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));
        assertFalse(lexer.isStartOfLine('1'));
    }

    @Test
    public void testIsEndOfFile() {
        assertFalse(lexer.isEndOfFile(';'));
        assertTrue(lexer.isEndOfFile(ExtendedBufferedReader.END_OF_STREAM));
    }

    @Test
    public void testIsDelimiter() {
        assertTrue(lexer.isDelimiter(';'));
        assertFalse(lexer.isDelimiter('1'));
    }

    @Test
    public void testIsEscape() {
        assertFalse(lexer.isEscape(';'));
        assertTrue(lexer.isEscape(ExtendedBufferedReader.DISABLED));
    }

    @Test
    public void testIsQuoteChar() {
        assertFalse(lexer.isQuoteChar(';'));
        assertTrue(lexer.isQuoteChar(ExtendedBufferedReader.DISABLED));
    }

    @Test
    public void testIsCommentStart() {
        assertFalse(lexer.isCommentStart(';'));
        assertTrue(lexer.isCommentStart(ExtendedBufferedReader.DISABLED));
    }
}