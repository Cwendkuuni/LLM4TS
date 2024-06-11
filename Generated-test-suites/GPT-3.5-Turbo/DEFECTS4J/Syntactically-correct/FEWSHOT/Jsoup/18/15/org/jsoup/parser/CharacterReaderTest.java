package org.jsoup.parser;

import org.jsoup.helper.Validate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
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
        
        reader.advance();
        assertTrue(reader.isEmpty());
        
        CharacterReader nonEmptyReader = new CharacterReader("test");
        assertFalse(nonEmptyReader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
        
        reader.consume();
        assertEquals('e', reader.current());
        
        reader.advance();
        assertEquals('s', reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.consume();
        reader.consume();
        
        reader.unconsume();
        assertEquals('s', reader.current());
        
        reader.unconsume();
        assertEquals('e', reader.current());
        
        reader.unconsume();
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.consume();
        
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        
        reader.advance();
        assertEquals("e", reader.consumeAsString());
        
        reader.advance();
        assertEquals("s", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        
        assertEquals("Hello", reader.consumeTo(','));
        
        reader.advance();
        assertEquals(" World!", reader.consumeTo(','));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        
        assertEquals("Hello", reader.consumeToAny(',', '!'));
        
        reader.advance();
        assertEquals(" World", reader.consumeToAny(',', '!'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        
        assertEquals("Hello, World!", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello123World");
        
        assertEquals("Hello", reader.consumeLetterSequence());
        
        reader.advance();
        assertEquals("World", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        
        assertEquals("ABC", reader.consumeHexSequence());
        
        reader.advance();
        assertEquals("123", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345ABC");
        
        assertEquals("12345", reader.consumeDigitSequence());
        
        reader.advance();
        assertEquals("ABC", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        
        assertTrue(reader.matches('t'));
        
        reader.consume();
        assertFalse(reader.matches('t'));
        
        assertTrue(reader.matches("est"));
        
        reader.consume();
        assertTrue(reader.matches("est"));
        
        assertFalse(reader.matches("estimate"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        
        assertTrue(reader.matchesIgnoreCase("test"));
        
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("test"));
        
        assertTrue(reader.matchesIgnoreCase("est"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        
        assertTrue(reader.matchesAny('a', 'e', 't'));
        
        reader.consume();
        assertTrue(reader.matchesAny('t'));
        
        assertFalse(reader.matchesAny(';'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        
        assertTrue(reader.matchesLetter());
        
        reader.consume();
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123test");
        
        assertTrue(reader.matchesDigit());
        
        reader.consume();
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        
        assertTrue(reader.matchConsume("te"));
        
        assertFalse(reader.matchConsume("te"));
        
        assertTrue(reader.matchConsumeIgnoreCase("sT"));
        
        assertFalse(reader.matchConsumeIgnoreCase("st"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        
        assertTrue(reader.containsIgnoreCase("hello"));
        
        assertFalse(reader.containsIgnoreCase("bye"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        
        assertEquals("Hello, World!", reader.toString());
        
        reader.consume();
        assertEquals("ello, World!", reader.toString());
    }
}