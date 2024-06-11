import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        // Initialize Lexer and test getLineNumber method
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() {
        // Initialize Lexer and test readEscape method
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        // Add test cases for escape characters
        // Verify the correct unescaped characters are returned
    }

    @Test
    public void testTrimTrailingSpaces() {
        // Initialize Lexer and test trimTrailingSpaces method
        // Add test cases for strings with trailing spaces
        // Verify that trailing spaces are removed
    }

    @Test
    public void testReadEndOfLine() {
        // Initialize Lexer and test readEndOfLine method
        // Add test cases for different line endings
        // Verify correct handling of line endings
    }

    @Test
    public void testIsWhitespace() {
        // Initialize Lexer and test isWhitespace method
        // Add test cases for whitespace characters
        // Verify correct identification of whitespace characters
    }

    @Test
    public void testIsStartOfLine() {
        // Initialize Lexer and test isStartOfLine method
        // Add test cases for start of line characters
        // Verify correct identification of start of line characters
    }

    @Test
    public void testIsEndOfFile() {
        // Initialize Lexer and test isEndOfFile method
        // Add test cases for end of file characters
        // Verify correct identification of end of file characters
    }

    @Test
    public void testIsDelimiter() {
        // Initialize Lexer and test isDelimiter method
        // Add test cases for delimiter characters
        // Verify correct identification of delimiter characters
    }

    @Test
    public void testIsEscape() {
        // Initialize Lexer and test isEscape method
        // Add test cases for escape characters
        // Verify correct identification of escape characters
    }

    @Test
    public void testIsQuoteChar() {
        // Initialize Lexer and test isQuoteChar method
        // Add test cases for quote characters
        // Verify correct identification of quote characters
    }

    @Test
    public void testIsCommentStart() {
        // Initialize Lexer and test isCommentStart method
        // Add test cases for comment start characters
        // Verify correct identification of comment start characters
    }
}