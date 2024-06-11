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
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.isEmpty());
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
        reader.advance();
        assertEquals('n', reader.current());
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
    }
    
    @Test
    public void testNextIndexOfWithSeq() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals(2, reader.nextIndexOf("put"));
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("in", reader.consumeTo('p'));
    }
    
    @Test
    public void testConsumeToWithSeq() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("in", reader.consumeTo("put"));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("i", reader.consumeToAny('n', 'p'));
    }
    
    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("", reader.consumeToAnySorted('a', 'e'));
    }
    
    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.consumeData());
    }
    
    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.consumeTagName());
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
        CharacterReader reader = new CharacterReader("input");
        assertEquals("", reader.consumeHexSequence());
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
        assertFalse(reader.matches('n'));
    }
    
    @Test
    public void testMatchesWithSeq() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matches("inp"));
        assertFalse(reader.matches("out"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesIgnoreCase("iNP"));
        assertFalse(reader.matchesIgnoreCase("oUT"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesAny('i', 'n'));
        assertFalse(reader.matchesAny('o', 'u'));
    }
    
    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.matchesAnySorted('a', 'e'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchConsume("inp"));
        assertFalse(reader.matchConsume("inp"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchConsumeIgnoreCase("INn"));
        assertFalse(reader.matchConsumeIgnoreCase("INn"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.containsIgnoreCase("out"));
        assertTrue(reader.containsIgnoreCase("put"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.toString());
    }
    
}