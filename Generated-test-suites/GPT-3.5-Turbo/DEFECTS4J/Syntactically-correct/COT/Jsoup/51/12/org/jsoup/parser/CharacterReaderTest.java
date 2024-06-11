import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Test input");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
    }

    // Additional test cases for edge cases and exceptions:

    @Test
    public void testNextIndexOf() {
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(1, reader.nextIndexOf("es"));
        assertEquals(-1, reader.nextIndexOf("x"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test", reader.consumeTo(' '));
        assertEquals("input", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Test", reader.consumeToAny(' ', 'x'));
        assertEquals(" input", reader.consumeToAny('x'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("", reader.consumeToAnySorted('x', 'y', 'z'));
        assertEquals("Test", reader.consumeToAnySorted('e', ' ', 'x'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("Test", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Tes", reader.consumeTagName());
        reader.consume(); // skip space
        assertEquals("i", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test input", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", reader.consumeLetterSequence());
        reader.consume(); // skip space
        assertEquals("input", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Test", reader.consumeLetterThenDigitSequence());
        reader.consume(); // skip space
        assertEquals("input", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
        reader.consume(); // skip space
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        reader.consume(); // skip space
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("t"));
        assertFalse(reader.matchesIgnoreCase("e"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 'e'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted('T', 'e'));
        assertFalse(reader.matchesAnySorted('x'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.consume(); // skip space
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("Test"));
        assertEquals(" input", reader.toString());
        assertFalse(reader.matchConsume("x"));
        assertEquals(" input", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("tEST"));
        assertEquals(" input", reader.toString());
        assertFalse(reader.matchConsumeIgnoreCase("x"));
        assertEquals(" input", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("test"));
        assertFalse(reader.containsIgnoreCase("x"));
    }

    @Test
    public void testRangeEquals() {
        assertTrue(reader.rangeEquals(0, 4, "Test"));
        assertFalse(reader.rangeEquals(0, 4, "test"));
    }

    @Test
    public void testToString() {
        assertEquals("Test input", reader.toString());
    }
}