import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test initial position
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test empty string
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        // Test non-empty string
        reader = new CharacterReader("Test");
        assertFalse(reader.isEmpty());
    }

    // Add test cases for other methods as per the requirements

}