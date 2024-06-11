import org.junit.Test;
import static org.junit.Assert.*;

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
        CharacterReader reader = new CharacterReader("hello");
        assertEquals('h', reader.current());
    }

    // Add more test cases for other methods as needed

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello", reader.consumeTo(' '));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals("hello", reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("hello");
        assertTrue(reader.matches('h'));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.containsIgnoreCase("t"));
    }
}