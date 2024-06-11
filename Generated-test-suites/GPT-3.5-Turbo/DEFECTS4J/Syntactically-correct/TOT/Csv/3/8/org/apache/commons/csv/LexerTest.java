import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    private Lexer lexer;
    private ExtendedBufferedReader reader;

    @Before
    public void setup() {
        reader = new ExtendedBufferedReader();
        lexer = new Lexer(format, reader);
    }

    @Test
    public void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        assertEquals(1, lineNumber);
        reader.incrementLineNumber();
        lineNumber = lexer.getLineNumber();
        assertEquals(2, lineNumber);
    }

    @Test
    public void testReadEscape_validEscapeSequence() {
        reader.setLastChar('\\');
        reader.setNextChar('n');
        int result = lexer.readEscape();
        assertEquals('\n', result);
    }

    @Test(expected = IOException.class)
    public void testReadEscape_invalidEscapeSequence() throws IOException {
        reader.setLastChar('\\');
        reader.setNextChar('#');
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces_noTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("abc");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    @Test
    public void testTrimTrailingSpaces_withTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("abc  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    @Test
    public void testReadEndOfLine_LF() throws IOException {
        boolean result = lexer.readEndOfLine('\n');
        assertTrue(result);
    }

    @Test
    public void testReadEndOfLine_CR() throws IOException {
        boolean result = lexer.readEndOfLine('\r');
        assertTrue(result);
    }

    @Test
    public void testReadEndOfLine_CR_LF() throws IOException {
        reader.setNextChar('\n');
        boolean result = lexer.readEndOfLine('\r');
        assertTrue(result);
    }

    @Test
    public void testReadEndOfLine_noLineTerminator() throws IOException {
        boolean result = lexer.readEndOfLine('a');
        assertFalse(result);
    }

    @Test
    public void testIsWhitespace_whitespaceChar() {
        boolean result = lexer.isWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespace_nonWhitespaceChar() {
        boolean result = lexer.isWhitespace('a');
        assertFalse(result);
    }

    @Test
    public void testIsStartOfLine_startOfLineChar() {
        boolean result = lexer.isStartOfLine('\n');
        assertTrue(result);
    }

    @Test
    public void testIsStartOfLine_nonStartOfLineChar() {
        boolean result = lexer.isStartOfLine('a');
        assertFalse(result);
    }

    @Test
    public void testIsEndOfFile_endOfFileChar() {
        boolean result = lexer.isEndOfFile(-1);
        assertTrue(result);
    }

    @Test
    public void testIsEndOfFile_nonEndOfFileChar() {
        boolean result = lexer.isEndOfFile('a');
        assertFalse(result);
    }

    @Test
    public void testIsDelimiter_delimiterChar() {
        boolean result = lexer.isDelimiter(',');
        assertTrue(result);
    }

    @Test
    public void testIsDelimiter_nonDelimiterChar() {
        boolean result = lexer.isDelimiter('a');
        assertFalse(result);
    }

    @Test
    public void testIsEscape_escapeChar() {
        boolean result = lexer.isEscape('\\');
        assertTrue(result);
    }

    @Test
    public void testIsEscape_nonEscapeChar() {
        boolean result = lexer.isEscape('a');
        assertFalse(result);
    }

    @Test
    public void testIsQuoteChar_quoteChar() {
        boolean result = lexer.isQuoteChar('"');
        assertTrue(result);
    }

    @Test
    public void testIsQuoteChar_nonQuoteChar() {
        boolean result = lexer.isQuoteChar('a');
        assertFalse(result);
    }

    @Test
    public void testIsCommentStart_commentStartChar() {
        boolean result = lexer.isCommentStart('#');
        assertTrue(result);
    }

    @Test
    public void testIsCommentStart_nonCommentStartChar() {
        boolean result = lexer.isCommentStart('a');
        assertFalse(result);
    }

    // Additional test cases for nextToken() method should be included here

}