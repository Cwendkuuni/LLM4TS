import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
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

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    // Add more test cases for other methods

    // Edge cases and exceptions test cases

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.rangeEquals(0, 4, "Test"));
        assertFalse(reader.rangeEquals(0, 3, "Test"));
    }

}