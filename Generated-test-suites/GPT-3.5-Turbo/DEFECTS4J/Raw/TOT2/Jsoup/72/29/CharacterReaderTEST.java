import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test for position
        // Create a CharacterReader object and check the initial position
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test for isEmpty method
        // Create a CharacterReader object with a string and check if it is empty
        CharacterReader reader = new CharacterReader("Hello");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test for current method
        // Create a CharacterReader object with a string and check the current char
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals('H', reader.current());
    }

    // Continue with other methods...

}