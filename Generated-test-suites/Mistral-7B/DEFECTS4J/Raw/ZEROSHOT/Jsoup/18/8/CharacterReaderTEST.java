package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderNotEmpty() {
        CharacterReader reader = new CharacterReader("hello world");
        assertFalse(reader.isEmpty());
        assertEquals('h', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeEnd() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeString() {
        CharacterReader reader = new CharacterReader("hello world");
        String consumed = reader.consumeAsString();
        assertEquals("hello", consumed);
        assertEquals('w', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testCharacterReaderConsumePastEnd() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.consumeToEnd();
        reader.consume();
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("hello world");
        String consumed = reader.consumeTo('l');
        assertEquals("he", consumed);
        assertEquals('o', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("hello world");
        String consumed = reader.consumeToEnd();
        assertEquals("hello world", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        String consumed = reader.consumeToAny('a', 'e');
        assertEquals("a", consumed);
        assertEquals('b', reader.current());

        consumed = reader.consumeToAny('f');
        assertEquals("def", consumed);
        assertEquals('f', reader.current());

        consumed = reader.consumeToAny('z');
        assertEquals("", consumed);
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        String consumed = reader.consumeLetterSequence();
        assertEquals("a", consumed);
        assertEquals('b', reader.current());

        consumed = reader.consumeLetterSequence();
        assertEquals("c", consumed);
        assertEquals('d', reader.current());

        consumed = reader.consumeLetterSequence();
        assertEquals("e", consumed);
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0123456789ABCDEFabcdef");
        String consumed = reader.consumeHexSequence();
        assertEquals("01", consumed);
        assertEquals('2', reader.current());

        consumed = reader.consumeHexSequence();
        assertEquals("34", consumed);
        assertEquals('5', reader.current());

        consumed = reader.consumeHexSequence();
        assertEquals("", consumed);
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("0123456789");
        String consumed = reader.consumeDigitSequence();
        assertEquals("012", consumed);
        assertEquals("3", reader.current());

        consumed = reader.consumeDigitSequence();
        assertEquals("45", consumed);
        assertEquals("6", reader.current());

        consumed = reader.consumeDigitSequence();
        assertEquals("", consumed);
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderMatch() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matches('h'));
        assertTrue(reader.matches("he"));
        assertFalse(reader.matches('a'));
    }

    @Test
    public void testCharacterReaderMatchIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesIgnoreCase("He"));
        assertTrue(reader.matchesIgnoreCase("hello"));
        assertFalse(reader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testCharacterReaderMatchAny() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('z'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("0123456789");
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.mark();
        reader.consumeTo('l');
        reader.rewindToMark();
        assertEquals('h', reader.current());
    }
}