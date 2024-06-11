import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        Reader input = new StringReader("test");
        CharacterReader reader = new CharacterReader(input);

        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        Reader input = new StringReader("test");
        CharacterReader reader = new CharacterReader(input);

        assertFalse(reader.isEmpty());
        reader.advance();
        assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Reader input = new StringReader("test");
        CharacterReader reader = new CharacterReader(input);

        assertEquals('t', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
    }

    // Continue testing all methods in the CharacterReader class

}