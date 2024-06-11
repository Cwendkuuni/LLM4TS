package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader emptyReader = new CharacterReader("");
        assertTrue(emptyReader.isEmpty());

        CharacterReader nonEmptyReader = new CharacterReader("test string");
        assertFalse(nonEmptyReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals('t', reader.current());

        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test string");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test string");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("test string");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test, string");
        assertEquals("test", reader.consumeTo(','));
        assertEquals(", ", reader.consumeTo(','));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("test ", reader.consumeToAny(' ', ','));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("test string", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertEquals("This", reader.consumeLetterSequence());
        assertEquals("is", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeHexSequence());
        assertEquals("abc", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test string");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test string");
        assertTrue(reader.matchesIgnoreCase("TEST"));
        assertFalse(reader.matchesIgnoreCase("STRING"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test string");
        assertTrue(reader.matchesAny('t', 'e', 's'));
        assertFalse(reader.matchesAny('a', 'b', 'c'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test string");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123abc");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test string");
        assertTrue(reader.matchConsume("test"));
        assertFalse(reader.matchConsume("test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test string");
        assertTrue(reader.matchConsumeIgnoreCase("TEST"));
        assertFalse(reader.matchConsumeIgnoreCase("STRING"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test string");
        assertTrue(reader.containsIgnoreCase("TEST"));
        assertTrue(reader.containsIgnoreCase("STRING"));
        assertFalse(reader.containsIgnoreCase("123"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test string");
        reader.consume();
        assertEquals("est string", reader.toString());
    }

}