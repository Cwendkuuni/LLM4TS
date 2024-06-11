import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        try {
            ExtendedBufferedReader reader = new ExtendedBufferedReader();
            CSVFormat format = new CSVFormat();
            Lexer lexer = new Lexer(format, reader);

            long lineNumber = lexer.getLineNumber();

            // Assert
            assertEquals(0, lineNumber);
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }

    @Test
    public void testReadEscape_ValidEscapeSequence() {
        try {
            ExtendedBufferedReader reader = new ExtendedBufferedReader();
            CSVFormat format = new CSVFormat();
            Lexer lexer = new Lexer(format, reader);

            // Set up
            reader.setReadResult('t');
            reader.setLastChar('\\');

            // Act
            int result = lexer.readEscape();

            // Assert
            assertEquals(9, result);
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }

    @Test(expected = IOException.class)
    public void testReadEscape_InvalidEscapeCharacter() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Set up
        reader.setReadResult('a');
        reader.setLastChar('\\');

        // Act
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces_TrailingSpacesExist() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Set up
        StringBuilder buffer = new StringBuilder("example  ");

        // Act
        lexer.trimTrailingSpaces(buffer);

        // Assert
        assertEquals("example", buffer.toString());
    }

    @Test
    public void testIsWhitespace_WhitespaceCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isWhitespace(' ');

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsWhitespace_NonWhitespaceCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isWhitespace('a');

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsStartOfLine_StartOfLineCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isStartOfLine('\n');

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsStartOfLine_NonStartOfLineCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isStartOfLine('a');

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsEndOfFile_EndOfFileCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isEndOfFile(-1);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEndOfFile_NonEndOfFileCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isEndOfFile(0);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsDelimiter_DelimiterCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isDelimiter(',');

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsDelimiter_NonDelimiterCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isDelimiter('a');

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsEscape_EscapeCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isEscape('\\');

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEscape_NonEscapeCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isEscape('a');

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsQuoteChar_QuoteCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isQuoteChar('\"');

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsQuoteChar_NonQuoteCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isQuoteChar('a');

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsCommentStart_CommentStartCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isCommentStart('#');

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsCommentStart_NonCommentStartCharacter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isCommentStart('a');

        // Assert
        assertFalse(result);
    }

    // TODO Add more test cases for the remaining methods

}