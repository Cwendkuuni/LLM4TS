package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader reader;
    
    @Before
    public void setup() {
        String input = "This is a test input.";
        reader = new CharacterReader(input);
    }
    
    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        assertEquals('h', reader.consume());
        assertEquals('i', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('h', reader.current());
    }
    
    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('h', reader.current());
    }
    
    @Test
    public void testMarkAndRewind() {
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('h', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOf() {
        assertEquals(2, reader.nextIndexOf('i'));
    }
    
    @Test
    public void testNextIndexOfWithNonExistingChar() {
        assertEquals(-1, reader.nextIndexOf('z'));
    }
    
    @Test
    public void testNextIndexOfWithSeq() {
        assertEquals(2, reader.nextIndexOf("is"));
    }
    
    @Test
    public void testNextIndexOfWithNonExistingSeq() {
        assertEquals(-1, reader.nextIndexOf("example"));
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("Th", reader.consumeTo('i'));
    }
    
    @Test
    public void testConsumeToWithNonExistingChar() {
        assertEquals("This is a test input.", reader.consumeTo('z'));
    }
    
    @Test
    public void testConsumeToWithSeq() {
        assertEquals("Th", reader.consumeTo("is"));
    }
    
    @Test
    public void testConsumeToWithNonExistingSeq() {
        assertEquals("This is a test input.", reader.consumeTo("example"));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("Th", reader.consumeToAny('i', 's'));
    }
    
    @Test
    public void testConsumeToAnyWithNonExistingChars() {
        assertEquals("This is a test input.", reader.consumeToAny('z', 'x', 'y'));
    }
    
    @Test
    public void testConsumeToAnySorted() {
        assertEquals("This ", reader.consumeToAnySorted(' ', 'T'));
    }
    
    @Test
    public void testConsumeToAnySortedWithNonExistingChars() {
        assertEquals("This is a test input.", reader.consumeToAnySorted('z', 'x', 'y'));
    }
    
    @Test
    public void testConsumeData() {
        assertEquals("This is a test ", reader.consumeData());
    }
    
    @Test
    public void testConsumeTagName() {
        assertEquals("This", reader.consumeTagName());
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test input.", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This1", reader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        assertEquals("This", reader.consumeHexSequence());
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
    public void testMatchesWithSeq() {
        assertTrue(reader.matches("Th"));
        assertFalse(reader.matches("is"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("tH"));
        assertFalse(reader.matchesIgnoreCase("is"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 'h', 'i'));
        assertFalse(reader.matchesAny('a', 'b'));
    }
    
    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted('T', 'h', 'i'));
        assertFalse(reader.matchesAnySorted('a', 'b'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("Th"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("tH"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("tHiS"));
    }
    
    @Test
    public void testToString() {
        assertEquals("This is a test input.", reader.toString());
    }

    @Test
    public void testCacheString() {
        String cacheString = reader.cacheString(0, 4);
        assertEquals("This", cacheString);
    }

    @Test
    public void testRangeEquals() {
        boolean rangeEquals = reader.rangeEquals(0, 4, "This");
        assertTrue(rangeEquals);
    }
}