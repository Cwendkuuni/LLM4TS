import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CharacterReaderTest {
    // Test for pos()
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
    }

    // Test for isEmpty()
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    // Test for current()
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());
    }

    // Add more test cases for other methods here

}