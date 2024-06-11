import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test when the cursor position is at the beginning
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test when the reader is not empty
        CharacterReader reader = new CharacterReader("Test String");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test when there are characters remaining
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals('T', reader.current());
    }

    // Add test cases for all other public methods in the CharacterReader class

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("Test String");
        char[] charBuf = new char[]{'T', 'e', 's', 't'};
        String[] stringCache = new String[512];
        assertEquals("Test", CharacterReader.cacheString(charBuf, stringCache, 0, 4));
    }

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader("Test String");
        char[] charBuf = new char[]{'T', 'e', 's', 't'};
        String cached = "Test";
        assertTrue(CharacterReader.rangeEquals(charBuf, 0, 4, cached));
    }

    // Add more test cases for edge cases and exceptions

}