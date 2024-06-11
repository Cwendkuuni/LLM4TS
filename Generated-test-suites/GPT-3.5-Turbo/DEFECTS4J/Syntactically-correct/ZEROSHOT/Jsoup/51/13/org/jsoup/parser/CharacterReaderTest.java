import org.junit.*;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("This is a test string");
    }

    @After
    public void tearDown() {
        reader = null;
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
    }

    @Test
    public void testMarkAndRewindToMark() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, reader.nextIndexOf('h'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(2, reader.nextIndexOf("is"));
        assertEquals(-1, reader.nextIndexOf("abc"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This ", reader.consumeTo(' '));
        assertEquals("is ", reader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("is", reader.consumeToAny(' ', 'a'));
        assertEquals(" ", reader.consumeToAny(' ', 'a'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals(" ", reader.consumeToAnySorted(' ', 'a'));
        assertEquals("is", reader.consumeToAnySorted(' ', 'a'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("This", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("This", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("This", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("This", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('A'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("This"));
        assertFalse(reader.matches("That"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("tHIS"));
        assertFalse(reader.matchesIgnoreCase("tHAT"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 'A'));
        assertFalse(reader.matchesAny('A', 'B'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted('A', 'T'));
        assertFalse(reader.matchesAnySorted('B', 'C'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("This"));
        assertFalse(reader.matchConsume("That"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("tHIS"));
        assertFalse(reader.matchConsumeIgnoreCase("tHAT"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("is"));
        assertFalse(reader.containsIgnoreCase("that"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string", reader.toString());
    }
}