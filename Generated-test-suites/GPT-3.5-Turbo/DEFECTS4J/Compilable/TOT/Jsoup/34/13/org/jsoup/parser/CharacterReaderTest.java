package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setUp() {
        characterReader = new CharacterReader("This is a test string.");
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
        assertEquals('h', characterReader.current());
    }
    
    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }
    
    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('h', characterReader.current());
    }
    
    @Test
    public void testMark() {
        characterReader.mark();
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        assertEquals(1, characterReader.pos());
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals('h', characterReader.current());
    }
    
    @Test
    public void testNextIndexOf() {
        assertEquals(3, characterReader.nextIndexOf('s'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }
    
    @Test
    public void testNextIndexOfSeq() {
        assertEquals(2, characterReader.nextIndexOf("is"));
        assertEquals(-1, characterReader.nextIndexOf("not"));
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("This ", characterReader.consumeTo(' '));
        assertEquals('i', characterReader.current());
    }
    
    @Test
    public void testConsumeToSeq() {
        assertEquals("This is", characterReader.consumeTo(" a"));
        assertEquals('a', characterReader.current());
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("This", characterReader.consumeToAny('.', '?'));
        assertEquals(' ', characterReader.current());
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string.", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
        assertEquals(' ', characterReader.current());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", characterReader.consumeLetterThenDigitSequence());
        assertEquals(' ', characterReader.current());
    }
    
    @Test
    public void testConsumeHexSequence() {
        assertEquals("This", characterReader.consumeHexSequence());
        assertEquals(' ', characterReader.current());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("This", characterReader.consumeDigitSequence());
        assertEquals(' ', characterReader.current());
    }
    
    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('t'));
    }
    
    @Test
    public void testMatchesSeq() {
        assertTrue(characterReader.matches("This"));
        assertFalse(characterReader.matches("this"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("THIS"));
        assertTrue(characterReader.matchesIgnoreCase("tHis"));
        assertFalse(characterReader.matchesIgnoreCase("that"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 't'));
        assertFalse(characterReader.matchesAny('A', 'a'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        assertTrue(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("This"));
        assertEquals(' ', characterReader.current());
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("tHis"));
        assertEquals(' ', characterReader.current());
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("this"));
        assertTrue(characterReader.containsIgnoreCase("TEST"));
        assertFalse(characterReader.containsIgnoreCase("that"));
    }
    
    @Test
    public void testToString() {
        assertEquals("This is a test string.", characterReader.toString());
    }
}