import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("test");
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
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('t', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('s', characterReader.consume());
        assertEquals('t', characterReader.consume());
        assertEquals((char)-1, characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("t", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("", characterReader.consumeTo('a'));
    }

    @Test
    public void testConsumeToString() {
        assertEquals("", characterReader.consumeTo("test"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", characterReader.consumeToAny('a', 'b'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("test", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("test", characterReader.consumeLetterSequence());
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
    public void testMatchesChar() {
        assertTrue(characterReader.matches('t'));
        assertFalse(characterReader.matches('a'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("test"));
        assertFalse(characterReader.matches("abc"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("Test"));
        assertFalse(characterReader.matchesIgnoreCase("ABC"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('a', 't'));
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
    public void testMatchConsumeString() {
        assertTrue(characterReader.matchConsume("test"));
        assertFalse(characterReader.matchConsume("abc"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        assertTrue(characterReader.matchConsumeIgnoreCase("Test"));
        assertFalse(characterReader.matchConsumeIgnoreCase("ABC"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(characterReader.containsIgnoreCase("AB"));
    }

    @Test
    public void testToString() {
        assertEquals("test", characterReader.toString());
    }
}