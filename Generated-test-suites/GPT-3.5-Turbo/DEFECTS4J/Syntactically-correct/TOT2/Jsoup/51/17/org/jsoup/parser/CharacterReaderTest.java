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
        CharacterReader reader = new CharacterReader("Test");
        assertFalse(reader.isEmpty());
    }

    // Add more test cases for other methods in CharacterReader class

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(true, reader.rangeEquals(0, 4, "Test"));
        assertEquals(false, reader.rangeEquals(0, 4, "test"));
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("Testing");
        assertEquals(2, reader.nextIndexOf('s'));
    }

    // Add more test cases for other methods in CharacterReader class

    // Add test cases for edge cases and exception handling scenarios

}