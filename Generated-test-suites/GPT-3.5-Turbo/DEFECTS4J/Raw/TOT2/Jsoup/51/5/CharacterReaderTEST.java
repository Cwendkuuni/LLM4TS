import static org.junit.Assert.*;
import org.junit.Test;

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
    }

    // Add more test cases for other methods in the CharacterReader class

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertTrue(reader.rangeEquals(0, 4, "This"));
    }

    @Test
    public void testRangeEqualsFalse() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertFalse(reader.rangeEquals(0, 4, "That"));
    }

    // Add test cases for edge cases and exception handling scenarios

    @Test
    public void testNextIndexOfNotFound() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(-1, reader.nextIndexOf('a'));
    }

    @Test
    public void testNextIndexOfFound() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
    }

}