package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderCreation() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(0, reader.pos());
        assertEquals(11, reader.length());
        assertFalse(reader.isEmpty());
        assertEquals('h', reader.current());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals('h', reader.consume());
        assertEquals('e', reader.current());
        assertEquals('l', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('w', reader.consume());
        assertEquals('r', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals(EOF, reader.current());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello", reader.consumeAsString());
        assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("ll", reader.consumeTo('l'));
        assertEquals("ld", reader.consumeTo('d'));
        assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("123abcd123efg");
        assertEquals("123", reader.consumeToAny('1', '2'));
        assertEquals("efg", reader.consumeToAny('e', 'g'));
        assertEquals("", reader.consumeToAny('z'));
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("world", reader.toString());
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader reader = new CharacterReader("hello world");
        assertFalse(reader.isEmpty());
        CharacterReader emptyReader = new CharacterReader("");
        assertTrue(emptyReader.isEmpty());
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('h', reader.current());
    }

    @Test
    public void testCharacterReaderUnconsume() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.consume();
        reader.unconsume();
        assertEquals('h', reader.current());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("bc"));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcDEFg");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("def"));
        assertFalse(reader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("123abcd123efg");
        assertTrue(reader.matchesAny('1', '2'));
        assertTrue(reader.matchesAny('e', 'g'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('2'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("123abcd123efg");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('b'));
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("defg", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123abc123def123ghi");
        assertEquals("123", reader.consumeHexSequence());
        assertEquals("def123ghi", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc123def123ghi");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("def123ghi", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello <title>My Page</title> world");
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("TITLE"));
        assertTrue(reader.containsIgnoreCase("world"));
    }
}