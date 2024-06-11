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

        reader = new CharacterReader("Test");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());

        reader.consume();
        assertEquals('e', reader.current());

        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Test");
        reader.advance();
        assertEquals(1, reader.pos());

        reader.advance();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("Test");
        reader.mark();
        reader.advance();
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
        assertEquals("T", reader.consumeTo('e'));
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
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("Test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1a2b");
        assertEquals("1a2b", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234");
        assertEquals("1234", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchesIgnoreCase("te"));
        assertFalse(reader.matchesIgnoreCase("t"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchesAny('T', 'e'));
        assertFalse(reader.matchesAny('t', 's'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Test");
        assertFalse(reader.matchesDigit());

        reader.advance();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Test");
        assertFalse(reader.matchConsume("e"));
        assertTrue(reader.matchConsume("T"));
        assertEquals(1, reader.pos());

        assertFalse(reader.matchConsume("t"));
        assertTrue(reader.matchConsumeIgnoreCase("EST"));
        assertEquals(4, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.containsIgnoreCase("TE"));
        assertFalse(reader.containsIgnoreCase("ES"));

        reader.advance();
        assertFalse(reader.containsIgnoreCase("ES"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("Test", reader.toString());
    }
}