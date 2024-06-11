package org.jsoup.parser;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("This is a test string.");
    }
    
    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        reader.advance();
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
        reader.advance();
        assertEquals('h', reader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        assertEquals('h' ,reader.consume());
        assertEquals('i', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        assertEquals('T', reader.consume());
        reader.unconsume();
        assertEquals('T', reader.current());
    }
    
    @Test
    public void testAdvance() {
        assertEquals('T', reader.current());
        reader.advance();
        assertEquals('h', reader.current());
    }
    
    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        reader.mark();
        assertEquals(0, reader.pos());
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("This is a ", reader.consumeTo('t'));
        assertEquals("test s", reader.consumeTo('r'));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("This ", reader.consumeToAny('i', 's'));
        assertEquals(" is ", reader.consumeToAny('a', 't'));
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string.", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", reader.consumeLetterSequence());
        assertEquals("is", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
        reader.advance(); // Move past the 'T'
        assertEquals("his", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        reader.advance(); // Move past the 'T'
        assertEquals("", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        reader.advance();
        assertFalse(reader.matches('T'));
        assertTrue(reader.matches("his "));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("tHIS"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 'I'));
        reader.advance();
        assertFalse(reader.matchesAny('T', 'I'));
        assertTrue(reader.matchesAny('h', 'i'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("This"));
        assertFalse(reader.matchConsume("test"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("ThIs"));
        assertFalse(reader.matchConsumeIgnoreCase("test"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("test"));
    }
    
    @Test
    public void testToString() {
        assertEquals("This is a test string.", reader.toString());
        reader.advance();
        assertEquals("his is a test string.", reader.toString());
    }
}