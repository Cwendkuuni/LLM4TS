import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test initial position is 0
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
        
        // Test position after consuming characters
        reader.consume();
        reader.consume();
        assertEquals(2, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        // Test empty reader
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        
        // Test non-empty reader
        reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        // Test current character
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
        
        // Test current character after consuming
        reader.consume();
        assertEquals('e', reader.current());
        
        // Test current character when reader is isEmpty
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }
    
    @Test
    public void testConsume() {
        // Test consume and return character
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        
        // Test consume when reader is isEmpty
        reader.consume();
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        // Test unconsume
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        assertEquals('e', reader.current());
        reader.unconsume();
        assertEquals('t', reader.current());
        
        // Test unconsume when at the beginning of reader
        reader.unconsume();
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testAdvance() {
        // Test advance
        CharacterReader reader = new CharacterReader("test");
        assertEquals('e', reader.advance());
        assertEquals('s', reader.advance());
        
        // Test advance when reader is isEmpty
        reader.advance();
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.advance());
    }
    
    @Test
    public void testMark() {
        // Test mark
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.consume();
        reader.consume();
        assertEquals('s', reader.current());
        
        // Test rewindToMark
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        // Test consumeAsString
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOf() {
        // Test nextIndexOf for character
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
        
        // Test nextIndexOf for sequence
        assertEquals(1, reader.nextIndexOf("es"));
        
        // Test nextIndexOf when target is not found
        assertEquals(-1, reader.nextIndexOf('x'));
        
        // Test nextIndexOf for sequence when sequence is not found
        assertEquals(-1, reader.nextIndexOf("abc"));
    }
    
    @Test
    public void testConsumeTo() {
        // Test consumeTo for character
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo('e'));
        
        // Test consumeTo for sequence
        reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo("es"));
        
        // Test consumeTo when target is not found
        reader = new CharacterReader("test");
        assertEquals("test", reader.consumeTo('x'));
        
        // Test consumeTo for sequence when sequence is not found
        reader = new CharacterReader("test");
        assertEquals("test", reader.consumeTo("abc"));
    }
    
    @Test
    public void testConsumeToAny() {
        // Test consumeToAny
        CharacterReader reader = new CharacterReader("test");
        assertEquals("te", reader.consumeToAny('s', 't'));
        
        // Test consumeToAny when none of the characters are found
        reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToAny('x', 'y', 'z'));
        
        // Test consumeToAny when no characters are consumed
        reader = new CharacterReader("test");
        assertEquals("", reader.consumeToAny('x'));
    }
    
    @Test
    public void testConsumeToEnd() {
        // Test consumeToEnd
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        // Test consumeLetterSequence
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        // Test consumeLetterThenDigitSequence
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        // Test consumeHexSequence
        CharacterReader reader = new CharacterReader("0123456789abcdef");
        assertEquals("0123456789abcdef", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        // Test consumeDigitSequence
        CharacterReader reader = new CharacterReader("0123456789abcdef");
        assertEquals("0123456789", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        // Test matches for character
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
        
        // Test matches for sequence
        reader = new CharacterReader("test");
        assertTrue(reader.matches("tes"));
        assertFalse(reader.matches("abc"));
        
        // Test matches for sequence when length is greater than remaining characters
        reader = new CharacterReader("test");
        assertFalse(reader.matches("test123"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        // Test matchesIgnoreCase
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("Tes"));
        assertFalse(reader.matchesIgnoreCase("ABC"));

        // Test matchesIgnoreCase when length is greater than remaining characters
        reader = new CharacterReader("test");
        assertFalse(reader.matchesIgnoreCase("Test123"));
    }
    
    @Test
    public void testMatchesAny() {
        // Test matchesAny
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t', 'e'));
        assertFalse(reader.matchesAny('a', 'b'));
        
        // Test matchesAny when reader is isEmpty
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesAny());
    }
    
    @Test
    public void testMatchesLetter() {
        // Test matchesLetter
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        
        // Test matchesLetter when reader is isEmpty
        reader.consume();
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        // Test matchesDigit
        CharacterReader reader = new CharacterReader("test123");
        assertFalse(reader.matchesDigit());
        
        // Test matchesDigit when reader is isEmpty
        reader.consume();
        reader.consume();
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        // Test matchConsume for sequence
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("tes"));
        assertFalse(reader.matchConsume("abc"));
        
        // Test matchConsume for sequence when length is greater than remaining characters
        reader = new CharacterReader("test");
        assertFalse(reader.matchConsume("test123"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        // Test matchConsumeIgnoreCase
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("TeS"));
        assertFalse(reader.matchConsumeIgnoreCase("ABC"));
        
        // Test matchConsumeIgnoreCase when length is greater than remaining characters
        reader = new CharacterReader("test");
        assertFalse(reader.matchConsumeIgnoreCase("test123"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        // Test containsIgnoreCase
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("Tes"));
        assertFalse(reader.containsIgnoreCase("ABC"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}