package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader_emptyString() {
        CharacterReader reader = new CharacterReader("");

        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
        assertEquals(-1, reader.consume());
    }

    @Test
    public void testCharacterReader_singleCharacter() {
        CharacterReader reader = new CharacterReader("A");

        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReader_nullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testCharacterReader_consumeAsString() {
        CharacterReader reader = new CharacterReader("Hello World!");

        assertEquals("Hello", reader.consumeAsString());
        assertEquals('W', reader.current());
    }

    @Test
    public void testCharacterReader_consumeTo() {
        CharacterReader reader = new CharacterReader("Hello World!");

        assertEquals("World", reader.consumeTo('d'));
        assertEquals('!', reader.current());
    }

    @Test
    public void testCharacterReader_consumeTo_notFound() {
        CharacterReader reader = new CharacterReader("Hello World!");

        assertEquals("World!", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_consumeToAny() {
        CharacterReader reader = new CharacterReader("abcd1234abAB");

        assertEquals("cd1", reader.consumeToAny('c', 'd'));
        assertEquals('1', reader.current());
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World!");

        assertEquals("World!", reader.consumeToEnd());
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReader_consumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello World!");

        assertEquals("Hello", reader.consumeLetterSequence());
        assertEquals('W', reader.current());
    }

    @Test
    public void testCharacterReader_consumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1a 0X2b 0X3C");

        assertEquals("1a", reader.consumeHexSequence());
        assertEquals(' ', reader.current());

        reader.unconsume();

        assertEquals("2b", reader.consumeHexSequence());
        assertEquals(' ', reader.current());

        reader.unconsume();

        assertEquals("3C", reader.consumeHexSequence());
        assertEquals(' ', reader.current());
    }

    @Test
    public void testCharacterReader_consumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123 456 789");

        assertEquals("23", reader.consumeDigitSequence());
        assertEquals(' ', reader.current());

        reader.unconsume();

        assertEquals("456", reader.consumeDigitSequence());
        assertEquals(' ', reader.current());

        reader.unconsume();

        assertEquals("789", reader.consumeDigitSequence());
        assertEquals(' ', reader.current());
    }

    @Test
    public void testCharacterReader_matches() {
        CharacterReader reader = new CharacterReader("Hello World!");

        assertTrue(reader.matches('H'));
        assertTrue(reader.matches("He"));
        assertFalse(reader.matches('!'));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World!");

        assertTrue(reader.matchesIgnoreCase("He"));
        assertTrue(reader.matchesIgnoreCase("hl"));
        assertFalse(reader.matchesIgnoreCase("!"));
    }

    @Test
    public void testCharacterReader_matchesAny() {
        CharacterReader reader = new CharacterReader("Hello World!");

        assertTrue(reader.matchesAny('H', 'e'));
        assertFalse(reader.matchesAny('!'));
    }

    @Test
    public void testCharacterReader_containsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test Title</title>");

        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("/head"));
    }
}