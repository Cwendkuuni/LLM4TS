import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("This is a test string");
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
        assertEquals('i', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        assertEquals('T', characterReader.consume());
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.advance();
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
    public void testNextIndexOf() {
        assertEquals(2, characterReader.nextIndexOf('i'));
        assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        assertEquals(2, characterReader.nextIndexOf("is"));
        assertEquals(-1, characterReader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This ", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToWithCharSequence() {
        assertEquals("This ", characterReader.consumeTo("is"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Th", characterReader.consumeToAny('i', 's'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("Thi", characterReader.consumeToAnySorted('i', 's', 't'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("This", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("This", characterReader.consumeTagName());
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
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("This", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('A'));
    }

    @Test
    public void testMatchesWithCharSequence() {
        assertTrue(characterReader.matches("Th"));
        assertFalse(characterReader.matches("Ab"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("th"));
        assertFalse(characterReader.matchesIgnoreCase("aB"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'A'));
        assertFalse(characterReader.matchesAny('A', 'B'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('A', 'T'));
        assertFalse(characterReader.matchesAnySorted('B', 'C'));
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
        assertTrue(characterReader.matchConsume("This"));
        assertEquals(' ', characterReader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("this"));
        assertEquals(' ', characterReader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("sS"));
        assertFalse(characterReader.containsIgnoreCase("AB"));
    }
}