package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setUp() {
        characterReader = new CharacterReader("test input");
    }
    
    @After
    public void tearDown() {
        characterReader = null;
    }
    
    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        
        characterReader.advance();
        
        assertEquals(1, characterReader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        
        characterReader.consumeToEnd();
        
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('t', characterReader.current());
        
        characterReader.advance();
        
        assertEquals('e', characterReader.current());
        
        characterReader.consumeToEnd();
        
        assertEquals(CharacterReader.EOF, characterReader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('t', characterReader.consume());
        
        assertEquals('e', characterReader.consume());
    }
    
    @Test
    public void testUnconsume() {
        characterReader.consume();
        
        characterReader.unconsume();
        
        assertEquals('t', characterReader.current());
    }
    
    @Test
    public void testAdvance() {
        characterReader.advance();
        
        assertEquals('e', characterReader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        
        characterReader.rewindToMark();
        
        assertEquals('t', characterReader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("t", characterReader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("test in", characterReader.consumeTo("pu"));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("test", characterReader.consumeToAny(' ', 'i', 'p'));
        
        characterReader.advance();
        
        assertEquals("input", characterReader.consumeToAny(' ', 'i', 'p'));
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("test input", characterReader.consumeToEnd());
        
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("test", characterReader.consumeLetterSequence());
        
        assertEquals("input", characterReader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("0A2Cdef");
        
        assertEquals("0A2C", characterReader.consumeHexSequence());
        
        assertEquals("def", characterReader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("123test456");
        
        assertEquals("123", characterReader.consumeDigitSequence());
        
        assertEquals("456", characterReader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('t'));
        
        assertFalse(characterReader.matches("e"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("T"));
        
        assertFalse(characterReader.matchesIgnoreCase("E"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('t', ' ', 'i'));
        
        assertFalse(characterReader.matchesAny('e', 'p'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        
        characterReader.advance();
        
        assertTrue(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("te"));
        
        assertTrue(characterReader.matchConsumeIgnoreCase("St"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("est"));
        
        assertFalse(characterReader.containsIgnoreCase("EST"));
    }
    
    @Test
    public void testToString() {
        assertEquals("test input", characterReader.toString());
    }
}