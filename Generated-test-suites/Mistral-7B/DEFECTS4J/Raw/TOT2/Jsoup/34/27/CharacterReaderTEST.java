import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

public class CharacterReaderTest {
    @Test
    public void testCharacterReader() {
        // Basic test case
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('h', reader.current());
        assertEquals('h', reader.consume());
        assertEquals(1, reader.pos());
        reader.unconsume();
        assertEquals('h', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
        reader.mark();
        reader.rewindToMark();
        assertEquals('h', reader.current());
        reader.consumeAsString();
        assertEquals("hello", reader.toString());

        // Edge cases and exception handling test cases
        // ...
    }

    // Edge cases and exception handling test cases go here
}