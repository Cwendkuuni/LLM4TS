import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        assertTrue(reader1.isEmpty());
        
        CharacterReader reader2 = new CharacterReader("test");
        assertFalse(reader2.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader1.current());
        
        CharacterReader reader2 = new CharacterReader("test");
        assertEquals('t', reader2.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('e', reader.consume());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.consume());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.consume());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOf_char() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
    }
    
    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.nextIndexOf("te"));
    }
    
    @Test
    public void testConsumeTo_char() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeTo('X'));
    }
    
    @Test
    public void testConsumeTo_CharSequence() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeTo("X"));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("te", reader.consumeToAny('s', 'X'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test123", reader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123ABC");
        assertEquals("123ABC", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123");
        assertEquals("123", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches_char() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('X'));
    }
    
    @Test
    public void testMatches_CharSequence() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches("te"));
        assertFalse(reader.matches("X"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("TEst");
        assertTrue(reader.matchesIgnoreCase("te"));
        assertFalse(reader.matchesIgnoreCase("X"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('s', 't', 'X'));
        assertFalse(reader.matchesAny('X'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("");
        assertFalse(reader1.matchesLetter());
        
        CharacterReader reader2 = new CharacterReader("test");
        assertTrue(reader2.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("");
        assertFalse(reader1.matchesDigit());
        
        CharacterReader reader2 = new CharacterReader("123");
        assertTrue(reader2.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("test"));
        assertFalse(reader.matchConsume("X"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("TEst");
        assertTrue(reader.matchConsumeIgnoreCase("test"));
        assertFalse(reader.matchConsumeIgnoreCase("X"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("TE"));
        assertFalse(reader.containsIgnoreCase("X"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
    
}