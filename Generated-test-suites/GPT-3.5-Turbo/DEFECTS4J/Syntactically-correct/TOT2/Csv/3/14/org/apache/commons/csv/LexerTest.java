import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testMapNullToDisabled() {
        char disabled = Lexer.mapNullToDisabled(null);
        assertEquals('\ufffe', disabled);
    }

    @Test
    public void testReadEscape() {
        // Negative case testing invalid escape sequence
        try {
            // To test if exception is thrown if invalid character follows escape
            // Simulating the case where a semicolon follows a backslash which is an invalid escape sequence
            Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader(new StringReader("\\")));
            int escapeResult = lexer.readEscape();
            assertEquals(Lexer.END_OF_STREAM, escapeResult);
        } catch (IOException e) {
            fail("Unexpected IOException thrown: " + e.getMessage());
        }
        
        // Positive case testing valid escape sequence
        try {
            // Simulating the case where newline character follows a backslash which is a valid escape sequence
            Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader(new StringReader("\\n")));
            int escapeResult = lexer.readEscape();
            assertEquals('\n', escapeResult);
        } catch (IOException e) {
            fail("Unexpected IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("   text   ");
        Lexer.trimTrailingSpaces(buffer);
        assertEquals("   text", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        // Positive case testing LF character
        try {
            Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader(new StringReader("\n")));
            assertTrue(lexer.readEndOfLine('\n'));
        } catch (IOException e) {
            fail("Unexpected IOException thrown: " + e.getMessage());
        }

        // Positive case testing CR character followed by LF character
        try {
            Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader(new StringReader("\r\n")));
            assertTrue(lexer.readEndOfLine('\r'));
        } catch (IOException e) {
            fail("Unexpected IOException thrown: " + e.getMessage());
        }

        // Negative case testing other characters
        try {
            Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader(new StringReader("abc")));
            assertFalse(lexer.readEndOfLine('a'));
        } catch (IOException e) {
            fail("Unexpected IOException thrown: " + e.getMessage());
        }
    }

    // More test cases can be added for the remaining methods in Lexer class

}