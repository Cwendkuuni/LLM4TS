package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.ext.ExtendedBufferedReader;
import org.junit.Test;

/**
 * Test class for {@link Lexer}
 */
public class LexerTest {

    /**
     * Test constructor.
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testConstructor() throws IOException {
        final CSVFormat format = CSVFormat.EXPLICIT.withDelimiter(';').withQuoteChar('"');
        final ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "a;b\r;c".getBytes())));

        final Lexer lexer = new Lexer(format, in);
        assertNotNull(lexer);

        assertEquals(';', lexer.delimiter);
        assertEquals('"', lexer.quoteChar);
        assertEquals('\r', lexer.commmentStart);
        assertFalse(lexer.ignoreSurroundingSpaces);
        assertFalse(lexer.ignoreEmptyLines);
        assertEquals(CSVFormat.DEFAULT, lexer.format);
        assertNotNull(lexer.in);

        in.close();
    }

    /**
     * Test getLineNumber().
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testGetLineNumber() throws IOException {
        final CSVFormat format = CSVFormat.DEFAULT;
        final ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "line1\nline2\nline3".getBytes())));

        final Lexer lexer = new Lexer(format, in);

        assertEquals(1, lexer.getLineNumber());

        lexer.nextToken(new Token());
        lexer.nextToken(new Token());

        assertEquals(2, lexer.getLineNumber());

        lexer.nextToken(new Token());
        lexer.nextToken(new Token());

        assertEquals(3, lexer.getLineNumber());

        in.close();
    }

    /**
     * Test readEscape().
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testReadEscape() throws IOException {
        final CSVFormat format = CSVFormat.DEFAULT;
        final ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "\r\n\\\\r\\n\\\\n\\\\t\\\\b\\\\f".getBytes())));

        final Lexer lexer = new Lexer(format, in);

        assertEquals(CR, lexer.readEscape());
        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
        assertEquals(TAB, lexer.readEscape());
        assertEquals(BACKSPACE, lexer.readEscape());
        assertEquals(FF, lexer.readEscape());

        in.close();
    }

    /**
     * Test readEndOfLine().
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testReadEndOfLine() throws IOException {
        final CSVFormat format = CSVFormat.DEFAULT;
        final ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "\r\nline1\r\nline2\r\nline3\r\n".getBytes())));

        final Lexer lexer = new Lexer(format, in);

        assertTrue(lexer.readEndOfLine(CR));
        assertTrue(lexer.readEndOfLine(LF));
        assertTrue(lexer.readEndOfLine(CR));

        assertFalse(lexer.readEndOfLine(UNDEFINED));

        in.close();
    }

    /**
     * Test isStartOfLine().
     */
    @Test
    public void testIsStartOfLine() {
        final int[] tests = { LF, CR, CR, LF, '\u0000' };

        final Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        for (final int test : tests) {
            assertTrue(lexer.isStartOfLine(test));
        }

    }

    /**
     * Test isEndOfFile().
     */
    @Test
    public void testIsEndOfFile() {
        final Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertFalse(lexer.isEndOfFile(CR));
        assertFalse(lexer.isEndOfFile(LF));
        assertFalse(lexer.isEndOfFile(TAB));
        assertFalse(lexer.isEndOfFile(BACKSPACE));
        assertFalse(lexer.isEndOfFile(UNDEFINED));
        assertTrue(lexer.isEndOfFile(END_OF_STREAM));

    }

