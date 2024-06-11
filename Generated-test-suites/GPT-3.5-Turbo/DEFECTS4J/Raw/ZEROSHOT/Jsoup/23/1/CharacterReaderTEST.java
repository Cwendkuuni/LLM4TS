import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader reader;
    
    @Before
    public void setUp() {
        reader = new CharacterReader("test string");
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
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testConsume() {
        char val = reader.consume();
        assertEquals('t', val);
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testUnconsume() {
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
        assertEquals(0, reader.pos());
        assertEquals(0, reader.mark());
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
        String consumed = reader.consumeAsString();
        assertEquals("t", consumed);
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testConsumeTo() {
        String consumed = reader.consumeTo('s');
        assertEquals("test ", consumed);
        assertEquals(5, reader.pos());
    }
    
    @Test
    public void testConsumeToAny() {
        String consumed = reader.consumeToAny('t');
        assertEquals("tes", consumed);
        assertEquals(3, reader.pos());
    }
    
    @Test
    public void testConsumeToEnd() {
        String consumed = reader.consumeToEnd();
        assertEquals("test string", consumed);
        assertEquals(11, reader.pos());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        String consumed = reader.consumeLetterSequence();
        assertEquals("test", consumed);
        assertEquals(4, reader.pos());
    }
    
    @Test
    public void testConsumeHexSequence() {
        String consumed = reader.consumeHexSequence();
        assertEquals("", consumed);
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        String consumed = reader.consumeDigitSequence();
        assertEquals("", consumed);
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testMatches() {
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('s'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("Tes"));
        assertFalse(reader.matchesIgnoreCase("Str"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('t', 's'));
        assertFalse(reader.matchesAny('r', 'g'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        boolean result = reader.matchConsume("tes");
        assertTrue(result);
        assertEquals(3, reader.pos());
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean result = reader.matchConsumeIgnoreCase("Tes");
        assertTrue(result);
        assertEquals(3, reader.pos());
    }
    
    @Test
    public void testContainsIgnoreCase() {
        boolean result = reader.containsIgnoreCase("Str");
        assertTrue(result);
    }
    
    @Test
    public void testToString() {
        String str = reader.toString();
        assertEquals("test string", str);
    }
}