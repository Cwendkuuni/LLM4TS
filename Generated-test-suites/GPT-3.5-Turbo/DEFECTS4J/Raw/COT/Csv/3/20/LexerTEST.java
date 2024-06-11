
import org.apache.commons.csv.Lexer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setUp() {
        // Initialize the Lexer with a CSV format and an input stream
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        lexer = new Lexer(format, in);
    }

    @Test
    public void testGetLineNumber() {
        // Test if getLineNumber returns the correct value
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() {
        try {
            // Test a valid escape sequence '\n'
            lexer.readEscape();  // read the escape character '\'
            int escapedChar = lexer.readEscape();  // read the escaped character 'n'
            assertEquals('\n', escapedChar);

            // Test an invalid escape sequence '\x'
            lexer.readEscape();  // read the escape character '\'
            int invalidChar = lexer.readEscape();  // read the invalid escaped character 'x'
            assertEquals(Lexer.END_OF_STREAM, invalidChar);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

    }

    @Test
    public void testTrimTrailingSpaces() {
        // Test trimming trailing spaces from a StringBuilder
        StringBuilder buffer = new StringBuilder("test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());

        // Test trimming trailing spaces where no spaces are present
        buffer = new StringBuilder("test");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());

        // Test trimming trailing spaces where all characters are spaces
        buffer = new StringBuilder("   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        try {
            // Test reading an LF character '\n'
            assertTrue(lexer.readEndOfLine('\n'));

            // Test reading a CR character '\r'
            assertTrue(lexer.readEndOfLine('\r'));

            // Test reading a CR and LF sequence '\r\n'
            assertTrue(lexer.readEndOfLine('\r'));

            // Test reading a character that is not a line terminator
            assertFalse(lexer.readEndOfLine('a'));
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIsWhitespace() {
        // Test a whitespace character ' ' (space)
        assertTrue(lexer.isWhitespace(' '));

        // Test a non-whitespace character 'a'
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    public void testIsStartOfLine() {
        // Test a start of line character LF '\n'
        assertTrue(lexer.isStartOfLine('\n'));

        // Test a start of line character CR '\r'
        assertTrue(lexer.isStartOfLine('\r'));

        // Test a start of file character UNDEFINED
        assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));

        // Test a non-start of line character 'a'
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        // Test an end of file character END_OF_STREAM
        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));

        // Test a non-end of file character 'a'
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        // Test a delimiter character ',' (comma)
        assertTrue(lexer.isDelimiter(','));

        // Test a non-delimiter character 'a'
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    public void testIsEscape() {
        // Test an escape character '\\' (backslash)
        assertTrue(lexer.isEscape('\\'));

        // Test a non-escape character 'a'
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    public void testIsQuoteChar() {
        // Test a quote character '"' (double quote)
        assertTrue(lexer.isQuoteChar('"'));

        // Test a non-quote character 'a'
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    public void testIsCommentStart() {
        // Test a comment start character '#' (hash)
        assertTrue(lexer.isCommentStart('#'));

        // Test a non-comment start character 'a'
        assertFalse(lexer.isCommentStart('a'));
    }
}

