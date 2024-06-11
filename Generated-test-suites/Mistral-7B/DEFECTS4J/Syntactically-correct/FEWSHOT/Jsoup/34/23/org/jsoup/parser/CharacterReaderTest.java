package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertNull(reader.consume());
    }

    @Test
    public void testSingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals('a', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMultipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        assertEquals('a', reader.consume());
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("ab", reader.consumeAsString());
        assertEquals('c', reader.consume());
        assertEquals("d", reader.consumeAsString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("ababc");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('e'));

        reader.advance();
        assertEquals(2, reader.nextIndexOf('b'));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader("ababab");
        assertEquals(1, reader.nextIndexOf("ab"));
        assertEquals(5, reader.nextIndexOf("ab"));
        assertEquals(-1, reader.nextIndexOf("cd"));

        reader.advance();
        assertEquals(2, reader.nextIndexOf("ab"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals('d', reader.consume());
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("ababab");
        assertEquals("ab", reader.consumeTo("ab"));
        assertEquals("cd", reader.consumeTo("cd"));
        assertEquals("", reader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToSequence() {
        CharacterReader reader = new CharacterReader("ababab");
        assertEquals("ab", reader.consumeTo("ab"));
        assertEquals("ef", reader.consumeTo("ef"));
        assertEquals("", reader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("def", reader.consumeToAny('e', 'f'));
        assertEquals("", reader.consumeToAny('x', 'y'));

        reader.advance();
        assertEquals("efg", reader.consumeToAny('e', 'g'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("abcd", reader.consumeToEnd());
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abcdefg");
        assertEquals("abcdefg", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd1efg");
        assertEquals("cd", reader.consumeLetterSequence());
        assertEquals("efg", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());

        reader.advance();
        assertEquals("efg", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd1efg");
        assertEquals("cd1", reader.consumeLetterThenDigitSequence());
        assertEquals("efg", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());

        reader.advance();
        assertEquals("efg", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('d'));
        assertFalse(reader.matches('e'));

        reader.advance();
        assertFalse(reader.matches('a'));
        assertTrue(reader.matches('d'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader("ababab");
        assertTrue(reader.matches("ab"));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));

        reader.advance();
        assertTrue(reader.matches("ab"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abAbAbaBA");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("AB"));
        assertFalse(reader.matchesIgnoreCase("cd"));

        reader.advance();
        assertTrue(reader.matchesIgnoreCase("ab"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAny('e', 'f'));

        reader.advance();
        assertTrue(reader.matchesAny('b', 'c', 'd'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader.advance();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcd123");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());

        reader.advance();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("abcd", reader.toString());
        assertTrue(reader.isEmpty());
    }
}