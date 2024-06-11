import static org.junit.Assert.*;
import org.junit.Test;

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
        assertEquals('T',reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals('T',reader.consume());
        assertEquals('e',reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test String");
        reader.consume();
        reader.unconsume();
        assertEquals('T',reader.current());
    }

    // Additional test cases for other methods should be added here

}