import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test String");
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
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(3, characterReader.nextIndexOf('t'));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("es", characterReader.consumeTo('t'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Test ", characterReader.consumeToAny(' ', 'r'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("Test", characterReader.consumeData());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals(" String", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123 Test");
        reader.consumeLetterSequence();
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("AB12CD");
        assertEquals("AB12", reader.consumeHexSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
    }

    @Test
    public void testMatchConsume() {
        characterReader.matchConsume("Test");
        assertEquals(' ', characterReader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("TesT"));
    }
}