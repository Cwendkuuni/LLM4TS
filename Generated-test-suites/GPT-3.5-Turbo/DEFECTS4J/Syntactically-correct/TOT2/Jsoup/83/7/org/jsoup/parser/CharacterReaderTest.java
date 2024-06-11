
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Testing pos method");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.consume());
    }

    // Add more test cases for other methods here

}
