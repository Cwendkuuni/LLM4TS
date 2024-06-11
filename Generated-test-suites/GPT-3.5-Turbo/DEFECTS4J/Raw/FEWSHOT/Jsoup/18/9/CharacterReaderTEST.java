package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        
        reader = new CharacterReader("input");
        assertFalse(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals('i', reader.current());
        
        reader.consume();
        assertEquals('n', reader.current());
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
        assertEquals('i', reader.current());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("input");
        reader.advance();
        assertEquals('n', reader.current());
    }
    
    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("input");
        reader.mark();
        reader.consume();
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
        CharacterReader reader = new CharacterReader("input");
        assertEquals("i", reader.consumeTo('n'));
        assertEquals("n", reader.consumeTo('p'));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("i", reader.consumeToAny('n'));
        assertEquals("", reader.consumeToAny('n'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("input123");
        assertEquals("input", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("input123");
        assertEquals("", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("input123");
        assertEquals("123", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matches('i'));
        assertFalse(reader.matches('n'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesIgnoreCase("INPUT"));
        assertFalse(reader.matchesIgnoreCase("output"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesAny('i', 't'));
        assertFalse(reader.matchesAny('n', 't'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("input123");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchConsume("i"));
        assertFalse(reader.matchConsume("n"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchConsumeIgnoreCase("INPUT"));
        assertFalse(reader.matchConsumeIgnoreCase("output"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.containsIgnoreCase("Output"));
        assertTrue(reader.containsIgnoreCase("ut"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("input");
        reader.consume();
        assertEquals("nput", reader.toString());
    }
}