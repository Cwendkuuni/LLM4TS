import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals((char) -1, characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals((char) -1, characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.unconsume();
        assertEquals(-1, characterReader.pos());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        assertEquals(0, characterReader.mark);
    }

    @Test
    public void testRewindToMark() {
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos);
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(-1, characterReader.nextIndexOf('a'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        assertEquals(-1, characterReader.nextIndexOf("abc"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("", characterReader.consumeTo('a'));
    }

    @Test
    public void testConsumeToWithCharSequence() {
        assertEquals("", characterReader.consumeTo("abc"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", characterReader.consumeToAny('a', 'b', 'c'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("", characterReader.consumeLetterThenDigitSequence());
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
    public void testMatches() {
        assertFalse(characterReader.matches('a'));
    }

    @Test
    public void testMatchesWithCharSequence() {
        assertFalse(characterReader.matches("abc"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertFalse(characterReader.matchesIgnoreCase("abc"));
    }

    @Test
    public void testMatchesAny() {
        assertFalse(characterReader.matchesAny('a', 'b', 'c'));
    }

    @Test
    public void testMatchesLetter() {
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertFalse(characterReader.matchConsume("abc"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertFalse(characterReader.matchConsumeIgnoreCase("abc"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(characterReader.containsIgnoreCase("abc"));
    }

    @Test
    public void testToString() {
        assertEquals("", characterReader.toString());
    }
}