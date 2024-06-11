import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    // Test case for isWhiteSpace method
    @Test
    public void testIsWhiteSpace() {
        Lexer lexer = new Lexer();
        assertFalse(lexer.isWhitespace(44)); // Not a whitespace character
        assertTrue(lexer.isWhitespace(32)); // Space character
    }

    // Test case for isStartOfLine method
    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer();
        assertTrue(lexer.isStartOfLine(UNDEFINED)); // Start of file
        assertTrue(lexer.isStartOfLine(LF)); // Line feed
        assertTrue(lexer.isStartOfLine(CR)); // Carriage return
    }

    // Test case for isEndOfFile method
    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer();
        assertFalse(lexer.isEndOfFile(44)); // Not end of file
        assertTrue(lexer.isEndOfFile(END_OF_STREAM)); // End of file
    }

    // Add more test cases for other methods and edge cases

}