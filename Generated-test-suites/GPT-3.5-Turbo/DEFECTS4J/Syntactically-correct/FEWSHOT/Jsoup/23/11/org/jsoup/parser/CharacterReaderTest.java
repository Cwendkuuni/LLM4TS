package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CharacterReaderTest {
  
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test string");
        assertEquals(0, reader.pos());
        
        reader.consume();
        assertEquals(1, reader.pos());
        
        reader.advance();
        assertEquals(2, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        
        reader = new CharacterReader("Test");
        assertFalse(reader.isEmpty());
        
        reader.consume();
        assertFalse(reader.isEmpty());
        
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        
        assertEquals('T', reader.current());
        
        reader.consume();
        assertEquals('e', reader.current());
        
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test");
        
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
        
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test");
        
        reader.consume();
        reader.consumeToEnd();
        
        reader.unconsume();
        assertEquals('e', reader.consume());
        
        reader.unconsume();
        assertEquals('T', reader.consume());
    }
    
    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("Test");
        
        reader.mark();
        
        reader.consume();
        reader.consumeToEnd();
        
        reader.rewindToMark();
        assertEquals('T', reader.consume());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Test");
        
        assertEquals("T", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        
        reader.consumeToEnd();
        assertEquals("", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Test string");
        
        assertEquals("Test", reader.consumeTo(' '));
        assertEquals(" string", reader.consumeTo(CharacterReader.EOF));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Test string");
        
        assertEquals("Te", reader.consumeToAny('s', 't'));
        assertEquals("st", reader.consumeToAny('s', 't'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Test string");
        
        assertEquals("Test string", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Test string");
        
        assertEquals("Test", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("Test 123");
        
        assertEquals("123", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("Test 123");
        
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Test");
        
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('e'));
        
        reader.consume();
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('T'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        
        assertTrue(reader.matchesIgnoreCase("TeSt"));
        assertFalse(reader.matchesIgnoreCase("TEST"));
        
        reader.consume();
        assertTrue(reader.matchesIgnoreCase("est"));
        assertFalse(reader.matchesIgnoreCase("Test"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Test");
        
        assertTrue(reader.matchesAny('T', 'e'));
        assertFalse(reader.matchesAny('t', 'E'));
        
        reader.consume();
        assertTrue(reader.matchesAny('e', 's'));
        assertFalse(reader.matchesAny('T', 's'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Test");
        
        assertTrue(reader.matchesLetter());
        
        reader.consume();
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Test");
        
        assertFalse(reader.matchesDigit());
        
        reader.consume();
        assertFalse(reader.matchesDigit());
        
        reader.consumeToEnd();
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Test");
        
        assertTrue(reader.matchConsume("T"));
        assertFalse(reader.matchConsume("t"));
        
        assertTrue(reader.matchConsume("est"));
        assertFalse(reader.matchConsume("Test"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        
        assertTrue(reader.matchConsumeIgnoreCase("TeSt"));
        assertFalse(reader.matchConsumeIgnoreCase("TEST"));
        
        assertTrue(reader.matchConsumeIgnoreCase("est"));
        assertFalse(reader.matchConsumeIgnoreCase("Test"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test string");
        
        assertTrue(reader.containsIgnoreCase("TEST"));
        assertTrue(reader.containsIgnoreCase("String"));
        assertFalse(reader.containsIgnoreCase("foo"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Test");
        
        assertEquals("Test", reader.toString());
        
        reader.consumeToEnd();
        assertEquals("", reader.toString());
    }
}