import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        String input = "This is a test";
        reader = new CharacterReader(input);
    }

    @Test
    public void testPos() {
        // Typical use case
        assertEquals(0, reader.pos());

        // Edge case
        reader.unconsume();
        assertEquals(-1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Typical use case
        assertFalse(reader.isEmpty());

        // Edge case
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Typical use case
        assertEquals('T', reader.current());

        // Edge case
        reader.consumeToEnd();
        assertEquals((char) -1, reader.current());
    }

    @Test
    public void testConsume() {
        // Typical use case
        assertEquals('T', reader.consume());

        // Edge case
        reader.consumeToEnd();
        assertEquals((char) -1, reader.consume());
    }

    @Test
    public void testUnconsume() {
        // Typical use case
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());

        // Edge case
        reader.consomeToEnd();
        reader.unconsume();
        assertEquals((char) -1, reader.current());
    }

    @Test
    public void testAdvance() {
        // Typical use case
        reader.advance();
        assertEquals('h', reader.current());

        // Edge case
        reader.consumeToEnd();
        reader.advance();
        assertEquals((char) -1, reader.current());
    }

    @Test
    public void testMark() {
        // Typical use case
        reader.advance(); // move to 'h'
        reader.mark();
        assertEquals('h', reader.current());

        // Edge case
        reader.consumeToEnd();
        reader.mark();
        assertEquals((char) -1, reader.current());
    }

    @Test
    public void testRewindToMark() {
        // Typical use case
        reader.advance(); // move to 'h'
        reader.mark();
        reader.advance(); // move to 'i'
        reader.rewindToMark();
        assertEquals('h', reader.current());

        // Edge case
        reader.consumeToEnd();
        reader.rewindToMark();
        assertEquals((char) -1, reader.current());
    }

    @Test
    public void testConsumeAsString() {
        // Typical use case
        assertEquals("Th", reader.consumeAsString());

        // Edge case
        reader.consumeToEnd();
        assertEquals("", reader.consumeAsString());
    }
  
    @Test
    public void testConsumeTo() {
        // Typical use case
        assertEquals("is ", reader.consumeTo(' '));

        // Edge case
        reader.consumeToEnd();
        assertEquals("", reader.consumeTo(' '));
    }
  
    @Test
    public void testConsumeToAny() {
        // Typical use case
        assertEquals("Th", reader.consumeToAny('i', 's'));

        // Edge case
        reader.consumeToEnd();
        assertEquals("", reader.consumeToAny('i', 's'));
    }
  
    @Test
    public void testConsumeToEnd() {
        // Typical use case
        assertEquals("This is a test", reader.consumeToEnd());

        // Edge case
        reader.consumeToEnd();
        assertEquals("", reader.consumeToEnd());
    }
  
    @Test
    public void testConsumeLetterSequence() {
        // Typical use case
        assertEquals("Th", reader.consumeLetterSequence());

        // Edge case
        reader.consumeToEnd();
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        // Typical use case
        assertEquals("", reader.consumeHexSequence());

        // Edge case
        reader.consumeToEnd();
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        // Typical use case
        assertEquals("", reader.consumeDigitSequence());

        // Edge case
        reader.consumeToEnd();
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        // Typical use case
        assertTrue(reader.matches('T'));

        // Edge case
        reader.consumeToEnd();
        assertFalse(reader.matches('T'));
    }
  
    @Test
    public void testMatchesAny() {
        // Typical use case
        assertTrue(reader.matchesAny('h', 'T'));

        // Edge case
        reader.consumeToEnd();
        assertFalse(reader.matchesAny('h', 'T'));
    }
  
    @Test
    public void testMatchesLetter() {
        // Typical use case
        assertTrue(reader.matchesLetter());

        // Edge case
        reader.consumeToEnd();
        assertFalse(reader.matchesLetter());
    }
  
    @Test
    public void testMatchesDigit() {
        // Typical use case
        assertFalse(reader.matchesDigit());

        // Edge case
        reader.consumeToEnd();
        assertFalse(reader.matchesDigit());
    }
  
    @Test
    public void testMatchConsume() {
        // Typical use case
        assertTrue(reader.matchConsume("Th"));

        // Edge case
        reader.consumeToEnd();
        assertFalse(reader.matchConsume("Th"));
    }
  
    @Test
    public void testMatchConsumeIgnoreCase() {
        // Typical use case
        assertTrue(reader.matchConsumeIgnoreCase("this"));

        // Edge case
        reader.consumeToEnd();
        assertFalse(reader.matchConsumeIgnoreCase("this"));
    }
  
    @Test
    public void testContainsIgnoreCase() {
        // Typical use case
        assertTrue(reader.containsIgnoreCase("Th"));

        // Edge case
        reader.consumeToEnd();
        assertFalse(reader.containsIgnoreCase("Th"));
    }
  
    @Test
    public void testToString() {
        // Typical use case
        assertEquals("This is a test", reader.toString());

        // Edge case
        reader.consumeToEnd();
        assertEquals("", reader.toString());
    }
}