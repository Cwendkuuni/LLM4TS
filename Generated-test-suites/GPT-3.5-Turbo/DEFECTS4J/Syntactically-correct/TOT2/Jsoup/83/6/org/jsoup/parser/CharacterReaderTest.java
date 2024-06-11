import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        Reader input = new StringReader("Test input");
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        Reader input = new StringReader("");
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Reader input = new StringReader("Test input");
        CharacterReader reader = new CharacterReader(input);
        assertEquals('T', reader.current());
    }

    // Additional test cases here...

}