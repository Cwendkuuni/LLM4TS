import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test the pos method
        CharacterReader cr = new CharacterReader("Hello");
        assertEquals(0, cr.pos());
        cr.consume();
        assertEquals(1, cr.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test the isEmpty method
        CharacterReader cr = new CharacterReader("");
        assertTrue(cr.isEmpty());
        CharacterReader cr2 = new CharacterReader("Hello");
        assertFalse(cr2.isEmpty());
    }

    // Add test cases for other methods

}