import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        // Test the getLineNumber method
        // Edge case: Input stream is empty
        // In this case, the line number should be 0
        Lexer lexer = new Lexer(format, in);
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testEscapeHandling() {
        // Test the readEscape method
        // Edge case: Escape character is followed by an invalid character
        // In this case, the method should return END_OF_STREAM
        Lexer lexer = new Lexer(format, in);
        try {
            assertEquals(END_OF_STREAM, lexer.readEscape());
        } catch (IOException e) {
            fail("IOException thrown unexpectedly");
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        // Test the trimTrailingSpaces method
        // Typical case: Trailing spaces are present at the end of the buffer
        StringBuilder buffer = new StringBuilder("Test String   ");
        Lexer lexer = new Lexer(format, in);
        lexer.trimTrailingSpaces(buffer);
        assertEquals("Test String", buffer.toString());
    }

    // Add more test cases for other methods as needed

}