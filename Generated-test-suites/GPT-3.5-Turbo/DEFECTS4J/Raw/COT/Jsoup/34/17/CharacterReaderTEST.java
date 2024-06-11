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
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
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
        reader.mark();
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
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
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(2, reader.nextIndexOf("st"));
    }
    
    @Test
    public void testConsumeTo_char() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("te", reader.consumeTo('s'));
    }
    
    @Test
    public void testConsumeTo_String() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("te", reader.consumeTo("st"));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("tes", reader.consumeToAny('t', 's'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test123", reader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test1F");
        assertEquals("test1F", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123test");
        assertEquals("123", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches_char() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('s'));
    }
    
    @Test
    public void testMatches_String() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches("test"));
        assertFalse(reader.matches("st"));
    }
    
    @Test
    public void testMatchesIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("tEsT");
        assertTrue(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matchesIgnoreCase("st"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('a', 't'));
        assertFalse(reader.matchesAny('A', 'T'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume_String() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("test"));
        assertFalse(reader.matchConsume("st"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("tEsT");
        assertTrue(reader.matchConsumeIgnoreCase("test"));
        assertFalse(reader.matchConsumeIgnoreCase("st"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("TE"));
        assertFalse(reader.containsIgnoreCase("ST"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}