package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.apache.commons.csv.Lexer.DISABLED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class LexerTest {

    @Test
    public void testLexerConstruction() {
        CSVFormat format = new CSVFormat(new String[]{"foo", "bar"}, '\t');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\tfoo\tbar"));
        Lexer lexer = new Lexer(format, in);

        assertEquals('\t', lexer.delimiter);
        assertEquals(DISABLED, lexer.escape);
        assertEquals(DISABLED, lexer.quoteChar);
        assertEquals(DISABLED, lexer.commmentStart);
        assertTrue(lexer.ignoreSurroundingSpaces);
        assertTrue(lexer.ignoreEmptyLines);
    }

    @Test
    public void testIgnoreSurroundingSpaces() throws IOException {
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader(" foo\nbar "));
        Lexer lexer = new Lexer(format, in);

        StringBuilder buffer = new StringBuilder();
        Token token = new Token();
        int c = lexer.in.read();

        assertFalse(lexer.isStartOfLine(c));
        assertEquals(UNDEFINED, c);
        token = lexer.nextToken(token);
        assertEquals(TokenType.WHITESPACE, token.getType());
        assertEquals(" ", token.getValue());

        c = lexer.in.read();
        assertTrue(lexer.isStartOfLine(c));
        assertEquals(LF, c);
        token = lexer.nextToken(token);
        assertEquals(TokenType.START_LINE, token.getType());

        buffer.append(lexer.nextToken(token).getValue());
        assertEquals("foo", buffer.toString());

        buffer.setLength(0);
        token.reset();
        c = lexer.in.read();
        token = lexer.nextToken(token);
        assertEquals(TokenType.WHITESPACE, token.getType());
        assertEquals(" ", token.getValue());
        buffer.append(token.getValue());

        token = lexer.nextToken(token);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken(token);
        assertEquals(TokenType.END_OF_LINE, token.getType());

        assertEquals(2, lexer.getLineNumber());
    }

    @Test
    public void testIgnoreEmptyLines() throws IOException {
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("foo\n\nbar"));
        Lexer lexer = new Lexer(format, in);

        StringBuilder buffer = new StringBuilder();
        Token token = new Token();

        token = lexer.nextToken(token);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken(token);
        assertEquals(TokenType.END_OF_LINE, token.getType());

        assertEquals(1, lexer.getLineNumber());

        token = lexer.nextToken(token);
        assertEquals(TokenType.END_OF_LINE, token.getType());

        assertEquals(2, lexer.getLineNumber());

        token = lexer.nextToken(token);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("bar", token.getValue());

        assertEquals(2, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("foo\\nbar"));
        Lexer lexer = new Lexer(format, in);

        int c = lexer.readEscape();
        assertEquals(LF, c);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeInvalid() throws IOException {
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("foo"));
        Lexer lexer = new Lexer(format, in);
        lexer.readEscape();
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(null, null);
        lexer.delimiter = '\t';
        assertEquals(true, lexer.isDelimiter('\t'));
        assertEquals(false, lexer.isDelimiter('x'));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(null, null);
        lexer.escape = '\\';
        assertEquals(true, lexer.isEscape('\\'));
        assertEquals(false, lexer.isEscape(' '));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(null, null);
        lexer.quoteChar = '"';
        assertEquals(true, lexer.isQuoteChar('"'));
        assertEquals(false, lexer.isQuoteChar(' '));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(null, null);
        lexer.commmentStart = '#';
        assertEquals(true, lexer.isCommentStart('#'));
        assertEquals(false, lexer.isCommentStart(' '));
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new Lexer(null, null);
        assertTrue(lexer.readEndOfLine('\r'));
        assertTrue(lexer.readEndOfLine('\n'));
        assertTrue(lexer.readEndOfLine('\r' + '\n'));
        assertFalse(lexer.readEndOfLine('x'));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isStartOfLine(10));
        assertFalse(lexer.isStartOfLine(11));
        assertTrue(lexer.isStartOfLine(12)); // LF
        assertTrue(lexer.isStartOfLine(13)); // CR
        assertTrue(lexer.isStartOfLine(10 + 13)); // CRLF
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isEndOfFile(0));
        assertFalse(lexer.isEndOfFile(-1));
        assertTrue(lexer.isEndOfFile(-2)); // EOF
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(null, null);
        assertTrue(lexer.isWhitespace(32)); // space
        assertTrue(lexer.isWhitespace(10)); // LF
        assertTrue(lexer.isWhitespace(13)); // CR
        assertTrue(lexer.isWhitespace(9)); // tab
        assertFalse(lexer.isWhitespace(48)); // '0'
        assertFalse(lexer.isWhitespace(65)); // 'A'
    }
}