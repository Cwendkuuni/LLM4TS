import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    // Test consume method
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals('g', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }
    
    // Test unconsume method
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.consume());
    }
    
    // Test advance method
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.advance();
        assertEquals('b', reader.consume());
    }
    
    // Test mark and rewindToMark methods
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('b', reader.consume());
    }
    
    // Test consumeAsString method
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
        assertEquals("d", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("f", reader.consumeAsString());
        assertEquals("g", reader.consumeAsString());
    }
    
    // Test nextIndexOf method
    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('h'));
    }
    
    // Test nextIndexOf method with CharSequence parameter
    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(-1, reader.nextIndexOf("cdh"));
    }
    
    // Test consumeTo method
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeTo('d'));
        assertEquals("efg", reader.consumeTo('h'));
    }
    
    // Test consumeTo method with String parameter
    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeTo("d"));
        assertEquals("efg", reader.consumeTo("h"));
    }
    
    // Test consumeToAny method
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeToAny('d', 'e'));
        assertEquals("fg", reader.consumeToAny('c', 'd'));
    }
    
    // Test consumeToEnd method
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcdefg", reader.consumeToEnd());
    }
    
    // Test consumeLetterSequence method
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("ab12efg");
        assertEquals("ab", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
        assertEquals("efg", reader.consumeLetterSequence());
    }
    
    // Test consumeLetterThenDigitSequence method
    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("ab12efg");
        assertEquals("ab", reader.consumeLetterThenDigitSequence());
        assertEquals("12", reader.consumeLetterThenDigitSequence());
        assertEquals("efg", reader.consumeLetterThenDigitSequence());
    }
    
    // Test consumeHexSequence method
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ab12efg");
        assertEquals("ab12e", reader.consumeHexSequence());
        assertEquals("fg", reader.consumeHexSequence());
    }
    
    // Test consumeDigitSequence method
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("ab12efg");
        assertEquals("", reader.consumeDigitSequence());
        assertEquals("12", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }
    
    // Test matches method
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('h'));
        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches("defg"));
    }
    
    // Test matchesIgnoreCase method
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdEFG");
        assertTrue(reader.matchesIgnoreCase('A'));
        assertFalse(reader.matchesIgnoreCase('H'));
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertFalse(reader.matchesIgnoreCase("DEFG"));
    }
    
    // Test matchesAny method
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('h', 'i'));
    }
    
    // Test matchesLetter method
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.isEmpty());
    }
    
    // Test matchesDigit method
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.isEmpty());
    }
    
    // Test matchConsume method
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchConsume("abc"));
        assertFalse(reader.matchConsume("de"));
        assertTrue(reader.matchConsume("fg"));
        assertFalse(reader.matchConsume("h"));
    }
    
    // Test matchConsumeIgnoreCase method
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdEFG");
        assertTrue(reader.matchConsumeIgnoreCase("abc"));
        assertFalse(reader.matchConsumeIgnoreCase("de"));
        assertTrue(reader.matchConsumeIgnoreCase("fg"));
        assertFalse(reader.matchConsumeIgnoreCase("h"));
    }
    
    // Test containsIgnoreCase method
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.containsIgnoreCase("bcd"));
        assertFalse(reader.containsIgnoreCase("deh"));
    }
    
    // Test toString method
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcdefg", reader.toString());
    }
}