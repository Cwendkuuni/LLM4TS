package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.FF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Test;

public class LexerTest {

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new ByteArrayInputStream(new byte[]{}));
        assertTrue(lexer.isWhitespace(UNDEFINED));
        assertTrue(lexer.isWhitespace(LF));
        assertTrue(lexer.isWhitespace(CR));
        assertTrue(lexer.isWhitespace(TAB));
        assertTrue(lexer.isWhitespace(BACKSPACE));
        assertTrue(lexer.isWhitespace(FF));
        assertFalse(lexer.isWhitespace(32));
        assertFalse(lexer.isWhitespace('.'));
        assertFalse(lexer.isWhitespace('A'));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new ByteArrayInputStream(new byte[]{}));
        assertTrue(lexer.isStartOfLine(UNDEFINED));
        assertTrue(lexer.isStartOfLine(LF));
        assertTrue(lexer.isStartOfLine(CR));
        assertFalse(lexer.isStartOfLine('A'));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new ByteArrayInputStream(new byte[]{END_OF_STREAM}));
        assertTrue(lexer.isEndOfFile(END_OF_STREAM));
        assertFalse(lexer.isEndOfFile(LF));
        assertFalse(lexer.isEndOfFile(CR));
        assertFalse(lexer.isEndOfFile(UNDEFINED));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new ByteArrayInputStream(new byte[]{','}));
        assertFalse(lexer.isDelimiter(','));
        assertTrue(lexer.isDelimiter(','));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new ByteArrayInputStream(new byte[]{'\\'}));
        assertFalse(lexer.isEscape('\\'));
        assertTrue(lexer.isEscape('\\'));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new ByteArrayInputStream(new byte[]{'"'}));
        assertTrue(lexer.isQuoteChar('"'));
        assertFalse(lexer.isQuoteChar('\''));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new ByteArrayInputStream(new byte[]{'#'}));
        assertTrue(lexer.isCommentStart('#'));
        assertFalse(lexer.isCommentStart('@'));
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new ByteArrayInputStream(new byte[]{CR, LF}));
        assertTrue(lexer.readEndOfLine(CR));
        assertTrue(lexer.readEndOfLine(LF));
        assertFalse(lexer.readEndOfLine(TAB));
        assertFalse(lexer.readEndOfLine(UNDEFINED));
        assertFalse(lexer.readEndOfLine(32));
        assertFalse(lexer.readEndOfLine('A'));
    }

    @Test
    public void testHandleEscape() throws IOException {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new ByteArrayInputStream(new byte[]{'\\', 'r'}));
        assertEquals(CR, lexer.readEscape());

        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new ByteArrayInputStream(new byte[]{'\\', 'n'}));
        assertEquals(LF, lexer.readEscape());

        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new ByteArrayInputStream(new byte[]{'\\', 0x03}));
        assertEquals(FS, lexer.readEscape()); // assuming FS is 0x03

        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'\\'});
        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, input);
        assertEquals('\\', lexer.readEscape());
    }

    @Test
    public void testNextToken() throws IOException {
        String csvData = "foo,\"bar, baz\", quo\"ted, quo'ted, quo\"ted quo'ted, \n,,\n comment#,  \t,   ";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        InputStreamReader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader readerWrapper = new ExtendedBufferedReader(reader);
        CSVFormat format = CSVFormat.DEFAULT;

        Lexer lexer = new DelimitedFileLexer(format, readerWrapper);

        Token token = new Token();

        Token nextToken = lexer.nextToken(token);
        assertEquals("foo", nextToken.getValue());
        assertEquals(TokenType.STRING, nextToken.getType());

        nextToken = lexer.nextToken(token);
        assertEquals(",", nextToken.getValue());
        assertEquals(TokenType.DELIMITER, nextToken.getType());

        nextToken = lexer.nextToken(token);
        assertEquals("bar, baz", nextToken.getValue());
        assertEquals(TokenType.STRING, nextToken.getType());

        nextToken = lexer.nextToken(token);
        assertEquals(",", nextToken.getValue());
        assertEquals(TokenType.DELIMITER, nextToken.getType());

        nextToken = lexer.nextToken(token);
        assertEquals("quo\"ted", nextToken.getValue());
        assertEquals(TokenType.STRING, nextToken.getType());

        nextToken = lexer.nextToken(token);
        assertEquals(",", nextToken.getValue());
        assertEquals(TokenType.DELIMITER, nextToken.getType());

        nextToken = lexer.nextToken(token);
        assertEquals("quo'ted", nextToken.getValue());
        assertEquals(TokenType.STRING, nextToken.getType());

        nextToken = lexer.nextToken(token);
        assertEquals("quo\"ted quo'ted", nextToken.getValue());
        assertEquals(TokenType.STRING, nextToken.getType());

        nextToken = lexer.nextToken(token);
        assertEquals(",", nextToken.getValue());
        assertEquals(TokenType.DELIMITER, nextToken.getType());

        nextToken = lexer.nextToken(token);
        assertNull(nextToken.getValue());
        assertEquals(TokenType.NULL, nextToken.getType());

        nextToken = lexer.nextToken(token);
        assertNull(nextToken);

        nextToken = lexer.nextToken(token);
        assertNull(nextToken);

        nextToken = lexer.nextToken(token);
        assertEquals("comment#", nextToken.getValue());
        assertEquals(TokenType.COMMENT, nextToken.getType());

        nextToken = lexer.nextToken(token);
        assertEquals("", nextToken.getValue());
        assertEquals(TokenType.EMPTY, nextToken.getType());

        nextToken = lexer.nextToken(token);
        assertEquals("", nextToken.getValue());
        assertEquals(TokenType.EMPTY, nextToken.getType());
    }
}