package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Test");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Test");
        reader.mark();
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("T", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("Te", reader.consumeTo('s'));
        assertEquals("t", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("T", reader.consumeToAny('e', 's'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("Test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Test123");
        assertEquals("Test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("Test123");
        assertEquals("123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("Test123");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('t'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchesIgnoreCase("t"));
        assertFalse(reader.matchesIgnoreCase("S"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchesAny('T', 'e'));
        assertFalse(reader.matchesAny('s', 't'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Test123");
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchConsume("Test"));
        assertFalse(reader.matchConsume("Test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchConsumeIgnoreCase("tEsT"));
        assertFalse(reader.matchConsumeIgnoreCase("Test"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.containsIgnoreCase("tEsT"));
        assertFalse(reader.containsIgnoreCase("Test1"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("Test", reader.toString());
    }
}