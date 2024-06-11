import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setup() {
        lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader(new StringReader("")));
    }

    @Test
    public void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape_ValidEscapeSequence() throws IOException {
        lexer.in.pushChar('\\');
        lexer.in.pushChar('r');
        int escapedChar = lexer.readEscape();
        assertEquals(13, escapedChar);
    }

    @Test(expected = IOException.class)
    public void testReadEscape_InvalidEscapeSequence() throws IOException {
        lexer.in.pushChar('\\');
        lexer.in.pushChar('#');
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces_BufferContainsNoTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("Hello, World!");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("Hello, World!", buffer.toString());
    }

    @Test
    public void testTrimTrailingSpaces_BufferContainsTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("Hello, World!     ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("Hello, World!", buffer.toString());
    }

    @Test
    public void testReadEndOfLine_withLFCharacter() throws IOException {
        boolean endOfLine = lexer.readEndOfLine(10);
        assertTrue(endOfLine);
    }

    @Test
    public void testReadEndOfLine_withCRCharacter() throws IOException {
        boolean endOfLine = lexer.readEndOfLine(13);
        assertTrue(endOfLine);
    }

    @Test
    public void testIsWhitespace_withWhitespaceCharacter() {
        boolean isWhitespace = lexer.isWhitespace(32);
        assertTrue(isWhitespace);
    }

    @Test
    public void testIsWhitespace_withNonWhitespaceCharacter() {
        boolean isWhitespace = lexer.isWhitespace(65);
        assertFalse(isWhitespace);
    }

    @Test
    public void testIsStartOfLine_withLFCharacter() {
        boolean isStartOfLine = lexer.isStartOfLine(10);
        assertTrue(isStartOfLine);
    }

    @Test
    public void testIsStartOfLine_withCRCharacter() {
        boolean isStartOfLine = lexer.isStartOfLine(13);
        assertTrue(isStartOfLine);
    }

    @Test
    public void testIsStartOfLine_withUndefinedCharacter() {
        boolean isStartOfLine = lexer.isStartOfLine(-1);
        assertTrue(isStartOfLine);
    }

    @Test
    public void testIsStartOfLine_withNonStartOfLineCharacter() {
        boolean isStartOfLine = lexer.isStartOfLine(32);
        assertFalse(isStartOfLine);
    }

    @Test
    public void testIsEndOfFile_withEndOfStreamCharacter() {
        boolean isEndOfFile = lexer.isEndOfFile(-1);
        assertTrue(isEndOfFile);
    }

    @Test
    public void testIsEndOfFile_withNonEndOfStreamCharacter() {
        boolean isEndOfFile = lexer.isEndOfFile(10);
        assertFalse(isEndOfFile);
    }

    @Test
    public void testIsDelimiter_withDelimiterCharacter() {
        boolean isDelimiter = lexer.isDelimiter(44);
        assertTrue(isDelimiter);
    }

    @Test
    public void testIsDelimiter_withNonDelimiterCharacter() {
        boolean isDelimiter = lexer.isDelimiter(65);
        assertFalse(isDelimiter);
    }

    @Test
    public void testIsEscape_withEscapeCharacter() {
        boolean isEscape = lexer.isEscape(92);
        assertTrue(isEscape);
    }

    @Test
    public void testIsEscape_withNonEscapeCharacter() {
        boolean isEscape = lexer.isEscape(65);
        assertFalse(isEscape);
    }

    @Test
    public void testIsQuoteChar_withQuoteCharCharacter() {
        boolean isQuoteChar = lexer.isQuoteChar(34);
        assertTrue(isQuoteChar);
    }

    @Test
    public void testIsQuoteChar_withNonQuoteCharCharacter() {
        boolean isQuoteChar = lexer.isQuoteChar(65);
        assertFalse(isQuoteChar);
    }

    @Test
    public void testIsCommentStart_withCommentStartCharacter() {
        boolean isCommentStart = lexer.isCommentStart(35);
        assertTrue(isCommentStart);
    }

    @Test
    public void testIsCommentStart_withNonCommentStartCharacter() {
        boolean isCommentStart = lexer.isCommentStart(65);
        assertFalse(isCommentStart);
    }

}