package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
    }

    @Test
    public void testConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(-1, reader.consume());
    }

    @Test
    public void testConsumeString() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeAsString();
        assertEquals("ab", consumed);
        consumed = reader.consumeTo('c');
        assertEquals("cd", consumed);
        consumed = reader.consumeToEnd();
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeSubstring() {
        CharacterReader reader = new CharacterReader("ababc");
        String consumed = reader.consumeTo("ab");
        assertEquals("ab", consumed);
        consumed = reader.consumeTo("bc");
        assertEquals("bc", consumed);
        consumed = reader.consumeToEnd();
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeAny() {
        CharacterReader reader = new CharacterReader("abacaba");
        String consumed = reader.consumeToAny('a', 'c');
        assertEquals("aba", consumed);
        consumed = reader.consumeToAny('b');
        assertEquals("b", consumed);
        consumed = reader.consumeToEnd();
        assertEquals("", consumed);
    }

    @Test
    public void testMatchSingleCharacter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("ef"));
    }

    @Test
    public void testMatchIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCdEf");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testMatchAny() {
        CharacterReader reader = new CharacterReader("abacaba");
        assertTrue(reader.matchesAny('a', 'c'));
        assertFalse(reader.matchesAny('b', 'x'));
    }

    @Test
    public void testMatchLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        reader.advance();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchDigit() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("AbCdEfGh");
        String consumed = reader.consumeLetterSequence();
        assertEquals("AbCd", consumed);
        reader.unconsume();
        consumed = reader.consumeLetterSequence();
        assertEquals("CdEf", consumed);
        reader.unconsume();
        reader.unconsume();
        consumed = reader.consumeLetterSequence();
        assertEquals("Gh", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1230xABF0xED");
        String consumed = reader.consumeHexSequence();
        assertEquals("1230", consumed);
        reader.unconsume();
        consumed = reader.consumeHexSequence();
        assertEquals("ABF0", consumed);
        reader.unconsume();
        reader.unconsume();
        reader.unconsume();
        consumed = reader.consumeHexSequence();
        assertEquals("ED", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123456");
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
        reader.unconsume();
        consumed = reader.consumeDigitSequence();
        assertEquals("456", consumed);
    }
}