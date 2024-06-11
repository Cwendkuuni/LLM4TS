package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Test;

/**
 * Test class for {@link Lexer}
 */
public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        CSVFormat format = CSVFormat.newBuilder().build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "line1\nline2".getBytes())));
        Lexer lexer = new Lexer(format, in);
        assertEquals(1, lexer.getLineNumber());
        lexer.nextToken(null); // consume first line
        assertEquals(2, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_CR() throws IOException {
        CSVFormat format = CSVFormat.newBuilder().build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "\r".getBytes())));
        Lexer lexer = new Lexer(format, in);
        assertEquals(CR, lexer.readEscape());
    }

    @Test
    public void testReadEscape_LF() throws IOException {
        CSVFormat format = CSVFormat.newBuilder().build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "\n".getBytes())));
        Lexer lexer = new Lexer(format, in);
        assertEquals(LF, lexer.readEscape());
    }

    @Test
    public void testReadEscape_TAB() throws IOException {
        CSVFormat format = CSVFormat.newBuilder().build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "\t".getBytes())));
        Lexer lexer = new Lexer(format, in);
        assertEquals(TAB, lexer.readEscape());
    }

    @Test
    public void testReadEscape_Backspace() throws IOException {
        CSVFormat format = CSVFormat.newBuilder().build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "\b".getBytes())));
        Lexer lexer = new Lexer(format, in);
        assertEquals(BACKSPACE, lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscape_Invalid() throws IOException {
        CSVFormat format = CSVFormat.newBuilder().build();
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "x".getBytes())));
        Lexer lexer = new Lexer(format, in);
        lexer.readEscape();
    }

    @Test
    public void testIsWhitespace_Whitespace() {
        assertTrue(Lexer.isWhitespace(' '));
        assertTrue(Lexer.isWhitespace('\t'));
        assertTrue(Lexer.isWhitespace('\n'));
        assertTrue(Lexer.isWhitespace('\r'));
        assertTrue(Lexer.isWhitespace('\f'));
    }

    @Test
    public void testIsWhitespace_NonWhitespace() {
        assertFalse(Lexer.isWhitespace('a'));
        assertFalse(Lexer.isWhitespace('b'));
        assertFalse(Lexer.isWhitespace(0x1F)); // control char
        assertFalse(Lexer.isWhitespace(0xFF)); // control char
    }

    @Test
    public void testIsStartOfLine_CR() {
        assertTrue(Lexer.isStartOfLine(CR));
    }

    @Test
    public void testIsStartOfLine_LF() {
        assertTrue(Lexer.isStartOfLine(LF));
    }

    @Test
    public void testIsStartOfLine_StartOfFile() {
        assertTrue(Lexer.isStartOfLine(UNDEFINED));
    }

    @Test
    public void testIsStartOfLine_MiddleOfLine() {
        assertFalse(Lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile_EndOfFile() {
        assertTrue(Lexer.isEndOfFile(END_OF_STREAM));
    }

    @Test
    public void testIsEndOfFile_MiddleOfStream() {
        assertFalse(Lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter_Delimiter() {
        char delimiter = ',';
        Lexer lexer = new Lexer(CSVFormat.newBuilder().setDelimiter(delimiter).build(), new ExtendedBufferedReader(
                new InputStreamReader(new ByteArrayInputStream(new byte[] { delimiter })))) {
            @Override
            abstract Token nextToken(Token reusableToken) throws IOException;
        };
        assertTrue(Lexer.isDelimiter(lexer.delimiter));
        assertTrue(Lexer.isDelimiter(lexer.readEscape()));
    }

    @Test
    public void testIsDelimiter_NonDelimiter() {
        char delimiter = ',';
        Lexer lexer = new Lexer(CSVFormat.newBuilder().setDelimiter(delimiter).build(), new ExtendedBufferedReader(
                new InputStreamReader(new ByteArrayInputStream(new byte[] { 'a' })))) {
            @Override
            abstract Token nextToken(Token reusableToken) throws IOException;
        };
        assertFalse(Lexer.isDelimiter('a'));
    }

    @Test
    public void testIsEscape_Escape() {
        char escape = '\\';
        Lexer lexer = new Lexer(CSVFormat.newBuilder().setEscape(escape).build(), new ExtendedBufferedReader(
                new InputStreamReader(new ByteArrayInputStream(new byte[] { escape })))) {
            @Override
            abstract Token nextToken(Token reusableToken) throws IOException;
        };
        assertTrue(Lexer.isEscape(lexer.escape));
        assertTrue(Lexer.isEscape(lexer.readEscape()));
    }

    @Test
    public void testIsEscape_NonEscape() {
        char escape = '\\';
        Lexer lexer = new Lexer(CSVFormat.newBuilder().setEscape(escape).build(), new ExtendedBufferedReader(
                new InputStreamReader(new ByteArrayInputStream(new byte[] { 'a' })))) {
            @Override
            abstract Token nextToken(Token reusableToken) throws IOException;
        };
        assertFalse(Lexer.isEscape('a'));
    }

    @Test
    public void testIsQuoteChar_QuoteChar() {
        char quoteChar = '"';
        Lexer lexer = new Lexer(CSVFormat.newBuilder().setQuoteChar(quoteChar).build(), new ExtendedBufferedReader(
                new InputStreamReader(new ByteArrayInputStream(new byte[] { quoteChar })))) {
            @Override
            abstract Token nextToken(Token reusableToken) throws IOException;
        };
        assertTrue(Lexer.isQuoteChar(lexer.quoteChar));
        assertTrue(Lexer.isQuoteChar(lexer.readEscape()));
    }

    @Test
    public void testIsQuoteChar_NonQuoteChar() {
        char quoteChar = '"';
        Lexer lexer = new Lexer(CSVFormat.newBuilder().setQuoteChar(quoteChar).build(), new ExtendedBufferedReader(
                new InputStreamReader(new ByteArrayInputStream(new byte[] { 'a' })))) {
            @Override
            abstract Token nextToken(Token reusableToken) throws IOException;
        };
        assertFalse(Lexer.isQuoteChar('a'));
    }

    @Test
    public void testIsCommentStart_CommentStart() {
        char commentStart = '#';
        Lexer lexer = new Lexer(CSVFormat.newBuilder().setCommentStart(commentStart).build(), new ExtendedBufferedReader(
                new InputStreamReader(new ByteArrayInputStream(new byte[] { commentStart })))) {
            @Override
            abstract Token nextToken(Token reusableToken) throws IOException;
        };
        assertTrue(Lexer.isCommentStart(lexer.commmentStart));
        assertTrue(Lexer.isCommentStart(lexer.readEscape()));
    }

    @Test
    public void testIsCommentStart_NonCommentStart() {
        char commentStart = '#';
        Lexer lexer = new Lexer(CSVFormat.newBuilder().setCommentStart(commentStart).build(), new ExtendedBufferedReader(
                new InputStreamReader(new ByteArrayInputStream(new byte[] { 'a' })))) {
            @Override
            abstract Token nextToken(Token reusableToken) throws IOException;
        };
        assertFalse(Lexer.isCommentStart('a'));
    }

}