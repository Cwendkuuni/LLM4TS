import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setUp() {
        lexer = new Lexer(new CSVFormat.Builder().build(), null); // Use a dummy format and input reader for testing purposes
    }

    @Test
    public void testReadEscape_validEscapeSequence() throws Exception {
        // Arrange
        int expected = '\n';

        // Act
        int result = lexer.readEscape();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testReadEscape_invalidEscapeSequence() {
        // Arrange
        int expected = Lexer.END_OF_STREAM;

        try {
            // Act
            int result = lexer.readEscape();
        } catch (Exception e) {
            // Assert
            assertEquals(IOException.class, e.getClass());
            assertEquals("EOF whilst processing escape sequence", e.getMessage());
        }
    }

    @Test
    public void testTrimTrailingSpaces_noTrailingSpaces() {
        // Arrange
        StringBuilder buffer = new StringBuilder("test");

        // Act
        lexer.trimTrailingSpaces(buffer);

        // Assert
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testTrimTrailingSpaces_withTrailingSpaces() {
        // Arrange
        StringBuilder buffer = new StringBuilder("test  ");

        // Act
        lexer.trimTrailingSpaces(buffer);

        // Assert
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine_EOL() throws Exception {
        // Arrange
        int c = '\n';
        boolean expected = true;

        // Act
        boolean result = lexer.readEndOfLine(c);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testReadEndOfLine_CRLF() throws Exception {
        // Arrange
        int c = '\r';
        boolean expected = true;

        // Act
        boolean result = lexer.readEndOfLine(c);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testReadEndOfLine_noEOL() throws Exception {
        // Arrange
        int c = 'a';
        boolean expected = false;

        // Act
        boolean result = lexer.readEndOfLine(c);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespace_whitespaceCharacter() {
        // Arrange
        int c = ' ';

        // Act
        boolean result = lexer.isWhitespace(c);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsWhitespace_nonWhitespaceCharacter() {
        // Arrange
        int c = 'a';

        // Act
        boolean result = lexer.isWhitespace(c);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsStartOfLine_startOfLineCharacter() {
        // Arrange
        int c = '\n';

        // Act
        boolean result = lexer.isStartOfLine(c);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsStartOfLine_notStartOfLineCharacter() {
        // Arrange
        int c = 'a';

        // Act
        boolean result = lexer.isStartOfLine(c);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsEndOfFile_endOfFileCharacter() {
        // Arrange
        int c = Lexer.END_OF_STREAM;

        // Act
        boolean result = lexer.isEndOfFile(c);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEndOfFile_notEndOfFileCharacter() {
        // Arrange
        int c = 'a';

        // Act
        boolean result = lexer.isEndOfFile(c);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsDelimiter_delimiterCharacter() {
        // Arrange
        int c = ',';

        // Act
        boolean result = lexer.isDelimiter(c);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsDelimiter_notDelimiterCharacter() {
        // Arrange
        int c = 'a';

        // Act
        boolean result = lexer.isDelimiter(c);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsEscape_escapeCharacter() {
        // Arrange
        int c = '\\';

        // Act
        boolean result = lexer.isEscape(c);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEscape_notEscapeCharacter() {
        // Arrange
        int c = 'a';

        // Act
        boolean result = lexer.isEscape(c);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsQuoteChar_quoteChar() {
        // Arrange
        int c = '"';

        // Act
        boolean result = lexer.isQuoteChar(c);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsQuoteChar_notQuoteChar() {
        // Arrange
        int c = 'a';

        // Act
        boolean result = lexer.isQuoteChar(c);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsCommentStart_commentStartCharacter() {
        // Arrange
        int c = '#';

        // Act
        boolean result = lexer.isCommentStart(c);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsCommentStart_notCommentStartCharacter() {
        // Arrange
        int c = 'a';

        // Act
        boolean result = lexer.isCommentStart(c);

        // Assert
        assertFalse(result);
    }

}