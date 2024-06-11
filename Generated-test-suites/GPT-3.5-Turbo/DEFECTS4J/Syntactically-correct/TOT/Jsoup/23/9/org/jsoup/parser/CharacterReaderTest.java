import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        String input = "This is a test string";
        characterReader = new CharacterReader(input);
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

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeTo_String() {
        assertEquals("This is", characterReader.consumeTo(" a"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This", characterReader.consumeToAny(' '));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "A12F";
        characterReader = new CharacterReader(input);
        assertEquals("A12F", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "12345";
        characterReader = new CharacterReader(input);
        assertEquals("12345", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('t'));
    }

    @Test
    public void testMatches_String() {
        assertTrue(characterReader.matches("This"));
        assertFalse(characterReader.matches("this"));
    }

    @Test
    public void testMatchesIgnoreCase_String() {
        assertTrue(characterReader.matchesIgnoreCase("this"));
        assertTrue(characterReader.matchesIgnoreCase("THIS"));
        assertFalse(characterReader.matchesIgnoreCase("that"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'h'));
        assertFalse(characterReader.matchesAny('t', 'H'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesLetter());
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("This"));
        assertFalse(characterReader.matchConsume("this"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("this"));
        assertTrue(characterReader.matchConsumeIgnoreCase("THIS"));
        assertFalse(characterReader.matchConsumeIgnoreCase("that"));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "This is a test";
        characterReader = new CharacterReader(input);
        assertTrue(characterReader.containsIgnoreCase("This"));
        assertTrue(characterReader.containsIgnoreCase("THiS"));
        assertFalse(characterReader.containsIgnoreCase("that"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string", characterReader.toString());
    }
}