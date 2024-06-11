import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Example String");
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
        assertEquals('E', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('E', characterReader.consume());
        assertEquals('x', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('E', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.consume();
        characterReader.advance();
        assertEquals('a', characterReader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.consume();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('E', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("E", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Exampl", characterReader.consumeTo('e'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Exam", characterReader.consumeToAny('p', 'l'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Example String", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Example", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("S", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("tr", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('E'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("example"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('a', 'x'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Example"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("sTriNg"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("exAmPle string"));
    }
}