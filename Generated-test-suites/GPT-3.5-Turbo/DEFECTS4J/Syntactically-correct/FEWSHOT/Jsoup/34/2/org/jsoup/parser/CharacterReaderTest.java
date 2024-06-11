import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals('H', reader.current());
    }
    
    @Test
    public void testConsume() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals(3, reader.pos());
    }
    
    @Test
    public void testUnconsume() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        reader.consume();
        reader.consume();
        reader.consume();
        reader.unconsume();
        
        assertEquals('l', reader.current());
    }
    
    @Test
    public void testAdvance() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        reader.advance();
        
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testMark() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        reader.mark();
        
        assertEquals(0, reader.mark);
    }
    
    @Test
    public void testRewindToMark() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("H", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("l", reader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOfChar() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals(2, reader.nextIndexOf('l'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }
    
    @Test
    public void testNextIndexOfSequence() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals(2, reader.nextIndexOf("ll"));
        assertEquals(-1, reader.nextIndexOf("zz"));
    }
    
    @Test
    public void testConsumeToChar() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("Hello ", reader.consumeTo('o'));
        assertEquals("world", reader.consumeTo('z'));
    }
    
    @Test
    public void testConsumeToSequence() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("Hello ", reader.consumeTo("o"));
        assertEquals("w", reader.consumeTo("rl"));
        assertEquals("orld", reader.consumeTo("zz"));
    }
    
    @Test
    public void testConsumeToAny() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("Hello ", reader.consumeToAny('o', 'z'));
        assertEquals("world", reader.consumeToAny('z'));
    }
    
    @Test
    public void testConsumeToEnd() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("Hello world", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        String input = "Hello1 world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("Hello", reader.consumeLetterSequence());
        assertEquals("1", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "Hello1 world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("Hello", reader.consumeLetterThenDigitSequence());
        assertEquals("1", reader.consumeLetterThenDigitSequence());
        assertEquals(" ", reader.consumeLetterThenDigitSequence());
        assertEquals("world", reader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        String input = "A12F";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("A12F", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        String input = "12345";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("12345", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatchesChar() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('e'));
    }
    
    @Test
    public void testMatchesString() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matches("He"));
        assertFalse(reader.matches("el"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchesIgnoreCase("hE"));
        assertFalse(reader.matchesIgnoreCase("El"));
    }
    
    @Test
    public void testMatchesAny() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchesAny('H', 'e'));
        assertFalse(reader.matchesAny('e', 'l'));
    }
    
    @Test
    public void testMatchesLetter() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchConsume("He"));
        assertEquals(2, reader.pos());
        assertFalse(reader.matchConsume("le"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchConsumeIgnoreCase("hE"));
        assertEquals(2, reader.pos());
        assertFalse(reader.matchConsumeIgnoreCase("lE"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.containsIgnoreCase("hE"));
        assertFalse(reader.containsIgnoreCase("lE"));
    }
    
    @Test
    public void testToString() {
        String input = "Hello world";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("Hello world", reader.toString());
    }
}