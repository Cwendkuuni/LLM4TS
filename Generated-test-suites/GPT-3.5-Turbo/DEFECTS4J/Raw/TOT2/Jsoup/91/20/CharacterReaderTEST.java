import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("test string"));
        assertEquals(0, reader.pos());
    }

    // Additional test cases for other methods
}