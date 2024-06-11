
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader createCharacterReader(String input) {
        return new CharacterReader(input);
    }

    @Test
    public void testIsEmptyEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testIsEmptyConsumedAllCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consumeAll(); // consume all characters
        assertTrue(reader.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEmptyNullInput() {
        CharacterReader reader = new CharacterReader(null);
    }

    @Test
    public void testIsEmptyNoCharactersToConsume() {
        CharacterReader reader = new CharacterReader("ab");
        assertFalse(reader.isEmpty());
        reader.consumeAll(); // consume all characters
        assertTrue(reader.isEmpty());
    }

    // Add test cases for remaining methods here...

}
