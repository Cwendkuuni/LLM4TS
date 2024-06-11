import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Create a CharacterReader object with a string input
        CharacterReader cr = new CharacterReader("Hello World");
        // Test if pos() returns the correct position
        assertEquals(0, cr.pos());
    }

    @Test
    public void testIsEmpty() {
        // Create a CharacterReader object with an empty string input
        CharacterReader cr = new CharacterReader("");
        // Test if isEmpty() returns true for an empty string
        assertTrue(cr.isEmpty());
    }

    // Add more test cases for other methods as required

}