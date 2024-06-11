import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        CharacterReader reader2 = new CharacterReader("test");
        assertFalse(reader2.isEmpty());
    }

    // Add test cases for other public methods

    @Test
    public void testEdgeCases() {
        // Add test cases for edge cases
    }

    @Test
    public void testExceptions() {
        // Add test cases for exception handling scenarios
    }

}