import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Test
    public void testPos() {
        characterReader = new CharacterReader("Hello");
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        characterReader = new CharacterReader("Hello");
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        characterReader = new CharacterReader("Hello");
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsume() {
        characterReader = new CharacterReader("Hello");
        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader = new CharacterReader("Hello");
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    // Add more test cases for each method

}