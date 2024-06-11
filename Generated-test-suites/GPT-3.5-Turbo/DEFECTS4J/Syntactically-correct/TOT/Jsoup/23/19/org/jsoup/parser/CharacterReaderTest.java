import org.jsoup.helper.Validate;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        String input = "This is a test input.";
        reader = new CharacterReader(input);
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        assertEquals(1, reader.pos());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.mark);
    }

    @Test
    public void testRewindToMark() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
        assertEquals(1, reader.pos());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This ", reader.consumeTo(' '));
        assertEquals(5, reader.pos());
    }

    @Test
    public void testConsumeToNotFound() {
        assertEquals("This is a test input.", reader.consumeTo('?'));
        assertEquals(reader.length - 1, reader.pos());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("is", reader.consumeToAny(' ', 'a'));
        assertEquals(2, reader.pos());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test input.", reader.consumeToEnd());
        assertEquals(reader.length - 1, reader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", reader.consumeLetterSequence());
        assertEquals(4, reader.pos());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
    }

    @Test
    public void testMatchesNotFound() {
        assertFalse(reader.matches('?'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("This"));
    }

    @Test
    public void testMatchesIgnoreCaseNotFound() {
        assertFalse(reader.matchesIgnoreCase("this"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 'h'));
    }

    @Test
    public void testMatchesAnyNotFound() {
        assertFalse(reader.matchesAny('a', 'b', 'c'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesLetterNotFound() {
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigitNotFound() {
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("This"));
        assertEquals(4, reader.pos());
    }

    @Test
    public void testMatchConsumeNotFound() {
        assertFalse(reader.matchConsume("this"));
        assertEquals(0, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("This"));
        assertEquals(4, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCaseNotFound() {
        assertFalse(reader.matchConsumeIgnoreCase("this"));
        assertEquals(0, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("TesT"));
    }

    @Test
    public void testContainsIgnoreCaseNotFound() {
        assertFalse(reader.containsIgnoreCase("test"));
    }
}