import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharacterReaderTest {
    @Test
    public void testCharacterReaderConstruction() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.input);
    }

    // Test other methods with basic usage here...
}