    /**
     * Test isDelimiter().
     */
    @Test
    public void testIsDelimiter() {
        final Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertFalse(lexer.isDelimiter('a'));
        assertFalse(lexer.isDelimiter(';'));
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter('\t'));
        assertFalse(lexer.isDelimiter(CR));
        assertFalse(lexer.isDelimiter(LF));
        assertFalse(lexer.isDelimiter(BACKSPACE));
        assertFalse(lexer.isDelimiter(FF));
        assertFalse(lexer.isDelimiter(UNDEFINED));

    }

    /**
     * Test isEscape().
     */
    @Test
    public void testIsEscape() {
        final CSVFormat format = CSVFormat.DEFAULT;
        final ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "\r\n\\\\\\\\".getBytes())));

        final Lexer lexer = new Lexer(format, in);

        assertFalse(lexer.isEscape('a'));
        assertFalse(lexer.isEscape(';'));
        assertFalse(lexer.isEscape(CR));
        assertFalse(lexer.isEscape(LF));
        assertFalse(lexer.isEscape(BACKSPACE));
        assertFalse(lexer.isEscape(FF));
        assertFalse(lexer.isEscape(UNDEFINED));
        assertTrue(lexer.isEscape('\\'));

        in.close();
    }

    /**
     * Test isQuoteChar().
     */
    @Test
    public void testIsQuoteChar() {
        final CSVFormat format = CSVFormat.DEFAULT;
        final ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "\r\n\"\"".getBytes())));

        final Lexer lexer = new Lexer(format, in);

        assertFalse(lexer.isQuoteChar('a'));
        assertFalse(lexer.isQuoteChar(';'));
        assertFalse(lexer.isQuoteChar(CR));
        assertFalse(lexer.isQuoteChar(LF));
        assertFalse(lexer.isQuoteChar(BACKSPACE));
        assertFalse(lexer.isQuoteChar(FF));
        assertFalse(lexer.isQuoteChar(UNDEFINED));
        assertTrue(lexer.isQuoteChar('"'));

        in.close();
    }

    /**
     * Test isCommentStart().
     */
    @Test
    public void testIsCommentStart() {
        final CSVFormat format = CSVFormat.DEFAULT;
        final ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "\r\n%".getBytes())));

        final Lexer lexer = new Lexer(format, in);

        assertFalse(lexer.isCommentStart('a'));
        assertFalse(lexer.isCommentStart(';'));
        assertFalse(lexer.isCommentStart(CR));
        assertFalse(lexer.isCommentStart(LF));
        assertFalse(lexer.isCommentStart(BACKSPACE));
        assertFalse(lexer.isCommentStart(FF));
        assertFalse(lexer.isCommentStart(UNDEFINED));
        assertTrue(lexer.isCommentStart('%'));

        in.close();
    }

    /**
     * Test nextToken().
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testNextToken() throws IOException {
        final CSVFormat format = CSVFormat.DEFAULT;
        final ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
                "line1,\"line 2\",line 3\rline4\rline5".getBytes())));

        final Lexer lexer = new Lexer(format, in);

        final Token token = new Token();

        // line1
        lexer.nextToken(token);
        assertEquals(TokenType.STRING, token.getType());
        assertEquals("line1", token.getValue());

        // ,
        lexer.nextToken(token);
        assertEquals(TokenType.DELIMITER, token.getType());
        assertEquals(',', token.getValue());

        // "line 2"
        lexer.nextToken(token);
        assertEquals(TokenType.STRING, token.getType());
        assertEquals("line 2", token.getValue());

        // ,
        lexer.nextToken(token);
        assertEquals(TokenType.DELIMITER, token.getType());
        assertEquals(',', token.getValue());

        // line 3
        lexer.nextToken(token);
        assertEquals(TokenType.STRING, token.getType());
        assertEquals("line 3", token.getValue());

        // \r
        lexer.nextToken(token);
        assertEquals(TokenType.END_OF_LINE, token.getType());

        // line4
        lexer.nextToken(token);
        assertEquals(TokenType.STRING, token.getType());
        assertEquals("line4", token.getValue());

        // \r
        lexer.nextToken(token);
        assertEquals(TokenType.END_OF_LINE, token.getType());

        // line5
        lexer.nextToken(token);
        assertEquals(TokenType.STRING, token.getType());
        assertEquals("line5", token.getValue());

        // EOF
        lexer.nextToken(token);
        assertEquals(TokenType.END_OF_STREAM, token.getType());

        in.close();
    }

    /**
     * Test trimTrailingSpaces().
     */
    @Test
    public void testTrimTrailingSpaces() {
        final StringBuilder buffer = new StringBuilder("line1\tline2\nline3 ");

        new Lexer(CSVFormat.DEFAULT, null).trimTrailingSpaces(buffer);

        assertEquals("line1\tline2\nline3", buffer.toString());

    }

    /**
     * Test isWhitespace().
     */
    @Test
    public void testIsWhitespace() {
        final Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertTrue(lexer.isWhitespace('\r'));
        assertTrue(lexer.isWhitespace('\n'));
        assertTrue(lexer.isWhitespace('\f'));

        assertFalse(lexer.isWhitespace('a'));

    }

}