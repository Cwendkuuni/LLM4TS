import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("This is a test string.");
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
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('h', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        assertEquals('h', reader.consume());
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
        assertEquals('h', reader.current());
        reader.advance();
        assertEquals('i', reader.current());
    }

    @Test
    public void testMark() {
        reader.advance();
        reader.mark();
        assertEquals('h', reader.current());
        reader.advance();
        reader.rewindToMark();
        assertEquals('h', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
        assertEquals("h", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, reader.nextIndexOf('h'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(1, reader.nextIndexOf("his"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This ", reader.consumeTo(' '));
        assertEquals("is a test", reader.consumeTo('.'));
    }

    @Test
    public void testConsumeToSequence() {
        assertEquals("This", reader.consumeTo(" is"));
        assertEquals(" is a test", reader.consumeTo("str"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This ", reader.consumeToAny('.'));
        assertEquals("is", reader.consumeToAny(' '));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("This", reader.consumeToAnySorted(' ', '.'));
        assertEquals("is", reader.consumeToAnySorted(' '));
    }

    @Test
    public void testConsumeData() {
        assertEquals("", reader.consumeData());
        reader.advance();
        assertEquals("his is", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("This", reader.consumeTagName());
        reader.advance();
        assertEquals("s", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string.", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", reader.consumeLetterSequence());
        reader.advance();
        assertEquals("is", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", reader.consumeLetterThenDigitSequence());
        reader.advance();
        assertEquals("1", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
        reader.advance();
        assertEquals("h", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        reader.advance();
        assertEquals("1", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        reader.consume();
        assertTrue(reader.matches('h'));
    }

    @Test
    public void testMatchesSequence() {
        assertTrue(reader.matches("Th"));
        reader.consume();
        assertTrue(reader.matches("his"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("th"));
        reader.consume();
        assertTrue(reader.matchesIgnoreCase("his"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T'));
        reader.consume();
        assertTrue(reader.matchesAny('h'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted('T', 'h'));
        reader.consume();
        assertTrue(reader.matchesAnySorted('h'));
    }

    @Test
    public void testMatchesLetter() {
        assertFalse(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("This"));
        assertEquals(' ', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("thIS"));
        assertEquals(' ', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("test"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testRangeEquals() {
        assertTrue(reader.rangeEquals(0, 4, "This"));
        assertFalse(reader.rangeEquals(0, 3, "This"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string.", reader.toString());
        reader.advance();
        assertEquals("his is a test string.", reader.toString());
    }

}