
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testIsEmpty_EmptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        assertEquals('b', reader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("a", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeTo('d'));
    }
    
    @Test
    public void testConsumeTo_Seq() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeTo("d"));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeToAny('d','e','f'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("fff123");
        assertEquals("fff", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
    }
    
    @Test
    public void testMatches_Seq() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches("abcd"));
        assertFalse(reader.matches("bcde"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesIgnoreCase("ABCD"));
        assertFalse(reader.matchesIgnoreCase("BCDE"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a','b','c'));
        assertFalse(reader.matchesAny('d','e','f'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchConsume("abcd"));
        assertFalse(reader.matchConsume("bcde"));
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchConsumeIgnoreCase("ABCD"));
        assertFalse(reader.matchConsumeIgnoreCase("BCDE"));
        assertEquals('c', reader.current());
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.containsIgnoreCase("abc"));
        assertFalse(reader.containsIgnoreCase("defg"));
    }
}
