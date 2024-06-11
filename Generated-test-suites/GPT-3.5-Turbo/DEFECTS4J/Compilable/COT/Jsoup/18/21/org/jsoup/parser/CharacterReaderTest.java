package org.jsoup.parser;
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
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testIsEmpty_False() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
    }

    @Test
    public void testCurrent_Empty() {
        CharacterReader reader = new CharacterReader("");
        assertEquals((char) -1, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("te", reader.consumeTo('s'));
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
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123ABC");
        assertEquals("123ABC", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123ABC");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
    }

    @Test
    public void testMatches_False() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matches('s'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("TE"));
    }

    @Test
    public void testMatchesIgnoreCase_False() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchesIgnoreCase("TS"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t', 's'));
    }

    @Test
    public void testMatchesAny_False() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchesAny('r', 's'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesLetter_False() {
        CharacterReader reader = new CharacterReader("123");
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit_False() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("te"));
    }

    @Test
    public void testMatchConsume_False() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchConsume("se"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("TE"));
    }

    @Test
    public void testMatchConsumeIgnoreCase_False() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchConsumeIgnoreCase("SE"));
    }

    @Test
    public void testContainsIgnoreCase_True() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("ES"));
    }

    @Test
    public void testContainsIgnoreCase_False() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.containsIgnoreCase("TS"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}