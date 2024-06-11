import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        String input = "This is a test input.";
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
    public void testMark() {
        characterReader.mark();
        characterReader.advance();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testRewindToMark() {
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
    public void testConsumeTo_char() {
        assertEquals("This is a ", characterReader.consumeTo('t'));
        assertEquals("tes", characterReader.consumeTo('i'));
    }

    @Test
    public void testConsumeTo_String() {
        assertEquals("This is a", characterReader.consumeTo("te"));
        assertEquals("t", characterReader.consumeTo("input"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This is a ", characterReader.consumeToAny('.', '!'));
        assertEquals("", characterReader.consumeToAny());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test input.", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('i'));
    }

    @Test
    public void testMatches_string() {
        assertTrue(characterReader.matches("This"));
        assertFalse(characterReader.matches("is"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("this"));
        assertFalse(characterReader.matchesIgnoreCase("IS"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'i'));
        assertFalse(characterReader.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("This"));
        assertFalse(characterReader.matchConsume("is"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("this"));
        assertFalse(characterReader.matchConsumeIgnoreCase("IS"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("test"));
        assertFalse(characterReader.containsIgnoreCase("INPUT"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test input.", characterReader.toString());
    }
}