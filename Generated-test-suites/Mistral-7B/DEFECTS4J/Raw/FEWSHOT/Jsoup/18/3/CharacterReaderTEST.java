package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());

        reader.advance();
        assertEquals(1, reader.pos());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());

        reader.advance();
        assertEquals('b', reader.current());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());

        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        assertEquals('b', reader.current());

        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("ab", reader.consumeAsString());

        reader.advance();
        assertEquals("c", reader.consumeAsString());

        reader.mark();
        assertEquals("ab", reader.consumeAsString());
        reader.rewindToMark();
        assertEquals("a", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("ef", reader.consumeTo('e'));
        assertEquals("", reader.consumeTo('a'));

        reader.advance();
        assertEquals("cd", reader.consumeTo('d'));
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals("bc", reader.consumeTo('c'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("012345");
        assertEquals("123", reader.consumeToAny('1', '3'));
        assertEquals("5", reader.consumeToAny('5'));
        assertEquals("", reader.consumeToAny('0'));

        reader.advance();
        assertEquals("24", reader.consumeToAny('2', '4'));
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals("123", reader.consumeToAny('1', '3'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeToEnd());

        reader.advance();
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc ABC def");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("ABC", reader.consumeLetterSequence());
        assertEquals("def", reader.consumeLetterSequence());

        reader.advance();
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x123f 0X456a");
        assertEquals("123f", reader.consumeHexSequence());
        assertEquals("456a", reader.consumeHexSequence());

        reader.advance();
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123 456 789");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("456", reader.consumeDigitSequence());
        assertEquals("789", reader.consumeDigitSequence());

        reader.advance();
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));

        reader.advance();
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches('f'));
        assertFalse(reader.matches('a'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCdef");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertTrue(reader.matchesIgnoreCase("c"));
        assertFalse(reader.matchesIgnoreCase("D"));

        reader.advance();
        assertTrue(reader.matchesIgnoreCase("e"));
        assertTrue(reader.matchesIgnoreCase("f"));
        assertFalse(reader.matchesIgnoreCase("A"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("012345");
        assertTrue(reader.matchesAny('0', '1', '2', '3', '4', '5'));
        assertFalse(reader.matchesAny('6'));

        reader.advance();
        assertTrue(reader.matchesAny('2', '4'));
        assertFalse(reader.matchesAny('0'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc ABC def");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader.advance();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123 456 789");
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());

        reader.advance();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc ABC def");
        assertTrue(reader.matchConsume("ab"));
        assertTrue(reader.matchConsume("AB"));
        assertFalse(reader.matchConsume("cd"));

        reader.advance();
        assertTrue(reader.matchConsume("ef"));
        assertFalse(reader.matchConsume("xyz"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCdef");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertTrue(reader.matchConsumeIgnoreCase("AB"));
        assertFalse(reader.matchConsumeIgnoreCase("cd"));

        reader.advance();
        assertTrue(reader.matchConsumeIgnoreCase("ef"));
        assertFalse(reader.matchConsumeIgnoreCase("xyz"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/style"));
        assertFalse(reader.containsIgnoreCase("/body"));

        reader.advance();
        assertTrue(reader.containsIgnoreCase("/head"));
        assertFalse(reader.containsIgnoreCase("/footer"));
    }
}