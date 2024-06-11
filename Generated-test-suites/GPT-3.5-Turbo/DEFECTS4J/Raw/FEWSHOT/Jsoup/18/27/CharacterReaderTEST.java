package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader reader;
    private String input;
    
    @Before
    public void setUp() {
        input = "This is a test string.";
        reader = new CharacterReader(input);
    }
    
    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        
        reader.consume();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
        
        reader.consume();
        assertEquals('h', reader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        
        reader.consume();
        assertEquals('i', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.consume());
    }
    
    @Test
    public void testAdvance() {
        assertEquals('T', reader.current());
        
        reader.advance();
        assertEquals('i', reader.current());
    }
    
    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.mark());
    }
    
    @Test
    public void testRewindToMark() {
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
        
        reader.consumeToEnd();
        assertEquals("", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("This ", reader.consumeTo(' '));
        
        reader.consumeToEnd();
        assertEquals("", reader.consumeTo(' '));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("This ", reader.consumeToAny('.', '!'));
        
        reader.consumeToEnd();
        assertEquals("", reader.consumeToAny('.', '!'));
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string.", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", reader.consumeLetterSequence());
        
        reader.consume();
        assertEquals("is", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('h'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("TH"));
        assertFalse(reader.matchesIgnoreCase("th"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 'h'));
        assertFalse(reader.matchesAny('h', 'i'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        
        reader.consume();
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        
        reader.consume();
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("This"));
        assertFalse(reader.matchConsume("is"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("TH"));
        assertFalse(reader.matchConsumeIgnoreCase("th"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertFalse(reader.containsIgnoreCase("test"));
        
        input = "This is a Test string.";
        reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("test"));
    }
    
    @Test
    public void testToString() {
        assertEquals("This is a test string.", reader.toString());
        
        reader.consume();
        assertEquals("his is a test string.", reader.toString());
    }
}