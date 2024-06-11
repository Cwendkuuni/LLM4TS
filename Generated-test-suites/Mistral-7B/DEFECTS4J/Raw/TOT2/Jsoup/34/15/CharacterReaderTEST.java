
import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderWithValidInput() {
        CharacterReader reader = new CharacterReader("TestInput");
        // Test other methods here
    }

    @Test
    public void testPosMethod() {
        CharacterReader reader = new CharacterReader("TestInput");
        assertEquals(0, reader.pos());
        reader.consume();
        reader.consume();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader reader = new CharacterReader("TestInput");
        assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrentAndConsumeMethods() {
        CharacterReader reader = new CharacterReader("TestInput");
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderEmptyInput() {
        new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderSpecialCharacters() {
        CharacterReader reader = new CharacterReader("TestInput@#%");
        // Test other methods here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderInvalidInput() {
        CharacterReader reader = new CharacterReader("\uD800"); // High surrogate
        reader.consume(); // This will throw an exception
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("TestInput");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeBeyondEndOfInput() {
        CharacterReader reader = new CharacterReader("TestInput");
        reader.consumeToEnd();
        reader.consume();
    }

    @Test
    public void testConsumeAndUnconsume() {
        CharacterReader reader = new CharacterReader("TestInput");
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    // Add other test methods here if necessary
}