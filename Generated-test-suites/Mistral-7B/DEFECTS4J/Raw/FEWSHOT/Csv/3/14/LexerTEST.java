package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.junit.Assert.*;

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.LexerTest.TestLexer;
import org.apache.commons.csv.util.ExtendedBufferedReader;

import java.io.IOException;

/**
 * Test cases for {@link Lexer} class
 */
public class LexerTest {

    /**
     * A test lexer for LexerTest
     */
    public static class TestLexer extends Lexer {

        /**
         * Constructor for test lexer
         *
         * @param format   the format object
         * @param in       the input stream
         */
        public TestLexer(CSVFormat format, ExtendedBufferedReader in) {
            super(format, in);
        }
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        ExtendedBufferedReader in = new ExtendedBufferedReader(System.in);
        CSVFormat format = new CSVFormatBuilder().build();
        Lexer lexer = new TestLexer(format, in);
        assertNotNull(lexer);
    }

    /**
     * Test mapNullToDisabled method
     */
    @Test
    public void testMapNullToDisabled() {
        CSVFormat format = new CSVFormatBuilder().delimiter(',')
                .escapeChar('\\').quotingMode(CSVFormat.QUOTE_MINIMAL)
                .commentPrefix("#")
                .ignoreSurroundingSpaces(true)
                .ignoreEmptyLines(true)
                .build();
        assertEquals('\ufffe', format.getEscape());
        assertEquals('\ufffe', format.getQuoteChar());
        assertEquals('\ufffe', format.getCommentStart());
    }

    /**
     * Test getLineNumber method
     *
     * @throws IOException if there is a problem reading the stream
     */
    @Test
    public void testGetLineNumber() throws IOException {
        String testData = "a,b\n1\n--\n2,c\n3\n";
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader(testData));
        CSVFormat format = new CSVFormatBuilder().build();
        Lexer lexer = new TestLexer(format, in);
        assertEquals(1, lexer.getLineNumber());
    }

    /**
     * Test readEscape method
     *
     * @throws IOException if there is a problem reading the stream or the end of stream is detected
     */
    @Test
    public void testReadEscape() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\\r\\n"));
        Lexer lexer = new TestLexer(null, in);
        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
    }

    /**
     * Test readEscape method with invalid escape sequence
     *
     * @throws IOException if there is a problem reading the stream or the end of stream is detected
     */
    @Test(expected = IOException.class)
    public void testReadEscapeInvalid() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("x"));
        Lexer lexer = new TestLexer(null, in);
        lexer.readEscape();
    }

    /**
     * Test trimTrailingSpaces method
     */
    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder(" foo\t bar ");
        Lexer lexer = new TestLexer(null, null);
        lexer.trimTrailingSpaces(buffer);
        assertEquals("foo bar", buffer.toString());
    }

    /**
     * Test readEndOfLine method
     *
     * @throws IOException if there is a problem reading the stream or the end of stream is detected
     */
    @Test
    public void testReadEndOfLine() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("ab\ncd"));
        Lexer lexer = new TestLexer(null, in);
        assertTrue(lexer.readEndOfLine(0));
        assertTrue(lexer.readEndOfLine(1));
    }

    /**
     * Test nextToken method
     *
     * @throws IOException if there is a problem reading the stream
     */
    @Test
    public void testNextToken() throws IOException {
        String testData = "a,b\n1\n--\n2,c\n3\n";
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader(testData));
        CSVFormat format = new CSVFormatBuilder().build();
        Lexer lexer = new TestLexer(format, in);
        StringBuilder token = new StringBuilder();
        Token reusableToken = new Token();
        Token nextToken = lexer.nextToken(reusableToken);
        assertEquals("a", nextToken.getValue());
        assertEquals(nextToken.getType(), TokenType.DATA);
        nextToken = lexer.nextToken(reusableToken);
        assertEquals("b", nextToken.getValue());
        assertEquals(nextToken.getType(), TokenType.DATA);
        nextToken = lexer.nextToken(reusableToken);
        assertEquals(1, nextToken.getValue().intValue());
        assertEquals(nextToken.getType(), TokenType.NUMBER);
        nextToken = lexer.nextToken(reusableToken);
        assertNull(nextToken);
    }

    /**
     * Test isWhitespace method
     */
    @Test
    public void testIsWhitespace() {
        assertTrue(isWhitespace(32));
        assertTrue(isWhitespace(13));
        assertTrue(isWhitespace(10));
        assertTrue(isWhitespace(8));
        assertTrue(isWhitespace(9));
        assertFalse(isWhitespace(44));
    }

    /**
     * Test isStartOfLine method
     */
    @Test
    public void testIsStartOfLine() {
        assertTrue(isStartOfLine(10));
        assertTrue(isStartOfLine(13));
        assertFalse(isStartOfLine(32));
        assertFalse(isStartOfLine(44));
    }

    /**
     * Test isEndOfFile method
     */
    @Test
    public void testIsEndOfFile() {
        assertFalse(isEndOfFile(32));
        assertFalse(isEndOfFile(13));
        assertFalse(isEndOfFile(10));
        assertFalse(isEndOfFile(44));
        assertFalse(isEndOfFile(0));
        assertTrue(isEndOfFile(-1));
    }

    /**
     * Test isDelimiter method
     */
    @Test
    public void testIsDelimiter() {
        CSVFormat format = new CSVFormatBuilder().delimiter(',').build();
        assertTrue(isDelimiter(44));
        assertFalse(isDelimiter(32));
    }

    /**
     * Test isEscape method
     */
    @Test
    public void testIsEscape() {
        CSVFormat format = new CSVFormatBuilder().escapeChar('\\').build();
        assertTrue(isEscape(92));
        assertFalse(isEscape(32));
    }

    /**
     * Test isQuoteChar method
     */
    @Test
    public void testIsQuoteChar() {
        CSVFormat format = new CSVFormatBuilder().quotingMode(CSVFormat.QUOTE_MINIMAL).build();
        assertTrue(isQuoteChar(34));
        assertFalse(isQuoteChar(32));
    }

    /**
     * Test isCommentStart method
     */
    @Test
    public void testIsCommentStart() {
        CSVFormat format = new CSVFormatBuilder().commentPrefix("#").build();
        assertTrue(isCommentStart(35));
        assertFalse(isCommentStart(32));
    }

}