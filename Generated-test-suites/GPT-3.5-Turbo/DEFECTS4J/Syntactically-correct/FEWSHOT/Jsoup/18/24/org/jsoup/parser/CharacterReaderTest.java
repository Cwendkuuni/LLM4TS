
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        assertTrue(reader1.isEmpty());

        CharacterReader reader2 = new CharacterReader("abc");
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader1.current());

        CharacterReader reader2 = new CharacterReader("abc");
        assertEquals('a', reader2.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader1.consume());

        CharacterReader reader2 = new CharacterReader("abc");
        assertEquals('a', reader2.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("abcde");
        reader.advance();
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeTo('d'));
        assertEquals('d', reader.current());
    }

    @Test
    public void testConsumeToWhenNotFound() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcdefg", reader.consumeTo('z'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeToAny('d', 'e'));
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeToAnyWhenNotFound() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcdefg", reader.consumeToAny('x', 'y', 'z'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcdefg", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals('1', reader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123g");
        assertEquals("abc123", reader.consumeHexSequence());
        assertEquals('g', reader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123g");
        assertEquals("", reader.consumeDigitSequence());
        assertEquals('a', reader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
    }

    @Test
    public void testMatchesWhenEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertFalse(reader.matches('a'));
    }

    @Test
    public void testMatches2() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("ba"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("abc");
        assertTrue(reader1.matchesLetter());

        CharacterReader reader2 = new CharacterReader("123abc");
        assertFalse(reader2.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("123");
        assertTrue(reader1.matchesDigit());

        CharacterReader reader2 = new CharacterReader("abc123");
        assertFalse(reader2.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("ab"));
        assertEquals('c', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Abc");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertEquals('c', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefghijklmno");
        assertTrue(reader.containsIgnoreCase("cde"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Override
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());
    }
}
