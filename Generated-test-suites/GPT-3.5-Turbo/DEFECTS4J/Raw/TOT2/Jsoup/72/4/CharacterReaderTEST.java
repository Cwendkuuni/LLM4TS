import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test getting the current cursor position
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test checking if content has been read
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        assertFalse(reader.isEmpty());
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test getting the char at the current position
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    // Add more test cases for other methods...

}