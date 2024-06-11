package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    private static final String TEST_STRING = "abcdefghijklmNOPQRSTuvwxyzABCDEFGHIJKLM12345";

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        CharacterReader reader1 = new CharacterReader("abc");
        assertFalse(reader1.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());

        CharacterReader reader1 = new CharacterReader("abcd");
        assertEquals('a', reader1.consume());
        assertEquals('b', reader1.consume());
        assertEquals('c', reader1.consume());
        assertEquals('d', reader1.consume());
        assertEquals(CharacterReader.EOF, reader1.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());

        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('c', reader.current());

        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.advance();
        assertEquals('b', reader.current());

        reader.advance();
        assertEquals('c', reader.current());

        reader.advance();
        assertEquals('d', reader.current());

        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        reader.consume();
        assertEquals('a', reader.current());

        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeAsString());

        CharacterReader reader1 = new CharacterReader("abcd");
        assertEquals("abc", reader1.consumeAsString(3));
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(TEST_STRING);

        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(16, reader.nextIndexOf('M'));
        assertEquals(-1, reader.nextIndexOf('Z'));

        CharacterReader reader1 = new CharacterReader("12345");
        assertEquals(1, reader1.nextIndexOf('2'));
        assertEquals(-1, reader1.nextIndexOf('6'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeTo('d'));
        assertEquals("def", reader.consumeToEnd());

        CharacterReader reader1 = new CharacterReader("12345");
        assertEquals("123", reader1.consumeTo('4'));
        assertEquals("5", reader1.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefghijklmABCDEFGHIJKLM12345");
        assertEquals("defghijklmABCDEFGHIJKLM", reader.consumeToAny('a', 'z', 'A', 'Z', '0', '9'));
        assertEquals("", reader.consumeToAny('z', 'z'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeToEnd());

        CharacterReader reader1 = new CharacterReader("12345");
        assertEquals("5", reader1.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmNOPQRSTuvwxyzABCDEFGHIJKLM12345");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("NOPQRST", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmNOPQRSTuvwxyzABCDEFGHIJKLM12345");
        assertEquals("ab1", reader.consumeLetterThenDigitSequence());
        assertEquals("34", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmNOPQRSTuvwxyzABCDEFGHIJKLM12345");
        assertEquals("abc", reader.consumeHexSequence());
        assertEquals("F0", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmNOPQRSTuvwxyzABCDEFGHIJKLM12345");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("0", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));

        CharacterReader reader1 = new CharacterReader("123");
        assertTrue(reader1.matches('1'));
        assertTrue(reader1.matches('2'));
        assertTrue(reader1.matches('3'));
        assertFalse(reader1.matches('4'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCdEfGhIjKlM");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertTrue(reader.matchesIgnoreCase("gh"));
        assertFalse(reader.matchesIgnoreCase("xy"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdefghijklmNOPQRSTuvwxyzABCDEFGHIJKLM12345");
        assertTrue(reader.matchesAny('a', 'z', 'A', 'Z', '0', '9'));
        assertFalse(reader.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdefghijklmNOPQRSTuvwxyzABCDEFGHIJKLM12345");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcdefghijklmNOPQRSTuvwxyzABCDEFGHIJKLM12345");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdefghijklmNOPQRSTuvwxyzABCDEFGHIJKLM12345");
        assertTrue(reader.matchConsume("ab"));
        assertTrue(reader.matchConsume("ef"));
        assertTrue(reader.matchConsume("gh"));
        assertFalse(reader.matchConsume("xy"));

        CharacterReader reader1 = new CharacterReader("AbCdEfGhIjKlM");
        assertTrue(reader1.matchConsumeIgnoreCase("ab"));
        assertTrue(reader1.matchConsumeIgnoreCase("ef"));
        assertTrue(reader1.matchConsumeIgnoreCase("gh"));
        assertFalse(reader1.matchConsumeIgnoreCase("xy"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefghijklmNOPQRSTuvwxyzABCDEFGHIJKLM12345");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/style"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.toString());

        CharacterReader reader1 = new CharacterReader("12345");
        assertEquals("5", reader1.toString());
    }
}