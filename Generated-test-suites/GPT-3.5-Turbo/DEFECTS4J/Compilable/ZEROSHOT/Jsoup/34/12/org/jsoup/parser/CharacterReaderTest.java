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

        reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());

        reader.consume();
        assertEquals('e', reader.current());

        while (!reader.isEmpty()) {
            reader.consume();
        }
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('e', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.advance();
        assertEquals('e', reader.current());
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("s", reader.consumeAsString());
        assertEquals("t", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(2, reader.nextIndexOf('l'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(6, reader.nextIndexOf("world"));
        assertEquals(-1, reader.nextIndexOf("foo"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("hello,world");
        assertEquals("hello,", reader.consumeTo(','));
        assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("hello,world");
        assertEquals("hello", reader.consumeTo("world"));
        assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("hello,world");
        assertEquals("hello", reader.consumeToAny(','));
        assertEquals("world", reader.consumeToAny(','));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("hello,world");
        assertEquals("hello,world", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("hello123");
        assertEquals("hello", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("hello123");
        assertEquals("hello", reader.consumeLetterThenDigitSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABCD1234");
        assertEquals("ABCD", reader.consumeHexSequence());
        assertEquals("1234", reader.consumeToEnd());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("hello123");
        assertEquals("", reader.consumeLetterSequence());
        assertEquals("hello123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("hello,world");
        assertTrue(reader.matches('h'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("hello,world");
        assertTrue(reader.matches("hello"));
        assertFalse(reader.matches("foo"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello,world");
        assertTrue(reader.matchesIgnoreCase("HELLO"));
        assertFalse(reader.matchesIgnoreCase("FOO"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("hello,world");
        assertTrue(reader.matchesAny('h', 'z'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("hello,world");
        assertTrue(reader.matchesLetter());
        reader.consumeToEnd();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        reader.consumeToEnd();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("hello,world");
        assertTrue(reader.matchConsume("hello"));
        assertFalse(reader.matchConsume("foo"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello,world");
        assertTrue(reader.matchConsumeIgnoreCase("HELLO"));
        assertFalse(reader.matchConsumeIgnoreCase("FOO"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello,world");
        assertTrue(reader.containsIgnoreCase("WORLD"));
        assertFalse(reader.containsIgnoreCase("FOO"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.consume();
        assertEquals("st", reader.toString());
    }
}