import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader reader;
    
    @Before
    public void setUp() {
        reader = new CharacterReader("Hello, World!");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        for (int i = 0; i < 12; i++) {
            reader.advance();
        }
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        reader.advance();
        reader.mark();
        reader.advance();
        assertEquals('e', reader.current());
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello", reader.consumeTo(','));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hel", reader.consumeToAny(',', 'l'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals(", World!", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("Worl", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("!", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('o'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("Hello"));
        assertFalse(reader.matches("World"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("hello"));
        assertFalse(reader.matchesIgnoreCase("WORLD"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny(',', 'W'));
        assertFalse(reader.matchesAny('Q', 'Z'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("Hello"));
        assertFalse(reader.matchConsume("World"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("hello"));
        assertFalse(reader.matchConsumeIgnoreCase("WORLD"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("world"));
        assertFalse(reader.containsIgnoreCase("java"));
    }

    @Test
    public void testToString() {
        assertEquals(", World!", reader.toString());
    }
}