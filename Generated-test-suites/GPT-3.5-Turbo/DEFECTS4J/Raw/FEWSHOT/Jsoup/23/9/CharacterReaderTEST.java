import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        assertTrue(reader1.isEmpty());
        
        CharacterReader reader2 = new CharacterReader("not empty");
        assertFalse(reader2.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals((char) -1, reader1.current());
        
        CharacterReader reader2 = new CharacterReader("input");
        assertEquals('i', reader2.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals('i', reader.consume());
        assertEquals('n', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("input");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('n', reader.current());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("input");
        reader.advance();
        assertEquals('n', reader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("input");
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('i', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("i", reader.consumeAsString());
        assertEquals("n", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader1 = new CharacterReader("input");
        assertEquals("input", reader1.consumeTo('x'));
        
        CharacterReader reader2 = new CharacterReader("input");
        assertEquals("in", reader2.consumeTo('p'));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader1 = new CharacterReader("input");
        assertEquals("in", reader1.consumeToAny('p', 'u'));
        
        CharacterReader reader2 = new CharacterReader("input");
        assertEquals("", reader2.consumeToAny());
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("nput", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("in", reader.consumeLetterSequence());
        assertEquals("put", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        assertEquals("ABC", reader.consumeHexSequence());
        assertEquals("123", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        assertEquals("", reader.consumeDigitSequence());
        
        CharacterReader reader2 = new CharacterReader("123ABC");
        assertEquals("123", reader2.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader1 = new CharacterReader("input");
        assertTrue(reader1.matches('i'));
        
        CharacterReader reader2 = new CharacterReader("input");
        assertFalse(reader2.matches('x'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("input");
        assertTrue(reader1.matchesIgnoreCase("INPUT"));
        
        CharacterReader reader2 = new CharacterReader("input");
        assertFalse(reader2.matchesIgnoreCase("OUTPUT"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader1 = new CharacterReader("input");
        assertTrue(reader1.matchesAny('i', 'x'));
        
        CharacterReader reader2 = new CharacterReader("input");
        assertFalse(reader2.matchesAny('x', 'y'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("A");
        assertTrue(reader1.matchesLetter());
        
        CharacterReader reader2 = new CharacterReader("1");
        assertFalse(reader2.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("1");
        assertTrue(reader1.matchesDigit());
        
        CharacterReader reader2 = new CharacterReader("i");
        assertFalse(reader2.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader1 = new CharacterReader("input");
        assertTrue(reader1.matchConsume("input"));
        assertTrue(reader1.isEmpty());
        
        CharacterReader reader2 = new CharacterReader("input");
        assertFalse(reader2.matchConsume("output"));
        assertFalse(reader2.isEmpty());
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("input");
        assertTrue(reader1.matchConsumeIgnoreCase("INPUT"));
        assertTrue(reader1.isEmpty());
        
        CharacterReader reader2 = new CharacterReader("input");
        assertFalse(reader2.matchConsumeIgnoreCase("OUTPUT"));
        assertFalse(reader2.isEmpty());
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("input");
        assertTrue(reader1.containsIgnoreCase("IN"));
        
        CharacterReader reader2 = new CharacterReader("input");
        assertFalse(reader2.containsIgnoreCase("OUT"));
    }
}