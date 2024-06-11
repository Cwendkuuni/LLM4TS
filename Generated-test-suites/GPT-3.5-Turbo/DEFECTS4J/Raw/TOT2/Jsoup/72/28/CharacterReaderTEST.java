import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        // Test if pos() returns the correct position
        CharacterReader characterReader = new CharacterReader("Test");
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test if isEmpty() returns true when all content has been read
        CharacterReader characterReader = new CharacterReader("");
        assertTrue(characterReader.isEmpty());
    }

    // Add more test cases for each method in the CharacterReader class

    @Test
    public void testRangeEquals() {
        char[] testCharArr = {'t', 'e', 's', 't'};
        String testString = "test";
        assertTrue(CharacterReader.rangeEquals(testCharArr, 0, 4, testString));
    }

    @Test
    public void testCompleteStringCache() {
        // Test if cacheString method returns a new string when count exceeds maxStringCacheLen
        CharacterReader characterReader = new CharacterReader("Testing cacheString method");
        String cachedString = characterReader.cacheString(characterReader.charBuf, characterReader.stringCache, 0, 28);
        assertEquals("Testing cacheString method", cachedString);
    }

    // Include test cases for edge cases and potential exceptions

}