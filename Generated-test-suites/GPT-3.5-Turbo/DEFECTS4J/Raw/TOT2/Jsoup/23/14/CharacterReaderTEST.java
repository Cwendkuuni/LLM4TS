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
        CharacterReader reader = new CharacterReader("Test String");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals('T', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test String");
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Test String");
        reader.advance();
        assertEquals('e', reader.current());
    }

    // Additional test cases for other methods can be added here

}