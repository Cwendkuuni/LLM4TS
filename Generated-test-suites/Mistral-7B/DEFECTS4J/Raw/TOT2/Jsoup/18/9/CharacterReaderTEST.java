import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.isEmpty());

        for (int i = 0; i < reader.length(); i++) {
            reader.consume();
        }

        assertTrue(reader.isEmpty());
    }
}