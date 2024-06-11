package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for {@link Lexer}
 */
public class LexerTest {

    private ExtendedBufferedReader reader;
    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setUp() throws Exception {
        format = new CSVFormatBuilder()
                .setDelimiter(',')
                .setHeaderLineNumber(1)
                .build();

        reader = new ExtendedBufferedReader(new StringReader(""));
        lexer = new Lexer(format, reader);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("Test\n"));
        Lexer lexer = new Lexer(format, reader);
        int lineNumber = lexer.getLineNumber();
        Assert.assertEquals(1, lineNumber);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeWithInvalidCharacter() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("\\x"));
        lexer = new Lexer(format, reader);
        lexer.readEscape();
    }

    @Test
    public void testReadEscape() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("abc\\r\\n"));
        lexer = new Lexer(format, reader);
        int c = lexer.read();
        int escapedChar = lexer.readEscape();
        Assert.assertTrue(lexer.readEndOfLine(c));
        Assert.assertEquals(CR, escapedChar);

        reader = new ExtendedBufferedReader(new StringReader("abc\\n"));
        lexer = new Lexer(format, reader);
        c = lexer.read();
        escapedChar = lexer.readEscape();
        Assert.assertEquals(LF, escapedChar);
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader(" test string \n"));
        StringBuilder buffer = new StringBuilder();
        lexer = new Lexer(format, reader);
        lexer.nextToken(new Token(TokenType.START_ARRAY, "", 0, 0));
        buffer.append(lexer.getText());
        lexer.nextToken(new Token(TokenType.END_ARRAY, "", 0, 0));
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("test string", buffer.toString());
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("Test\n"));
        Lexer lexer = new Lexer(format, reader);
        boolean startOfLine = lexer.isStartOfLine(lexer.read());
        Assert.assertTrue(startOfLine);

        reader = new ExtendedBufferedReader(new StringReader("Test"));
        lexer = new Lexer(format, reader);
        startOfLine = lexer.isStartOfLine(lexer.read());
        Assert.assertFalse(startOfLine);
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("Test"));
        Lexer lexer = new Lexer(format, reader);
        boolean endOfFile = lexer.isEndOfFile(lexer.read());
        Assert.assertFalse(endOfFile);

        reader.close();
        lexer = new Lexer(format, reader);
        endOfFile = lexer.isEndOfFile(lexer.read());
        Assert.assertTrue(endOfFile);
    }

    @Test
    public void testIsDelimiter() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("test,test"));
        Lexer lexer = new Lexer(format, reader);
        boolean isDelimiter = lexer.isDelimiter(lexer.read());
        Assert.assertTrue(isDelimiter);

        isDelimiter = lexer.isDelimiter(lexer.read());
        Assert.assertFalse(isDelimiter);
    }

    @Test
    public void testIsEscape() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("test\\\\"));
        Lexer lexer = new Lexer(format, reader);
        boolean isEscape = lexer.isEscape(lexer.read());
        Assert.assertTrue(isEscape);

        isEscape = lexer.isEscape(lexer.read());
        Assert.assertFalse(isEscape);
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("test\"test"));
        Lexer lexer = new Lexer(format, reader);
        boolean isQuoteChar = lexer.isQuoteChar(lexer.read());
        Assert.assertTrue(isQuoteChar);

        isQuoteChar = lexer.isQuoteChar(lexer.read());
        Assert.assertFalse(isQuoteChar);
    }

    @Test
    public void testIsCommentStart() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("test#comment"));
        Lexer lexer = new Lexer(format, reader);
        boolean isCommentStart = lexer.isCommentStart(lexer.read());
        Assert.assertFalse(isCommentStart);

        reader.skip(1); // skip the comment
        isCommentStart = lexer.isCommentStart(lexer.read());
        Assert.assertFalse(isCommentStart);

        reader = new ExtendedBufferedReader(new StringReader("test#"));
        lexer = new Lexer(format, reader);
        isCommentStart = lexer.isCommentStart(lexer.read());
        Assert.assertTrue(isCommentStart);

        reader.skip(1); // skip the comment start
        isCommentStart = lexer.isCommentStart(lexer.read());
        Assert.assertFalse(isCommentStart);
    }

    @Test
    public void testNextToken() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("test,test"));
        Token token = new Token(TokenType.START_ARRAY, "", 0, 0);
        lexer = new Lexer(format, reader);
        Token nextToken = lexer.nextToken(token);
        Assert.assertEquals(TokenType.START_ARRAY, nextToken.getType());
        Assert.assertEquals("", nextToken.getText());
        Assert.assertEquals(1, nextToken.getStartPosition());
        Assert.assertEquals(1, nextToken.getEndPosition());

        token = new Token(TokenType.END_ARRAY, "", 0, 0);
        nextToken = lexer.nextToken(token);
        Assert.assertEquals(TokenType.END_OF_STREAM, nextToken.getType());
    }

    @Test
    public void testNextTokenWithEscapedCharacter() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("test,\"ab\\c\"test"));
        Token token = new Token(TokenType.START_ARRAY, "", 0, 0);
        lexer = new Lexer(format, reader);
        Token nextToken = lexer.nextToken(token);
        Assert.assertEquals(TokenType.START_ARRAY, nextToken.getType());
        Assert.assertEquals("", nextToken.getText());
        Assert.assertEquals(1, nextToken.getStartPosition());
        Assert.assertEquals(1, nextToken.getEndPosition());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals(TokenType.STRING, nextToken.getType());
        Assert.assertEquals("\"" + Constants.BACKSLASH + "c" + Constants.QUOTE_CHAR, nextToken.getText());
        Assert.assertEquals(3, nextToken.getStartPosition());
        Assert.assertEquals(4, nextToken.getEndPosition());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals(TokenType.END_ARRAY, nextToken.getType());
        Assert.assertEquals("", nextToken.getText());
        Assert.assertEquals(6, nextToken.getStartPosition());
        Assert.assertEquals(6, nextToken.getEndPosition());

        nextToken = lexer.nextToken(token);
        Assert.assertEquals(TokenType.END_OF_STREAM, nextToken.getType());
    }
}