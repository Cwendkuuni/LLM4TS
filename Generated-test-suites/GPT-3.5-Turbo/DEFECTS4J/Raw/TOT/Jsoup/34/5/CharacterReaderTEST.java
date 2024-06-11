import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class CharacterReaderTest {
    
    private CharacterReader reader;
    
    @Before
    public void setUp() {
        reader = new CharacterReader("This is a test string.");
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
        while (!reader.isEmpty()) {
            reader.consume();
        }
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
        reader.advance();
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
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
        assertEquals("h", reader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOf() {
        assertEquals(1, reader.nextIndexOf("h"));
        assertEquals(-1, reader.nextIndexOf("x"));
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("This is a test ", reader.consumeTo("string."));
        assertEquals("string.", reader.consumeTo("X"));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("This is a ", reader.consumeToAny('t', 's'));
        assertEquals("test ", reader.consumeToAny('.'));
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string.", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", reader.consumeLetterSequence());
        assertEquals("is", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", reader.consumeLetterThenDigitSequence());
        assertEquals("is", reader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
        reader.advance();
        assertEquals("", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        reader.advance();
        assertEquals("", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('t'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("T"));
        assertTrue(reader.matchesIgnoreCase("t"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 't'));
        assertFalse(reader.matchesAny('A', 'a'));
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
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("This"));
        assertFalse(reader.matchConsume("is"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("this"));
        assertFalse(reader.matchConsumeIgnoreCase("IS"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("THIS"));
        assertFalse(reader.containsIgnoreCase("that"));
    }
    
    @Test
    public void testToString() {
        assertEquals("This is a test string.", reader.toString());
    }
}