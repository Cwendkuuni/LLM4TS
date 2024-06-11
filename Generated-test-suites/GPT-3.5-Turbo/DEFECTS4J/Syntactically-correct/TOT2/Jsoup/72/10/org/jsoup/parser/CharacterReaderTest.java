import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test when character reader is created
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals(0, reader.pos());

        // Test after advancing the position
        reader.advance();
        assertEquals(1, reader.pos());

        // Test after consuming characters
        reader.consume();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Test");
        assertFalse(reader.isEmpty());

        while (!reader.isEmpty()) {
            reader.consume();
        }

        assertTrue(reader.isEmpty());
    }

    // Add more test cases for other methods

}