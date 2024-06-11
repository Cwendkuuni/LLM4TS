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
        CharacterReader reader1 = new CharacterReader("");
        assertTrue(reader1.isEmpty());
        
        CharacterReader reader2 = new CharacterReader("test");
        assertFalse(reader2.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader1.current());
        
        CharacterReader reader2 = new CharacterReader("test");
        assertEquals('t', reader2.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
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
        reader.advance();
        reader.mark();
        reader.advance();
        assertEquals('s', reader.current());
    }
    
    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
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
        assertEquals("e", reader.consumeAsString());
        assertEquals("s", reader.consumeAsString());
        assertEquals("t", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader1 = new CharacterReader("test");
        assertEquals("t", reader1.consumeTo('e'));
        
        CharacterReader reader2 = new CharacterReader("test");
        assertEquals("te", reader2.consumeTo('s'));
        
        CharacterReader reader3 = new CharacterReader("test");
        assertEquals("test", reader3.consumeTo(CharacterReader.EOF));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader1 = new CharacterReader("test");
        assertEquals("t", reader1.consumeToAny('e','s'));
        
        CharacterReader reader2 = new CharacterReader("test");
        assertEquals("tes", reader2.consumeToAny(CharacterReader.EOF));
        
        CharacterReader reader3 = new CharacterReader("test");
        assertEquals("t", reader3.consumeToAny('e','f', 'g'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader1 = new CharacterReader("test");
        assertEquals("test", reader1.consumeToEnd());
        
        CharacterReader reader2 = new CharacterReader("");
        assertEquals("", reader2.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader1 = new CharacterReader("test123");
        assertEquals("test", reader1.consumeLetterSequence());
        
        CharacterReader reader2 = new CharacterReader("123test");
        assertEquals("", reader2.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader1 = new CharacterReader("0ABCD");
        assertEquals("0ABC", reader1.consumeHexSequence());
        
        CharacterReader reader2 = new CharacterReader("test");
        assertEquals("", reader2.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader1 = new CharacterReader("123test");
        assertEquals("123", reader1.consumeDigitSequence());
        
        CharacterReader reader2 = new CharacterReader("test");
        assertEquals("", reader2.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader1 = new CharacterReader("test");
        assertTrue(reader1.matches('t'));
        assertFalse(reader1.matches('e'));
        
        CharacterReader reader2 = new CharacterReader("test");
        assertTrue(reader2.matches("tes"));
        assertFalse(reader2.matches("set"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("test");
        assertTrue(reader1.matchesIgnoreCase("TES"));
        assertFalse(reader1.matchesIgnoreCase("SET"));
        
        CharacterReader reader2 = new CharacterReader("test");
        assertTrue(reader2.matchesIgnoreCase("tEs"));
        assertFalse(reader2.matchesIgnoreCase("sEt"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader1 = new CharacterReader("test");
        assertTrue(reader1.matchesAny('t', 'e'));
        assertFalse(reader1.matchesAny('a', 'b'));
        
        CharacterReader reader2 = new CharacterReader("test");
        assertTrue(reader2.matchesAny(CharacterReader.EOF));
        assertFalse(reader2.matchesAny('a', 'b'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("test");
        assertTrue(reader1.matchesLetter());
        
        CharacterReader reader2 = new CharacterReader("123test");
        assertFalse(reader2.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("test123");
        assertFalse(reader1.matchesDigit());
        
        CharacterReader reader2 = new CharacterReader("123test");
        assertTrue(reader2.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader1 = new CharacterReader("test");
        assertTrue(reader1.matchConsume("te"));
        assertFalse(reader1.matchConsume("te"));
        
        CharacterReader reader2 = new CharacterReader("test");
        assertTrue(reader2.matchConsume("tes"));
        assertFalse(reader2.matchConsume("tes"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("test");
        assertTrue(reader1.matchConsumeIgnoreCase("TEs"));
        assertFalse(reader1.matchConsumeIgnoreCase("SEt"));
        
        CharacterReader reader2 = new CharacterReader("test");
        assertTrue(reader2.matchConsumeIgnoreCase("teS"));
        assertFalse(reader2.matchConsumeIgnoreCase("seT"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("test");
        assertTrue(reader1.containsIgnoreCase("tes"));
        assertFalse(reader1.containsIgnoreCase("set"));
        
        CharacterReader reader2 = new CharacterReader("test");
        assertTrue(reader2.containsIgnoreCase("TES"));
        assertFalse(reader2.containsIgnoreCase("SET"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}