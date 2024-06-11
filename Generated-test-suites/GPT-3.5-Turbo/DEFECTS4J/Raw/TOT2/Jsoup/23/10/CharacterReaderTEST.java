import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("This is a test string!");
    }

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
        assertEquals('h', characterReader.consume());
    }

    // Add more test cases for other methods

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string!", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string!", characterReader.toString());
    }

    // Include edge cases and error scenarios

    @Test
    public void testConsumeToEdgeCase() {
        assertEquals("This", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeLetterSequenceEdgeCase() {
        assertEquals("This", characterReader.consumeLetterSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('t'));
    }

    // Add more edge case and error scenario tests

}