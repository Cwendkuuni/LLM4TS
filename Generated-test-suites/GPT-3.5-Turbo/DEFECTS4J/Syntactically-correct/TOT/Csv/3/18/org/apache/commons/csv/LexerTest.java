import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setUp() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null); // Replace null with actual input stream
        lexer = new Lexer(format, in);
    }

    @Test
    public void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscapeWithValidEscapeSequence() throws IOException {
        // Setup
        ExtendedBufferedReader in = new ExtendedBufferedReader(null); // Replace null with actual input stream
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, in);

        // Test
        int escapedChar = lexer.readEscape();

        // Assert
        assertEquals('\n', escapedChar);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeWithInvalidEscapeSequence() throws IOException {
        // Setup
        ExtendedBufferedReader in = new ExtendedBufferedReader(null); // Replace null with actual input stream
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, in);

        // Test
        int escapedChar = lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpacesWithTrailingSpaces() {
        // Setup
        StringBuilder buffer = new StringBuilder("hello   ");

        // Test
        lexer.trimTrailingSpaces(buffer);

        // Assert
        assertEquals("hello", buffer.toString());
    }

    @Test
    public void testTrimTrailingSpacesWithoutTrailingSpaces() {
        // Setup
        StringBuilder buffer = new StringBuilder("hello");

        // Test
        lexer.trimTrailingSpaces(buffer);

        // Assert
        assertEquals("hello", buffer.toString());
    }

    @Test
    public void testReadEndOfLineWithLF() throws IOException {
        // Test
        boolean isEndOfLine = lexer.readEndOfLine('\n');

        // Assert
        Assert.assertTrue(isEndOfLine);
    }

    @Test
    public void testReadEndOfLineWithCR() throws IOException {
        // Test
        boolean isEndOfLine = lexer.readEndOfLine('\r');

        // Assert
        Assert.assertTrue(isEndOfLine);
    }

    @Test
    public void testReadEndOfLineWithCRLF() throws IOException {
        // Test
        boolean isEndOfLine = lexer.readEndOfLine('\r');

        // Assert
        Assert.assertTrue(isEndOfLine);
    }

    @Test
    public void testReadEndOfLineWithoutEndOfLine() throws IOException {
        // Test
        boolean isEndOfLine = lexer.readEndOfLine('a');

        // Assert
        Assert.assertFalse(isEndOfLine);
    }

    @Test
    public void testIsWhitespaceWithWhitespaceCharacter() {
        // Test
        boolean isWhitespace = lexer.isWhitespace(' ');

        // Assert
        Assert.assertTrue(isWhitespace);
    }

    @Test
    public void testIsWhitespaceWithNonWhitespaceCharacter() {
        // Test
        boolean isWhitespace = lexer.isWhitespace('a');

        // Assert
        Assert.assertFalse(isWhitespace);
    }

    @Test
    public void testIsStartOfLineWithStartOfLineCharacter() {
        // Test
        boolean isStartOfLine = lexer.isStartOfLine('\n');

        // Assert
        Assert.assertTrue(isStartOfLine);
    }

    @Test
    public void testIsStartOfLineWithNonStartOfLineCharacter() {
        // Test
        boolean isStartOfLine = lexer.isStartOfLine('.');

        // Assert
        Assert.assertFalse(isStartOfLine);
    }

    @Test
    public void testIsEndOfFileWithEndOfFileCharacter() {
        // Test
        boolean isEndOfFile = lexer.isEndOfFile(-1);

        // Assert
        Assert.assertTrue(isEndOfFile);
    }

    @Test
    public void testIsEndOfFileWithNonEndOfFileCharacter() {
        // Test
        boolean isEndOfFile = lexer.isEndOfFile('a');

        // Assert
        Assert.assertFalse(isEndOfFile);
    }

    @Test
    public void testIsDelimiterWithDelimiterCharacter() {
        // Test
        boolean isDelimiter = lexer.isDelimiter(',');

        // Assert
        Assert.assertTrue(isDelimiter);
    }

    @Test
    public void testIsDelimiterWithNonDelimiterCharacter() {
        // Test
        boolean isDelimiter = lexer.isDelimiter('.');

        // Assert
        Assert.assertFalse(isDelimiter);
    }

    @Test
    public void testIsEscapeWithEscapeCharacter() {
        // Test
        boolean isEscape = lexer.isEscape('\\');

        // Assert
        Assert.assertTrue(isEscape);
    }

    @Test
    public void testIsEscapeWithNonEscapeCharacter() {
        // Test
        boolean isEscape = lexer.isEscape('.');

        // Assert
        Assert.assertFalse(isEscape);
    }

    @Test
    public void testIsQuoteCharWithQuoteCharacter() {
        // Test
        boolean isQuoteChar = lexer.isQuoteChar('"');

        // Assert
        Assert.assertTrue(isQuoteChar);
    }

    @Test
    public void testIsQuoteCharWithNonQuoteCharacter() {
        // Test
        boolean isQuoteChar = lexer.isQuoteChar('.');

        // Assert
        Assert.assertFalse(isQuoteChar);
    }

    @Test
    public void testIsCommentStartWithCommentStartCharacter() {
        // Test
        boolean isCommentStart = lexer.isCommentStart('#');

        // Assert
        Assert.assertTrue(isCommentStart);
    }

    @Test
    public void testIsCommentStartWithNonCommentStartCharacter() {
        // Test
        boolean isCommentStart = lexer.isCommentStart('.');

        // Assert
        Assert.assertFalse(isCommentStart);
    }

}