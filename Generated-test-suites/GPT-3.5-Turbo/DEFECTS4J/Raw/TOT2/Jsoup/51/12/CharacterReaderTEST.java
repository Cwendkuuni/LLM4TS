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

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
    }

    // Add more test cases for other methods

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader("test");
        boolean result = reader.rangeEquals(0, 2, "te");
        assertTrue(result);
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("test");
        String cached = reader.cacheString(0, 2);
        assertEquals("tes", cached);
    }

}