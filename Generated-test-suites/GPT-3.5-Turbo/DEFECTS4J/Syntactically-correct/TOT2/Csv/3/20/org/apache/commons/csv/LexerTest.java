import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        // Test for getting line number
        // Test case 1
        // Edge case: Empty line
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertEquals(0, lexer.getLineNumber());

        // Test case 2
        // Typical case: Line with content
        lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        lexer.in.incrementLineNumber();
        assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() {
        // Test for reading escape sequence
        // Test case 1
        // Edge case: Invalid escape sequence
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        try {
            lexer.readEscape();
        } catch (IOException e) {
            assertEquals("EOF whilst processing escape sequence", e.getMessage());
        }

        // Test case 2
        // Typical case: Valid escape sequence
        lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertEquals('\\', lexer.readEscape());
    }

    @Test
    public void testTrimTrailingSpaces() {
        // Test for trimming trailing spaces
        // Test case 1
        // Typical case: Trimming spaces from a string
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        StringBuilder buffer = new StringBuilder("  trim  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("  trim", buffer.toString());

        // Test case 2
        // Edge case: Empty string
        buffer = new StringBuilder("");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }

    // Add more test cases for other methods as needed

}