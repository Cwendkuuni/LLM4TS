import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Test String");
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
        for (int i = 0; i < 11; i++) {
            reader.consume();
        }
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
        assertEquals('e', reader.consume());
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
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test ", reader.consumeTo(' '));
        assertEquals("String", reader.consumeTo(' '));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals(" ", reader.consumeToIgnoreCase("t"));
        assertEquals("T", reader.consumeToIgnoreCase("e"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("T", reader.consumeToAny('e', 's'));
        assertEquals("st ", reader.consumeToAny('r'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test String", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", reader.consumeLetterSequence());
        assertEquals("String", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
        reader.consume();  // Consume 'e'
        assertEquals("st", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        reader.consume();  // Consume 'e'
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        reader.consume();
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('T'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("Test"));
        reader.consume();  // Consume 'T'
        assertTrue(reader.matches("est"));
        assertFalse(reader.matches("Test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("TEST"));
        reader.consume();  // Consume 'T'
        assertTrue(reader.matchesIgnoreCase("est"));
        assertFalse(reader.matchesIgnoreCase("Test"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 'e'));
        reader.consume();  // Consume 'T'
        assertFalse(reader.matchesAny('T', 'e'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.consume();  // Consume 'T'
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.consume();  // Consume 'T'
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertFalse(reader.matchConsume("Test"));
        assertTrue(reader.matchConsume("est"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertFalse(reader.matchConsumeIgnoreCase("TEST"));
        assertTrue(reader.matchConsumeIgnoreCase("est"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(reader.containsIgnoreCase("TEST"));
        assertTrue(reader.containsIgnoreCase("est"));
    }

    @Test
    public void testToString() {
        assertEquals("Test String", reader.toString());
    }
}