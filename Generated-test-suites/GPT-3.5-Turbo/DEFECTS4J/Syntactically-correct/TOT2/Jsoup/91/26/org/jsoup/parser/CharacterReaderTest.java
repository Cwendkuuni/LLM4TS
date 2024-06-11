
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test if the initial position is 0
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test if isEmpty returns false for non-empty input
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test if current returns the correct character
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals('t', reader.current());
    }

    // Tests for other methods...
}

