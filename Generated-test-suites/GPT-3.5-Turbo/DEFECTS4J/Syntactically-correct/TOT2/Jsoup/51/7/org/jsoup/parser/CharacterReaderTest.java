import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    CharacterReader characterReader = new CharacterReader("Test Input");

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(2, characterReader.nextIndexOf("st"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test Inpu", characterReader.consumeTo('t'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Tes", characterReader.consumeToAny('t', 'x', 'y'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test Input", characterReader.consumeToEnd());
    }

    // Additional test cases for other methods should be added accordingly
}