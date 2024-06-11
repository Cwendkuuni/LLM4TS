package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_Constructor() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals(5, reader.pos());
        assertEquals("Hello World!".substring(5), reader.toString());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("Hello World!");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrentChar() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        // consume all characters
        for (int i = 0; i < reader.length(); i++) {
            reader.consume();
        }
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello World!");
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consumeTo('l');
        assertEquals("ello", reader.toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World!");
        String consumed = reader.consumeToEnd();
        assertEquals("World!", consumed);
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello World!");
        String consumed = reader.consumeTo('l');
        assertEquals("ello", consumed);
        assertEquals('W', reader.current());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("Hello World!");
        String consumed = reader.consumeTo("ll");
        assertEquals("llo", consumed);
        assertEquals('W', reader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello World!");
        String consumed = reader.consumeToAny('l', 'o');
        assertEquals("llo", consumed);
        assertEquals('W', reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        String consumed = reader.consumeLetterSequence();
        assertEquals("abc", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x2A");
        String consumed = reader.consumeHexSequence();
        assertEquals("2A", consumed);
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123");
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMatches_char() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matches('H'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('!'));
        assertFalse(reader.matches(CharacterReader.EOF));
    }

    @Test
    public void testMatches_String() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matches("Hello"));
        assertTrue(reader.matches("World!"));
        assertFalse(reader.matches("Java"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matchesIgnoreCase("HELLO"));
        assertTrue(reader.matchesIgnoreCase("world!"));
        assertFalse(reader.matchesIgnoreCase("Java"));
    }

    @Test
    public void testMatchesAny_charArray() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matchesAny('l', 'o'));
        assertTrue(reader.matchesAny('H', 'e'));
        assertFalse(reader.matchesAny('J', 'a'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Hello World!");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }
}