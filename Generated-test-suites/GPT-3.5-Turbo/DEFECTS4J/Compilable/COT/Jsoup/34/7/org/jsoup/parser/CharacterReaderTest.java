package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

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
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals('i', reader.current());
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
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("input");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('i', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("i", reader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals(1, reader.nextIndexOf('n'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }
    
    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals(1, reader.nextIndexOf("npu"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("in", reader.consumeTo('p'));
        assertEquals("ut", reader.consumeTo('x'));
    }
    
    @Test
    public void testConsumeToWithSequence() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("in", reader.consumeTo("pu"));
        assertEquals("ut", reader.consumeTo("xyz"));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.consumeToAny('x','y','z'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("input123");
        assertEquals("input123", reader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("inputAB12");
        assertEquals("inputAB12", reader.consumeHexSequence());
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
        assertFalse(reader.matches('x'));
    }
    
    @Test
    public void testMatchesWithSequence() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matches("inpu"));
        assertFalse(reader.matches("xyz"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesIgnoreCase("inPu"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesAny('i','u'));
        assertFalse(reader.matchesAny('x','y'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchConsume("in"));
        assertFalse(reader.matchConsume("xyz"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchConsumeIgnoreCase("InPu"));
        assertFalse(reader.matchConsumeIgnoreCase("xyz"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.containsIgnoreCase("inpu"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.toString());
    }
}

