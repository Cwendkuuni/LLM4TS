import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        // Initialize Lexer object
        Lexer lexer = new Lexer(format, in);
        
        // Test getLineNumber method
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape() {
        // Initialize Lexer object
        Lexer lexer = new Lexer(format, in);
        
        // Test readEscape method with valid escape characters
        // Add test cases for different escape characters
        int escapedCR = lexer.readEscape();
        assertEquals('\r', escapedCR);
        
        int escapedLF = lexer.readEscape();
        assertEquals('\n', escapedLF);
        
        // Add more test cases for other escape characters
        // Test readEscape method with invalid escape characters
        // Add test cases for invalid escape characters and verify the return value
        
    }

    @Test
    public void testTrimTrailingSpaces() {
        // Initialize Lexer object
        Lexer lexer = new Lexer(format, in);
        
        // Test trimTrailingSpaces method
        StringBuilder buffer = new StringBuilder("   test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("   test", buffer.toString());
    }

    // Add more test cases for other methods in Lexer class
    
}