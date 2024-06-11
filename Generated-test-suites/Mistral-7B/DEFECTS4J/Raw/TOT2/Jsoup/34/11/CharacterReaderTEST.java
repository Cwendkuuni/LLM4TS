
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private static final String TEST_INPUT = "testInput";

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader reader = new CharacterReader(TEST_INPUT);
        assertEquals(TEST_INPUT.toCharArray().length, reader.length);
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(TEST_INPUT);
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(TEST_INPUT);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderWithNullInput() {
        CharacterReader reader = new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderWithSpecialCharacterInput() {
        CharacterReader reader = new CharacterReader(TEST_INPUT + "#");
        // Test special character handling.
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCharacterReaderNegativeIndex() {
        CharacterReader reader = new CharacterReader(TEST_INPUT);
        reader.pos = -1;
        reader.unconsume();
    }

    @Test
    public void testCharacterReaderInputModification() {
        CharacterReader reader = new CharacterReader(TEST_INPUT);
        char[] input = reader.input;
        input[0] = 'X';
        assertNotSame(reader.input[0], 'X');
        // Test the behavior after modifying the input array.
    }
}
