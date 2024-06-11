import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        
        reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.current());
        
        reader = new CharacterReader("test");
        assertEquals('t', reader.current());
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
        assertEquals('t', reader.consume());
        reader.unconsume();
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo('e'));
        assertEquals("est", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("te", reader.consumeToAny('s'));
        assertEquals("st", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("123", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("123", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("Te"));
        assertFalse(reader.matchesIgnoreCase("et"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t', 'e'));
        assertFalse(reader.matchesAny('e', 's'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test1");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("te"));
        assertFalse(reader.matchConsume("st"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("Te"));
        assertFalse(reader.matchConsumeIgnoreCase("eT"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("te"));
        assertFalse(reader.containsIgnoreCase("TE"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}