package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("This is a test string");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
    }
    
    @Test
    public void testUnconsume() {
        assertEquals('T', characterReader.consume());
        characterReader.unconsume();
        assertEquals('T', characterReader.consume());
    }
    
    @Test
    public void testAdvance() {
        assertEquals('T', characterReader.current());
        characterReader.advance();
        assertEquals('h', characterReader.current());
    }
    
    @Test
    public void testMark() {
        characterReader.mark();
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        assertEquals('h', characterReader.current());
        
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals("h", characterReader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("This is a", characterReader.consumeTo(' '));
        assertEquals("test", characterReader.consumeTo(' '));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("This", characterReader.consumeToAny(' ', 'i'));
        assertEquals(" ", characterReader.consumeToAny(' ', 'i'));
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string", characterReader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
        assertEquals("is", characterReader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('h'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("t"));
        assertFalse(characterReader.matchesIgnoreCase("h"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'h'));
        assertFalse(characterReader.matchesAny('a', 'b'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("This "));
        assertFalse(characterReader.matchConsume("is"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("this "));
        assertFalse(characterReader.matchConsumeIgnoreCase("is"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("IS a"));
        assertFalse(characterReader.containsIgnoreCase("is"));
    }
    
    @Test
    public void testToString() {
        assertEquals("This is a test string", characterReader.toString());
    }
}