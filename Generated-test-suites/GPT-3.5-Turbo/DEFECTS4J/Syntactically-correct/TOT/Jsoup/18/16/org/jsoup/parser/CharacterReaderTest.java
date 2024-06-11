import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("sample input");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.unconsume();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("sample input");
        assertFalse(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
        reader.advance();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("sample input");
        assertEquals('s', reader.current());
        reader.consume();
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('m', reader.current());
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("sample input");
        assertEquals('s', reader.consume());
        assertEquals('a', reader.consume());
        assertEquals('m', reader.consume());
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("sample input");
        reader.consume();
        reader.unconsume();
        assertEquals('s', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("sample input");
        reader.consume();
        reader.advance();
        assertEquals('m', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("sample input");
        reader.mark();
        reader.advance();
        assertEquals('a', reader.current());
        reader.rewindToMark();
        assertEquals('s', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("sample input");
        assertEquals("s", reader.consumeAsString());
        assertEquals("a", reader.consumeAsString());
        assertEquals("m", reader.consumeAsString());
        reader.consumeToEnd();
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("sample input");
        assertEquals("sample ", reader.consumeTo(' '));
        assertEquals("in", reader.consumeTo('n'));
        assertEquals("put", reader.consumeTo('t'));
        assertEquals("", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("sample input");
        assertEquals("sa", reader.consumeToAny('m', 'n'));
        assertEquals("m", reader.consumeToAny('n', 't'));
        assertEquals("n", reader.consumeToAny('t', 'i'));
        assertEquals("t", reader.consumeToAny('i', 'u'));
        assertEquals("", reader.consumeToAny('i', 'u', 'l'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("sample input");
        assertEquals("sample input", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("sample input");
        assertEquals("sample", reader.consumeLetterSequence());
        reader.consume();
        assertEquals("", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1a2b3c4d");
        assertEquals("1a2b3c4d", reader.consumeHexSequence());
        reader.consume();
        assertEquals("", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("1234567890", reader.consumeDigitSequence());
        reader.consume();
        assertEquals("", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("sample input");
        assertTrue(reader.matches('s'));
        assertFalse(reader.matches('a'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Sample Input");
        assertTrue(reader.matchesIgnoreCase("sample"));
        assertFalse(reader.matchesIgnoreCase("input"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("sample input");
        assertTrue(reader.matchesAny('s', 'm'));
        assertFalse(reader.matchesAny('i', 'n'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("sample input");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("sample input");
        assertFalse(reader.matchesDigit());
        reader.consumeTo("ample ");
        assertTrue(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("sample input");
        assertTrue(reader.matchConsume("sample"));
        assertFalse(reader.matchConsume("abcd"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Sample Input");
        assertTrue(reader.matchConsumeIgnoreCase("sample"));
        assertFalse(reader.matchConsumeIgnoreCase("input"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Title of the Book");
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("book"));
    }
}