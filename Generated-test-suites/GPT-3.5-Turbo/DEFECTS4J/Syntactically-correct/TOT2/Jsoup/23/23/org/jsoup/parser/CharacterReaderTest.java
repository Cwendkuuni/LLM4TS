import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals('T', reader.current());
    }

    // Add more test cases for other methods in CharacterReader class

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals("Test String", reader.toString());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("Test String");
        reader.advance();
        reader.mark();
        reader.advance();

        assertEquals('s', reader.current());

        reader.rewindToMark();

        assertEquals('T', reader.current());
    }

    // Additional test cases for edge cases and exceptions

}