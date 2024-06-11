import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test Input");
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
        assertEquals('e', characterReader.consume());
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
    public void testMark() {
        characterReader.mark();
        assertEquals(0, characterReader.pos());
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
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf("e"));
        assertEquals(-1, characterReader.nextIndexOf("z"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test ", characterReader.consumeTo("I"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Test", characterReader.consumeToAny(' ', 'I'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("T", characterReader.consumeToAnySorted('A', 'B', 'C', 'T'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("Test", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Test", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test Input", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Test", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("Test", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("Test", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("T"));
        assertFalse(characterReader.matchesIgnoreCase("z"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'z'));
        assertFalse(characterReader.matchesAny('A', 'B', 'C'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted(new char[] {'A', 'B', 'C', 'T'}));
        assertFalse(characterReader.matchesAnySorted(new char[] {'A', 'B', 'C'}));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Test"));
        assertFalse(characterReader.matchConsume("Input"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("Test"));
        assertFalse(characterReader.matchConsumeIgnoreCase("Input"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(characterReader.containsIgnoreCase("Title"));
        assertTrue(characterReader.containsIgnoreCase("est"));
    }

    @Test
    public void testToString() {
        assertEquals("Test Input", characterReader.toString());
    }

    @Test
    public void testCacheString() {
        assertEquals("Test", characterReader.cacheString(0, 4));
    }

    @Test
    public void testRangeEquals() {
        assertTrue(characterReader.rangeEquals(0, 4, "Test"));
        assertFalse(characterReader.rangeEquals(0, 4, "Test "));
    }
}