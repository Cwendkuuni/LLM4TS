
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        reader.unconsume();
        assertEquals('a', reader.consume());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        reader.advance();
        assertEquals('c', reader.consume());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        reader.mark();
        assertEquals('b', reader.consume());
        reader.rewindToMark();
        assertEquals('b', reader.consume());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("ab", reader.consumeTo('c'));
        assertEquals("c", reader.consumeTo('d'));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeToAny('b'));
        assertEquals("b", reader.consumeToAny('c'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123");
        assertEquals("123", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertFalse(reader.matchesIgnoreCase("BCD"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('b', 'c'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("ab"));
        assertFalse(reader.matchConsume("bc"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsumeIgnoreCase("AB"));
        assertFalse(reader.matchConsumeIgnoreCase("BC"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.containsIgnoreCase("AB"));
        assertFalse(reader.containsIgnoreCase("DE"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        assertEquals("bc", reader.toString());
    }
}
