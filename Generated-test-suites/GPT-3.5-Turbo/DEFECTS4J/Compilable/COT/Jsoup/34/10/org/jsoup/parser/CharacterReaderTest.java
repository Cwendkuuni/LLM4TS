package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        reader.unconsume();
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOf_char() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(2, reader.nextIndexOf('s'));
    }
    
    @Test
    public void testNextIndexOf_char_notFound() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(-1, reader.nextIndexOf('a'));
    }
    
    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(2, reader.nextIndexOf("st"));
    }
    
    @Test
    public void testNextIndexOf_CharSequence_notFound() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(-1, reader.nextIndexOf("abc"));
    }
    
    @Test
    public void testConsumeTo_char() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("te", reader.consumeTo('s'));
    }
    
    @Test
    public void testConsumeTo_char_notFound() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeTo('a'));
    }
    
    @Test
    public void testConsumeTo_String() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("te", reader.consumeTo("st"));
    }
    
    @Test
    public void testConsumeTo_String_notFound() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeTo("abc"));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("te", reader.consumeToAny('s', 'a'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("word123");
        assertEquals("word", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("w1");
        assertEquals("w1", reader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1FB");
        assertEquals("1FB", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123");
        assertEquals("123", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches_char() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
    }
    
    @Test
    public void testMatches_String() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches("test"));
    }
    
    @Test
    public void testMatchesIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("tEsT"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('a', 'e', 'i', 'o', 'u'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("word123");
        assertTrue(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("word123");
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume_String() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("te"));
        assertEquals('s', reader.current());
    }
    
    @Test
    public void testMatchConsumeIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("Te"));
        assertEquals('s', reader.current());
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("ES"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}


// All test methods go here ...

