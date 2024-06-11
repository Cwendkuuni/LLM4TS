package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;
 
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
        // TODO: Implement test for mark() method
    }

    @Test
    public void testRewindToMark() {
        // TODO: Implement test for rewindToMark() method
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("tes", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("tes", reader.consumeToAny('t'));
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
    public void testConsumeHexSequence() {
        // TODO: Implement test for consumeHexSequence() method
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("12345", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("TeSt");
        assertTrue(reader.matchesIgnoreCase("tEsT"));
    }

    @Test
    public void testMatchesAny() {
        // TODO: Implement test for matchesAny() method
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        // TODO: Implement test for matchConsume() method
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        // TODO: Implement test for matchConsumeIgnoreCase() method
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.containsIgnoreCase("abc"));
        assertTrue(reader.containsIgnoreCase("tes"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }

}