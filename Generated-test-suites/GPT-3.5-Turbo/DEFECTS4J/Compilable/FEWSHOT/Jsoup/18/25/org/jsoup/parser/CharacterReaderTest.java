
package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals(0, reader.pos());

        reader.consume();

        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertFalse(reader.isEmpty());

        while (!reader.isEmpty()) {
            reader.consume();
        }

        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals('a', reader.current());

        reader.consume();

        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals('a', reader.consume());

        reader.unconsume();

        assertEquals('a', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdef");

        reader.advance();

        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcdef");

        reader.mark();

        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("abcdef");

        reader.advance();

        reader.mark();

        reader.advance();

        assertEquals(2, reader.pos());

        reader.rewindToMark();

        assertEquals(1, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
        assertEquals("d", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("f", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("abc", reader.consumeTo('d'));
        assertEquals('d', reader.consume());
        assertEquals("ef", reader.consumeTo(CharacterReader.EOF));
    }

    @Test
    public void testConsumeToSeq() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("abc", reader.consumeTo("d"));
        assertEquals('d', reader.consume());
        assertEquals("ef", reader.consumeTo(CharacterReader.EOF));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("ab", reader.consumeToAny('c', 'd'));
        assertEquals('c', reader.consume());
        assertEquals("ef", reader.consumeToAny(CharacterReader.EOF));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("abcdef", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123");

        assertEquals("abcd", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcdef123");

        assertEquals("abcdef", reader.consumeHexSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc");

        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));

        reader.consume();

        assertTrue(reader.matches('b'));
    }

    @Test
    public void testMatchesSeq() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches("def"));

        reader.consume();

        assertTrue(reader.matches("def"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("aBcdef");

        assertTrue(reader.matchesIgnoreCase("abc"));
        assertFalse(reader.matchesIgnoreCase("def"));

        reader.consume();

        assertTrue(reader.matchesIgnoreCase("def"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('d', 'e', 'f'));

        reader.consume();

        assertTrue(reader.matchesAny('d', 'e', 'f'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());

        reader.consume();

        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertFalse(reader.matchesDigit());

        reader.consume();

        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertTrue(reader.matchConsume("abc"));
        assertEquals('d', reader.consume());

        assertFalse(reader.matchConsume("def"));

        assertEquals('d', reader.consume());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("aBcdef");

        assertTrue(reader.matchConsumeIgnoreCase("abc"));
        assertEquals('d', reader.consume());

        assertFalse(reader.matchConsumeIgnoreCase("def"));

        assertEquals('d', reader.consume());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertTrue(reader.containsIgnoreCase("abc"));

        reader.consume();

        assertFalse(reader.containsIgnoreCase("abc"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdef");

        reader.consume();

        assertEquals("bcdef", reader.toString());
    }
}
