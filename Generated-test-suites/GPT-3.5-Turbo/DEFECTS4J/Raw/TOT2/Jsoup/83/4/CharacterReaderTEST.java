import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test the pos() method
        Reader input = new StringReader("Test Input");
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test the isEmpty() method
        Reader input = new StringReader("");
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test the current() method
        Reader input = new StringReader("Test Input");
        CharacterReader reader = new CharacterReader(input);
        assertEquals('T', reader.current());
    }

    // Add more test cases for other methods in the CharacterReader class

}