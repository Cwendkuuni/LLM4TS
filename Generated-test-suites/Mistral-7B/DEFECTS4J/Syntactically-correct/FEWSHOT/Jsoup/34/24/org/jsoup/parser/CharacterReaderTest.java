package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import java.util.Locale;

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
        assertEquals(0, reader.pos());
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
        assertEquals(3, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeString() {
        CharacterReader reader = new CharacterReader("ABC");
        String consumed = reader.consumeTo("BC");
        assertEquals("BC", consumed);
        assertEquals('C', reader.current());
        assertEquals(3, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader("ABC");
        String consumed = reader.consumeTo('C');
        assertEquals("C", consumed);
        assertEquals('C', reader.current());
        assertEquals(3, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeAny() {
        CharacterReader reader = new CharacterReader("ABC");
        String consumed = reader.consumeToAny('A', 'B');
        assertEquals("A", consumed);
        assertEquals('A', reader.current());
        assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        String consumed = reader.consumeLetterSequence();
        assertEquals("ABC", consumed);
        assertEquals('C', reader.current());
        assertEquals(4, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("ABC123", consumed);
        assertEquals(7, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABC#dEf");
        String consumed = reader.consumeHexSequence();
        assertEquals("#dE", consumed);
        assertEquals('f', reader.current());
        assertEquals(6, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
        assertEquals('C', reader.current());
        assertEquals(6, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("ABC#dEf");
        assertEquals(1, reader.nextIndexOf('B'));
        assertEquals(3, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('X'));
        assertEquals(5, reader.nextIndexOf('#'));
        assertEquals(-1, reader.nextIndexOf("XY"));
    }

    @Test
    public void testCharacterReaderMarkUnconsume() {
        CharacterReader reader = new CharacterReader("ABC");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('A', reader.current());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches("A"));
        assertFalse(reader.matches('B'));
        assertFalse(reader.matches("B"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("B"));
        assertFalse(reader.matchesIgnoreCase("b"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesAny('A', 'B', 'C'));
        assertFalse(reader.matchesAny('X', 'B', 'C'));
        assertFalse(reader.matchesAny('A', 'X', 'C'));
        assertFalse(reader.matchesAny('A', 'B', 'X'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchConsume("A"));
        assertEquals('A', reader.current());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmpty());

        reader.mark();
        assertTrue(reader.matchConsume("B"));
        assertEquals('B', reader.current());
        assertEquals(2, reader.pos());
        reader.rewindToMark();
        assertEquals('A', reader.current());
        assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertEquals('A', reader.current());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmpty());

        reader.mark();
        assertTrue(reader.matchConsumeIgnoreCase("B"));
        assertEquals('B', reader.current());
        assertEquals(2, reader.pos());
        reader.rewindToMark();
        assertEquals('A', reader.current());
        assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC#dEf");
        assertTrue(reader.containsIgnoreCase("AB"));
        assertTrue(reader.containsIgnoreCase("CD"));
        assertFalse(reader.containsIgnoreCase("XY"));
        assertFalse(reader.containsIgnoreCase("ab"));
        assertFalse(reader.containsIgnoreCase("ef"));
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderNullInput() {
        new CharacterReader(null);
    }
}