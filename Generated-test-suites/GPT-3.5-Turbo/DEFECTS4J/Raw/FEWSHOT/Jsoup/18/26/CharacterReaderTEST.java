package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals(0, reader.pos());
        
        reader.consume();
        
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertFalse(reader.isEmpty());
        
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals('t', reader.current());
        
        reader.consume();
        
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testConsume() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals('t', reader.consume());
        
        assertEquals('e', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        reader.consume();
        
        reader.unconsume();
        
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testAdvance() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals('t', reader.current());
        
        reader.advance();
        
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testMark() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        reader.mark();
        
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        reader.mark();
        
        reader.consume();
        reader.consume();
        reader.consume();
        
        reader.rewindToMark();
        
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("t", reader.consumeAsString());
        
        assertEquals("e", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("", reader.consumeTo('t'));
        
        assertEquals("es", reader.consumeTo('t'));
    }
    
    @Test
    public void testConsumeToAny() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("", reader.consumeToAny('t', 'e'));
        
        assertEquals("s", reader.consumeToAny('t', 'e'));
    }
    
    @Test
    public void testConsumeToEnd() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("test", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        String input = "test123";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("test", reader.consumeLetterSequence());
        
        assertEquals("123", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        String input = "test123";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("123", reader.consumeDigitSequence());
        
        assertEquals("", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matches('t'));
        
        reader.consume();
        
        assertFalse(reader.matches('t'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchesIgnoreCase("TeS"));
        
        reader.consume();
        
        assertFalse(reader.matchesIgnoreCase("TeS"));
    }
    
    @Test
    public void testMatchesAny() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchesAny('t', 'e'));
        
        reader.consume();
        
        assertFalse(reader.matchesAny('t', 'e'));
    }
    
    @Test
    public void testMatchesLetter() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchesLetter());
        
        reader.consume();
        
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        String input = "test123";
        CharacterReader reader = new CharacterReader(input);
        
        assertFalse(reader.matchesDigit());
        
        reader.consume();
        reader.consume();
        
        assertTrue(reader.matchesDigit());
        
        reader.consume();
        
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchConsume("te"));
        assertFalse(reader.matchConsume("te"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchConsumeIgnoreCase("TE"));
        assertFalse(reader.matchConsumeIgnoreCase("TE"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertFalse(reader.containsIgnoreCase("TE"));
        
        assertTrue(reader.containsIgnoreCase("te"));
    }
    
    @Test
    public void testToString() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("test", reader.toString());
        
        reader.consume();
        
        assertEquals("est", reader.toString());
    }
}