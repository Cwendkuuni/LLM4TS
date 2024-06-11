package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertNull(reader.current());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals('C', reader.current());
        assertEquals('C', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        reader.mark();
        assertEquals('A', reader.current());
        reader.unconsume();
        reader.advance();
        assertEquals('B', reader.current());
        reader.rewindToMark();
        assertEquals('A', reader.current());
        reader.consume();
        assertEquals('B', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(8, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(-1, reader.nextIndexOf("xy"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        assertEquals("b", reader.consumeTo('b').toString());
        assertEquals("cdabcd", reader.consumeTo('e').toString());
        assertEquals("abcd", reader.consumeToAny('a', 'b', 'c').toString());
        assertEquals("", reader.consumeToAny('x', 'y').toString());
        assertEquals("d", reader.consumeToEnd().charAt(0));
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123efghijk");
        assertEquals("abcd", reader.consumeLetterSequence().toString());
        assertEquals("efghijk", reader.consumeLetterSequence().toString());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd123efghijk");
        assertEquals("abcd123", reader.consumeLetterThenDigitSequence().toString());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcd%41%42%43efghijk");
        assertEquals("%41%42%43", reader.consumeHexSequence().toString());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd123efghijk");
        assertEquals("123", reader.consumeDigitSequence().toString());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcd123efghijk");
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches("ef"));
        assertFalse(reader.matches('x'));
        assertFalse(reader.matches("xy"));
        assertFalse(reader.matchesAny('a', 'b', 'z'));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABcd123EFGHijK");
        assertTrue(reader.matchesIgnoreCase("EF"));
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertFalse(reader.matchesIgnoreCase("XY"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchesIgnoreCase("1"));
    }

    @Test
    public void testCharacterReaderContainIgnoreCase() {
        CharacterReader reader = new CharacterReader(" <title>Test</title> <style>");
        assertTrue(reader.containsIgnoreCase("/title>"));
        assertTrue(reader.containsIgnoreCase("/style>"));
    }
}