
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    CharacterReader characterReader;

    @Test
    public void testPosInitialPositionZero() {
        characterReader = new CharacterReader(new StringReader(""));
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmptyEmptyContent() {
        characterReader = new CharacterReader(new StringReader(""));
        assertTrue(characterReader.isEmpty());
    }

    // Add more test cases for all methods

}