package org.jsoup.parser;

import org.junit.*;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        assertTrue(reader1.isEmpty());

        CharacterReader reader2 = new CharacterReader("abcd");
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader1.current());

        CharacterReader reader2 = new CharacterReader("abcd");
        assertEquals('a', reader2.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        reader.unconsume();
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.consume());
        reader.advance();
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        reader.rewindToMark();
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("a", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("ab", reader.consumeTo('c'));
        assertEquals("c", reader.consumeTo('d'));
        assertEquals("d", reader.consumeTo('e'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("a", reader.consumeToAny('b', 'c'));
        assertEquals("b", reader.consumeToAny('c', 'd'));
        assertEquals("cd", reader.consumeToAny('e'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("abcd", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0xABCD");
        assertEquals("0xABCD", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("12345", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader1 = new CharacterReader("abcd");
        assertTrue(reader1.matches('a'));

        CharacterReader reader2 = new CharacterReader("abcd");
        assertFalse(reader2.matches('b'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("Abcd");
        assertTrue(reader1.matchesIgnoreCase("AB"));

        CharacterReader reader2 = new CharacterReader("abcd");
        assertFalse(reader2.matchesIgnoreCase("BC"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader1 = new CharacterReader("abcd");
        assertTrue(reader1.matchesAny('a', 'b'));

        CharacterReader reader2 = new CharacterReader("abcd");
        assertFalse(reader2.matchesAny('c', 'd'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("abcd");
        assertTrue(reader1.matchesLetter());

        CharacterReader reader2 = new CharacterReader("1234");
        assertFalse(reader2.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("abcd");
        assertFalse(reader1.matchesDigit());

        CharacterReader reader2 = new CharacterReader("1234");
        assertTrue(reader2.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchConsume("ab"));
        assertFalse(reader.matchConsume("cd"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Abcd");
        assertTrue(reader.matchConsumeIgnoreCase("AB"));
        assertFalse(reader.matchConsumeIgnoreCase("CD"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.containsIgnoreCase("AB"));

        CharacterReader reader2 = new CharacterReader("abcd");
        assertFalse(reader2.containsIgnoreCase("CD"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("abcd", reader.toString());
    }
}