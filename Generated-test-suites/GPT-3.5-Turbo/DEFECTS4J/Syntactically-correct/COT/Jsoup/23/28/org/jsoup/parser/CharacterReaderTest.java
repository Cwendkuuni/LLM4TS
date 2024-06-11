
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("example");
        assertFalse(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals('e', reader.consume());
        assertEquals('x', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("example");
        reader.consume();
        reader.unconsume();
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("example");
        reader.advance();
        assertEquals('x', reader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("example");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("e", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("exam", reader.consumeTo('p'));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("ex", reader.consumeToAny('a', 'm'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("example", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("example", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcd1234");
        assertEquals("abcd1234", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234abcd");
        assertEquals("1234", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('x'));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.matchesAny('e', 'x'));
        assertFalse(reader.matchesAny('a', 'b'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("example");
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
        reader.consumeDigitSequence();
        assertTrue(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.matchConsume("ex"));
        assertFalse(reader.matchConsume("amp"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.matchConsumeIgnoreCase("Ex"));
        assertFalse(reader.matchConsumeIgnoreCase("amp"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.containsIgnoreCase("exa"));
        assertFalse(reader.containsIgnoreCase("amp"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("example", reader.toString());
    }
}
