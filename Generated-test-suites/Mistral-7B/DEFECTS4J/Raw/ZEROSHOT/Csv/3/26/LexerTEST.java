package org.apache.commons.csv.test;

import org.apache.commons.csv.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Test class for {@link Lexer}
 */
public class LexerTest {

    private ExtendedBufferedReader in;
    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setUp() throws IOException {
        format = new CSVFormat(new String[]{"column1", "column2"}, emptySeparator());
        String testString = "column1,\"column2\"\n" +
                "1,2\n" +
                "3,\"4,5\"\n" +
                "6";
        in = new ExtendedBufferedReader(new StringReader(testString));
        lexer = new Lexer(format, in);
    }

    @After
    public void tearDown() throws IOException {
        in.close();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(1, lexer.getLineNumber());
        lexer.nextToken(null); // consume first line
        assertEquals(2, lexer.getLineNumber());
    }

    @Test
    public void testHandleEscape() throws IOException {
        lexer.readEscape();
        assertEquals(CR, lexer.in.getLastChar());
        lexer.readEscape();
        assertEquals(LF, lexer.in.getLastChar());
    }

    @Test(expected = IOException.class)
    public void testHandleInvalidEscape() throws IOException {
        lexer.readEscape();
        lexer.readEscape();
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder(" column1 ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("column1", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        assertTrue(lexer.readEndOfLine(Constants.CR));
        assertTrue(lexer.readEndOfLine(Constants.LF));
        assertFalse(lexer.readEndOfLine(Constants.TAB));
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        assertTrue(lexer.isStartOfLine(Constants.CR));
        assertTrue(lexer.isStartOfLine(Constants.LF));
        assertFalse(lexer.isStartOfLine(Constants.TAB));
        assertFalse(lexer.isStartOfLine('x'));
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        assertFalse(lexer.isEndOfFile(Constants.CR));
        assertFalse(lexer.isEndOfFile(Constants.LF));
        assertFalse(lexer.isEndOfFile(Constants.TAB));
        assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
    }

    @Test
    public void testIsDelimiter() throws IOException {
        assertTrue(lexer.isDelimiter(lexer.delimiter));
        assertFalse(lexer.isDelimiter('x'));
    }

    @Test
    public void testIsEscape() throws IOException {
        assertTrue(lexer.isEscape(lexer.escape));
        assertFalse(lexer.isEscape('x'));
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        assertTrue(lexer.isQuoteChar(lexer.quoteChar));
        assertFalse(lexer.isQuoteChar('x'));
    }

    @Test
    public void testIsCommentStart() throws IOException {
        assertTrue(lexer.isCommentStart(lexer.commmentStart));
        assertFalse(lexer.isCommentStart('x'));
    }

    @Test
    public void testNextToken() throws IOException {
        Token token = lexer.nextToken(null);
        assertEquals("column1", token.getValue());
        assertEquals(TokenType.COLUMN_NAME, token.getType());

        token = lexer.nextToken(null);
        assertEquals("column2", token.getValue());
        assertEquals(TokenType.COLUMN_NAME, token.getType());

        token = lexer.nextToken(null);
        assertEquals("1", token.getValue());
        assertEquals(TokenType.STRING, token.getType());

        token = lexer.nextToken(null);
        assertEquals("2", token.getValue());
        assertEquals(TokenType.NUMBER, token.getType());

        token = lexer.nextToken(null);
        assertEquals("4,5", token.getValue());
        assertEquals(TokenType.STRING, token.getType());

        // check for end of file
        token = lexer.nextToken(null);
        assertNull(token);
    }

    private CSVParser.Separator emptySeparator() {
        return new CSVParser.Separator() {
            @Override
            public char separatorChar() {
                return Constants.UNDEFINED;
            }
        };
    }
}