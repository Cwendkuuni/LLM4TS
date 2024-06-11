package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals('H', reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('o', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.advance();
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.advance();
        reader.mark();
        reader.advance();
        assertEquals('l', reader.current());
    }
    
    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("H", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("He", reader.consumeTo('l'));
    }
    
    @Test
    public void testConsumeToEmpty() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("Hello", reader.consumeTo('z'));
    }
    
    @Test
    public void testConsumeToSeq() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello", reader.consumeTo(" "));
    }
    
    @Test
    public void testConsumeToSeqEmpty() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.consumeTo("z"));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello ", reader.consumeToAny('W', 'o'));
    }
    
    @Test
    public void testConsumeToAnyEmpty() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("", reader.consumeToAny('z'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello123");
        assertEquals("Hello", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("A1B2C3");
        assertEquals("A1B2C3", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123ABC");
        assertEquals("123", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('e'));
    }
    
    @Test
    public void testMatchesSeq() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matches("Hello"));
        assertFalse(reader.matches("World"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesIgnoreCase("hello"));
        assertFalse(reader.matchesIgnoreCase("World"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesAny('H', 'e'));
        assertFalse(reader.matchesAny('z'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Hello");
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchConsume("Hello"));
        assertFalse(reader.matchConsume("World"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchConsumeIgnoreCase("hello"));
        assertFalse(reader.matchConsumeIgnoreCase("World"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.containsIgnoreCase("lo"));
        assertFalse(reader.containsIgnoreCase("World"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("Hello", reader.toString());
        reader.consume();
        assertEquals("ello", reader.toString());
    }
}