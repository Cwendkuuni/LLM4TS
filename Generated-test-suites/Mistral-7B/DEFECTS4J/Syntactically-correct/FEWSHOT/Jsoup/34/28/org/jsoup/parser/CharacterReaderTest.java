package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertNull(reader.consume());
    }

    @Test
    public void testCharacterReaderSingleCharacterInput() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());

        reader.mark();
        reader.advance();
        assertEquals('a', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharactersInput() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertTrue(reader.isEmpty());

        reader.mark();
        reader.advance();
        assertEquals('b', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToCharacter() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeTo('c');
        assertEquals("ab", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToCharacter_AtEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeTo('d');
        assertEquals("abc", consumed);
        assertEquals(EOF, reader.current());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToCharacter_NotFound() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeTo('e');
        assertEquals("", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeTo("bc");
        assertEquals("ab", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToString_AtEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeToEnd();
        assertEquals("abcd", consumed);
        assertEquals(EOF, reader.current());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToString_SubsequenceNotFound() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeTo("ef");
        assertEquals("", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd123efg456");
        String consumed = reader.consumeToAny('1', '2', '3');
        assertEquals("123", consumed);
        assertEquals('e', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd123efg456");
        String consumed = reader.consumeToEnd();
        assertEquals("abcd123efg456", consumed);
        assertEquals(EOF, reader.current());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg456");
        String consumed = reader.consumeLetterSequence();
        assertEquals("abcd", consumed);
        assertEquals('1', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg456");
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("abcd1", consumed);
        assertEquals('2', reader.current());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcd123efg456");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches('1'));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("12"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd123efg456");
        assertTrue(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchesIgnoreCase("E"));
        assertTrue(reader.matchesIgnoreCase("1"));
        assertFalse(reader.matchesIgnoreCase("z"));
        assertFalse(reader.matchesIgnoreCase("12"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd123efg456");
        assertTrue(reader.matchesAny('a', 'e'));
        assertTrue(reader.matchesAny('1', '6'));
        assertFalse(reader.matchesAny('z', '1'));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd<title>123efg<style>456");
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("<style>"));
        assertFalse(reader.containsIgnoreCase("<body>"));
    }
}