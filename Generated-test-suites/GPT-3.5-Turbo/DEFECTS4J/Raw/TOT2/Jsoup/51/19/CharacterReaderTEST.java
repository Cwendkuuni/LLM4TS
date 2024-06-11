import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

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

    // Add more test cases for other methods as per requirements

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("testing cache");
        assertEquals("cache", reader.cacheString(8, 5));
    }

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader("testing range equals");
        assertTrue(reader.rangeEquals(8, 5, "range"));
    }

    // Add more test cases for other methods as per requirements
}