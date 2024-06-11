import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test the pos method in CharacterReader class
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test the isEmpty method in CharacterReader class
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test the current method in CharacterReader class
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals('T', reader.current());
    }

    // Add more test cases for other public methods in CharacterReader class

}