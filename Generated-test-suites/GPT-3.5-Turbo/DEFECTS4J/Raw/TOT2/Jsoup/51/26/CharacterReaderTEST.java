import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    // Additional test cases for other methods can be added here

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(6, reader.nextIndexOf('W'));
    }

    // Additional test cases for other edge cases and exceptions can be added here

}