import org.junit.*;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos());
        
        reader.consume();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.isEmpty());
        
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('a', reader.current());
        
        reader.consume();
        assertEquals('b', reader.current());
    }
    
    @Test
    public void testConsume() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('a', reader.consume());
        
        assertEquals('b', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }
    
    @Test
    public void testAdvance() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        assertEquals('b', reader.current());
    }
    
    @Test
    public void testMark() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        reader.mark();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('b', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("a", reader.consumeAsString());
        
        assertEquals("b", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("ab", reader.consumeTo('c'));
        
        assertEquals("c", reader.consumeTo('d'));
    }
    
    @Test
    public void testConsumeToAny() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("ab", reader.consumeToAny('c', 'd'));
        
        assertEquals("", reader.consumeToAny('d'));
    }
    
    @Test
    public void testConsumeToEnd() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abc", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abc", reader.consumeLetterSequence());
        
        input = "123";
        reader = new CharacterReader(input);
        assertEquals("", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abc", reader.consumeHexSequence());
        
        input = "123";
        reader = new CharacterReader(input);
        assertEquals("123", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        String input = "123";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("123", reader.consumeDigitSequence());
        
        input = "abc";
        reader = new CharacterReader(input);
        assertEquals("", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('a'));
        
        reader.advance();
        assertFalse(reader.matches('a'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("aBc"));
        
        input = "123";
        reader = new CharacterReader(input);
        assertFalse(reader.matchesIgnoreCase("abc"));
    }
    
    @Test
    public void testMatchesAny() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        
        input = "123";
        reader = new CharacterReader(input);
        assertFalse(reader.matchesAny('a', 'b', 'c'));
    }
    
    @Test
    public void testMatchesLetter() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesLetter());
        
        input = "123";
        reader = new CharacterReader(input);
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        String input = "123";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesDigit());
        
        input = "abc";
        reader = new CharacterReader(input);
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsume("ab"));
        assertEquals('c', reader.current());
        
        assertFalse(reader.matchConsume("cd"));
        assertEquals('c', reader.current());
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsumeIgnoreCase("aBc"));
        assertEquals((char) -1, reader.current());
        
        assertFalse(reader.matchConsumeIgnoreCase("aBc"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("AB"));
        
        assertFalse(reader.containsIgnoreCase("CD"));
    }
    
    @Test
    public void testToString() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abc", reader.toString());
        
        reader.consume();
        assertEquals("bc", reader.toString());
    }
}