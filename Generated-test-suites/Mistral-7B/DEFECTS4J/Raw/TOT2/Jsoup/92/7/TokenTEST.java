import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Character;

public class TokenTest {

    @Test
    public void testDoctype() {
        // Basic test
        Doctype token = new Doctype();
        assertEquals("Doctype", token.tokenType());
        // Test getter methods
        token.getName("HTML");
        assertEquals("HTML", token.getName());
        token.setForceQuirks(true);
        assertTrue(token.isForceQuirks());
        // Test reset method
        token.reset();
        assertFalse(token.isForceQuirks());
    }

    @Test
    public void testCharacter() {
        // Basic test
        Character token = new Character('A');
        assertEquals("Character", token.tokenType());
        assertEquals('A', token.getData());
        // Test reset method
        token.reset();
        assertNull(token.getData());
        // Test exception handling - invalid character
        try {
            new Character('\u0000');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add test cases for StartTag and other token types here

    // Test edge cases and exceptions
    // ...

    // Merge all individual test cases into the complete JUnit 4 test file
}