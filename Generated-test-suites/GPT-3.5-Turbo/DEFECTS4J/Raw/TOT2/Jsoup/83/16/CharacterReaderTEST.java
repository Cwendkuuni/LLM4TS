import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Initialize
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));

        // Test
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Initialize
        CharacterReader characterReader = new CharacterReader(new StringReader(""));

        // Test
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Initialize
        CharacterReader characterReader = new CharacterReader(new StringReader("Test"));

        // Test
        assertEquals('T', characterReader.current());
    }

    // Add more test cases for other methods as needed

    @Test
    public void testRangeEquals() {
        // Initialize
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));

        // Test
        assertTrue(characterReader.rangeEquals(0, 4, "Test"));
    }
}