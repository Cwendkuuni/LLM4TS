import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Initialize CharacterReader with "Test"
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Initialize CharacterReader with "Test"
        CharacterReader reader = new CharacterReader("Test");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Initialize CharacterReader with "Test"
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());
    }

    // Add more test cases for other public methods in the CharacterReader class

}