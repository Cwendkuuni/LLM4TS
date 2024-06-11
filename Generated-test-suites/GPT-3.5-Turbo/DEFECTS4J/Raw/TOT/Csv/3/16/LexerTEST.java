import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class LexerTest {

    private static final char DELIMITER = ',';
    private static final char ESCAPE = '"';
    private static final char QUOTE_CHAR = '"';
    private static final char COMMENT_START = '#';

    @Mock
    private ExtendedBufferedReader mockIn;

    private Lexer lexer;

    @Before
    public void setup() {
        mockIn = mock(ExtendedBufferedReader.class);
        lexer = new Lexer(CSVFormat.DEFAULT, mockIn);
    }

    @Test
    public void testConstructor() {
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), lexer.delimiter);
        assertEquals(CSVFormat.DEFAULT.getEscape(), lexer.escape);
        assertEquals(CSVFormat.DEFAULT.getQuoteChar(), lexer.quoteChar);
        assertEquals(CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), lexer.ignoreSurroundingSpaces);
        assertEquals(CSVFormat.DEFAULT.getIgnoreEmptyLines(), lexer.ignoreEmptyLines);
    }

    @Test
    public void testGetLineNumber() {
        when(mockIn.getLineNumber()).thenReturn(5L);
        assertEquals(5L, lexer.getLineNumber());
    }

    @Test
    public void testMapNullToDisabledNotNull() {
        assertEquals('a', lexer.mapNullToDisabled('a'));
    }

    @Test
    public void testMapNullToDisabledNull() {
        assertEquals(Lexer.DISABLED, lexer.mapNullToDisabled(null));
    }

    @Test
    public void testIsWhitespaceWhitespaceCharacter() {
        assertTrue(lexer.isWhitespace(' '));
    }

    @Test
    public void testIsWhitespaceNotWhitespaceCharacter() {
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    public void testIsStartOfLineStartOfLineCharacter() {
        assertTrue(lexer.isStartOfLine(Lexer.CR));
    }

    @Test
    public void testIsStartOfLineNotStartOfLineCharacter() {
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFileEndOfFileCharacter() {
        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
    }

    @Test
    public void testIsEndOfFileNotEndOfFileCharacter() {
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiterDelimiterCharacter() {
        assertTrue(lexer.isDelimiter(DELIMITER));
    }

    @Test
    public void testIsDelimiterNotDelimiterCharacter() {
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    public void testIsEscapeEscapeCharacter() {
        assertTrue(lexer.isEscape(ESCAPE));
    }

    @Test
    public void testIsEscapeNotEscapeCharacter() {
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    public void testIsQuoteCharQuoteCharCharacter() {
        assertTrue(lexer.isQuoteChar(QUOTE_CHAR));
    }

    @Test
    public void testIsQuoteCharNotQuoteCharCharacter() {
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    public void testIsCommentStartCommentStartCharacter() {
        assertTrue(lexer.isCommentStart(COMMENT_START));
    }

    @Test
    public void testIsCommentStartNotCommentStartCharacter() {
        assertFalse(lexer.isCommentStart('a'));
    }

    @Test
    public void testTrimTrailingSpacesNoTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("abc");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    @Test
    public void testTrimTrailingSpacesWithTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("abc   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    // Additional test cases for nextToken(Token reusableToken) method
    @Test
    public void testNextTokenEmptyLineNoIgnoreEmptyLines() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        when(mockIn.read()).thenReturn(Lexer.END_OF_STREAM);
        Token token = lexer.nextToken(null);
        assertEquals(Token.Type.EOF, token.getType());
    }

    @Test
    public void testNextTokenEmptyLineIgnoreEmptyLines() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        when(mockIn.read()).thenReturn(Lexer.END_OF_STREAM);
        Token token = lexer.nextToken(null);
        assertEquals(Token.Type.EOF, token.getType());
    }

    @Test
    public void testNextTokenEndOfStream() throws IOException {
        when(mockIn.read()).thenReturn(Lexer.END_OF_STREAM);
        Token token = lexer.nextToken(null);
        assertEquals(Token.Type.EOF, token.getType());
    }

    // Edge cases for readEscape() method
    @Test
    public void testReadEscapeEscapeCharacter() throws IOException {
        when(mockIn.read()).thenReturn('n');
        int result = lexer.readEscape();
        assertEquals(Lexer.LF, result);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeEndOfStream() throws IOException {
        when(mockIn.read()).thenReturn(Lexer.END_OF_STREAM);
        lexer.readEscape();
    }

    // Error scenarios for readEscape() method
    @Test
    public void testReadEscapeInvalidCharacter() throws IOException {
        when(mockIn.read()).thenReturn('x');
        int result = lexer.readEscape();
        assertEquals('x', result);
    }

    @Test
    public void testReadEscapeWhitespaceCharacter() throws IOException {
        when(mockIn.read()).thenReturn(' ');
        int result = lexer.readEscape();
        assertEquals(' ', result);
    }

    // Additional test cases for readEndOfLine(int c) method
    @Test
    public void testReadEndOfLineLF() throws IOException {
        when(mockIn.lookAhead()).thenReturn(Lexer.LF);
        boolean result = lexer.readEndOfLine(Lexer.LF);
        assertTrue(result);
    }

    @Test
    public void testReadEndOfLineCR() throws IOException {
        when(mockIn.lookAhead()).thenReturn(Lexer.LF);
        boolean result = lexer.readEndOfLine(Lexer.CR);
        assertTrue(result);
    }

    @Test
    public void testReadEndOfLineCRLF() throws IOException {
        when(mockIn.lookAhead()).thenReturn(Lexer.LF);
        boolean result = lexer.readEndOfLine(Lexer.CR);
        assertTrue(result);
    }

    @Test
    public void testReadEndOfLineNoEndOfLine() throws IOException {
        when(mockIn.lookAhead()).thenReturn('a');
        boolean result = lexer.readEndOfLine('a');
        assertFalse(result);
    }

    // Additional test cases for isWhitespace(final int c) method
    @Test
    public void testIsWhitespaceWhitespaceCharacter() {
        assertTrue(lexer.isWhitespace(' '));
    }

    @Test
    public void testIsWhitespaceNotWhitespaceCharacter() {
        assertFalse(lexer.isWhitespace('a'));
    }

    // Additional test cases for isStartOfLine(final int c) method
    @Test
    public void testIsStartOfLineStartOfLineCharacter() {
        assertTrue(lexer.isStartOfLine(Lexer.CR));
    }

    @Test
    public void testIsStartOfLineNotStartOfLineCharacter() {
        assertFalse(lexer.isStartOfLine('a'));
    }

    // Additional test cases for isDelimiter(final int c) method
    @Test
    public void testIsDelimiterDelimiterCharacter() {
        assertTrue(lexer.isDelimiter(DELIMITER));
    }

    @Test
    public void testIsDelimiterNotDelimiterCharacter() {
        assertFalse(lexer.isDelimiter('a'));
    }

    // Additional test cases for isEscape(final int c) method
    @Test
    public void testIsEscapeEscapeCharacter() {
        assertTrue(lexer.isEscape(ESCAPE));
    }

    @Test
    public void testIsEscapeNotEscapeCharacter() {
        assertFalse(lexer.isEscape('a'));
    }

    // Additional test cases for isQuoteChar(final int c) method
    @Test
    public void testIsQuoteCharQuoteCharCharacter() {
        assertTrue(lexer.isQuoteChar(QUOTE_CHAR));
    }

    @Test
    public void testIsQuoteCharNotQuoteCharCharacter() {
        assertFalse(lexer.isQuoteChar('a'));
    }

    // Additional test cases for isCommentStart(final int c) method
    @Test
    public void testIsCommentStartCommentStartCharacter() {
        assertTrue(lexer.isCommentStart(COMMENT_START));
    }

    @Test
    public void testIsCommentStartNotCommentStartCharacter() {
        assertFalse(lexer.isCommentStart('a'));
    }

}