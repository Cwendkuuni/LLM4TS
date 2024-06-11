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
        CharacterReader reader1 = new CharacterReader("");
        assertTrue(reader1.isEmpty());
        
        CharacterReader reader2 = new CharacterReader("abc");
        assertFalse(reader2.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader1.current());
        
        CharacterReader reader2 = new CharacterReader("abc");
        assertEquals('a', reader2.current());
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
        assertEquals('a', reader.advance());
        assertEquals('b', reader.advance());
        assertEquals('c', reader.advance());
        assertEquals(CharacterReader.EOF, reader.advance());
    }
    
    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        reader.advance();
        assertEquals('c', reader.advance());
        reader.rewindToMark();
        assertEquals('b', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader1 = new CharacterReader("abc");
        assertEquals("a", reader1.consumeTo('b'));
        
        CharacterReader reader2 = new CharacterReader("abc");
        assertEquals("abc", reader2.consumeTo('d'));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeToAny('b','c','d'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertEquals("abc", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0123abcGHI");
        assertEquals("0123abc", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("0123abc");
        assertEquals("0123", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertFalse(reader.matchesIgnoreCase("DEF"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.matchesAny('a', 'd', 'e'));
        assertFalse(reader.matchesAny('b', 'c', 'f'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("abcDEF");
        assertTrue(reader1.matchesLetter());
        
        CharacterReader reader2 = new CharacterReader("123456");
        assertFalse(reader2.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("abcDEF");
        assertFalse(reader1.matchesDigit());
        
        CharacterReader reader2 = new CharacterReader("123456");
        assertTrue(reader2.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.matchConsume("abc"));
        assertFalse(reader.matchConsume("abc"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.matchConsumeIgnoreCase("ABC"));
        assertFalse(reader.matchConsumeIgnoreCase("ABC"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.containsIgnoreCase("AbC"));
        assertFalse(reader.containsIgnoreCase("XYZ"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertEquals("abcDEF", reader.toString());
    }
}