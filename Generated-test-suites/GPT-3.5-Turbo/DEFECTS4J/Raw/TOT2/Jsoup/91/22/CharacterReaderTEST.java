import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("TestString");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.advance();
        assertEquals('e', reader.current());
    }

    // Add more test cases for other methods

}