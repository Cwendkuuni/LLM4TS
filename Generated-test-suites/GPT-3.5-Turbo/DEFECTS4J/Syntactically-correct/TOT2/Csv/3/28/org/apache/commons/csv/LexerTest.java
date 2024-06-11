
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        // Create a sample ExtendedBufferedReader and CSVFormat
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();

        // Create a Lexer instance
        Lexer lexer = new Lexer(format, in);

        // Test the getLineNumber method
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() {
        // Create a sample ExtendedBufferedReader and CSVFormat
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();

        // Create a Lexer instance
        Lexer lexer = new Lexer(format, in);

        // Test the readEscape method
        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
        assertEquals(TAB, lexer.readEscape());
        assertEquals(BACKSPACE, lexer.readEscape());
        assertEquals(FF, lexer.readEscape());
    }

    // Add more test methods for other public methods in the Lexer class as needed
    
}
