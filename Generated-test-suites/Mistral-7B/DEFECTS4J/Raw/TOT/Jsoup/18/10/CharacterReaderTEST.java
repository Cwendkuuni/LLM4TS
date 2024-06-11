import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharacterReaderTest {
    @Test
    public void testBasicFunctionality() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);

        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('c', reader.consume());
        // ... add more assertions for other methods and edge cases
    }
}