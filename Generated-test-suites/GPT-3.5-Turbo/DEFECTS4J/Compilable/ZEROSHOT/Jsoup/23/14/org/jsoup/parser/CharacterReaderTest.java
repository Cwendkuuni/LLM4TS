package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test when pos() method is called
        // Given
        CharacterReader reader = new CharacterReader("");
        
        // When
        int result = reader.pos();
        
        // Then
        assertEquals(0, result);
    }
    
    @Test
    public void testIsEmpty() {
        // Test when isEmpty() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        boolean result = reader.isEmpty();
        
        // Then
        assertFalse(result);
    }
    
    @Test
    public void testCurrent() {
        // Test when current() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        char result = reader.current();
        
        // Then
        assertEquals('t', result);
    }
    
    @Test
    public void testConsume() {
        // Test when consume() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        char result = reader.consume();
        
        // Then
        assertEquals('t', result);
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testUnconsume() {
        // Test when unconsume() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        reader.consume();
        reader.unconsume();
        
        // Then
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testAdvance() {
        // Test when advance() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        reader.advance();
        
        // Then
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        // Test when mark() and rewindToMark() methods are called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        reader.advance();
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();

        // Then
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        // Test when consumeAsString() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        String result = reader.consumeAsString();
        
        // Then
        assertEquals("t", result);
    }
    
    @Test
    public void testConsumeTo() {
        // Test when consumeTo(char) method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        String result = reader.consumeTo('e');
        
        // Then
        assertEquals("t", result);
    }
    
    @Test
    public void testConsumeToSeq() {
        // Test when consumeTo(String) method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        String result = reader.consumeTo("es");
        
        // Then
        assertEquals("t", result);
    }
    
    @Test
    public void testConsumeToAny() {
        // Test when consumeToAny(char...) method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        String result = reader.consumeToAny('a', 'e');
        
        // Then
        assertEquals("t", result);
    }
    
    @Test
    public void testConsumeToEnd() {
        // Test when consumeToEnd() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        String result = reader.consumeToEnd();
        
        // Then
        assertEquals("test", result);
    }
    
    @Test
    public void testConsumeLetterSequence() {
        // Test when consumeLetterSequence() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        String result = reader.consumeLetterSequence();
        
        // Then
        assertEquals("test", result);
    }
    
    @Test
    public void testConsumeHexSequence() {
        // Test when consumeHexSequence() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        String result = reader.consumeHexSequence();
        
        // Then
        assertEquals("", result);
    }
    
    @Test
    public void testConsumeDigitSequence() {
        // Test when consumeDigitSequence() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        String result = reader.consumeDigitSequence();
        
        // Then
        assertEquals("", result);
    }
    
    @Test
    public void testMatches() {
        // Test when matches(char) method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        boolean result = reader.matches('t');
        
        // Then
        assertTrue(result);
    }

    @Test
    public void testMatchesSeq() {
        // Test when matches(String) method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        boolean result = reader.matches("te");
        
        // Then
        assertTrue(result);
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        // Test when matchesIgnoreCase(String) method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        boolean result = reader.matchesIgnoreCase("TE");
        
        // Then
        assertTrue(result);
    }
    
    @Test
    public void testMatchesAny() {
        // Test when matchesAny(char...) method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        boolean result = reader.matchesAny('a', 't');
        
        // Then
        assertTrue(result);
    }
    
    @Test
    public void testMatchesLetter() {
        // Test when matchesLetter() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        boolean result = reader.matchesLetter();
        
        // Then
        assertTrue(result);
    }
    
    @Test
    public void testMatchesDigit() {
        // Test when matchesDigit() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        boolean result = reader.matchesDigit();
        
        // Then
        assertFalse(result);
    }
    
    @Test
    public void testMatchConsume() {
        // Test when matchConsume(String) method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        boolean result = reader.matchConsume("te");
        
        // Then
        assertTrue(result);
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        // Test when matchConsumeIgnoreCase(String) method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        boolean result = reader.matchConsumeIgnoreCase("TE");
        
        // Then
        assertTrue(result);
    }
    
    @Test
    public void testContainsIgnoreCase() {
        // Test when containsIgnoreCase(String) method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        boolean result = reader.containsIgnoreCase("TE");
        
        // Then
        assertFalse(result);
    }
    
    @Test
    public void testToString() {
        // Test when toString() method is called
        // Given
        CharacterReader reader = new CharacterReader("test");
        
        // When
        String result = reader.toString();
        
        // Then
        assertEquals("test", result);
    }
}