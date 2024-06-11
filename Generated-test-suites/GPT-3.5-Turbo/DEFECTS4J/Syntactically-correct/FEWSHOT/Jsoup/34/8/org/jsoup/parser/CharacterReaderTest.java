package org.jsoup.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterReaderTest {

    @Test
    void pos() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    void isEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
    }

    @Test
    void current() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader1.current());

        CharacterReader reader2 = new CharacterReader("test");
        assertEquals('t', reader2.current());
    }

    @Test
    void consume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    void unconsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        reader.unconsume();
        assertEquals('e', reader.current());
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    void advance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.current());
        reader.advance();
        assertEquals('s', reader.current());
    }

    @Test
    void mark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.advance();
        assertEquals('s', reader.current());
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    void consumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("s", reader.consumeAsString());
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    void nextIndexOf_char() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals(2, reader.nextIndexOf('s'));
    }

    @Test
    void nextIndexOf_seq() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(-1, reader.nextIndexOf("aaa"));
        assertEquals(2, reader.nextIndexOf("st"));
    }

    @Test
    void consumeTo_char() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("te", reader.consumeTo('s'));
        assertEquals('s', reader.current());
        reader.consume();
        assertEquals("t", reader.consumeTo('s'));
    }

    @Test
    void consumeTo_seq() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("te", reader.consumeTo("st"));
        assertEquals('s', reader.current());
        reader.consume();
        assertEquals("t", reader.consumeTo("st"));
    }

    @Test
    void consumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeToAny('e', 's'));
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals("", reader.consumeToAny('e', 's'));
        reader.consume();
        assertEquals("st", reader.consumeToAny('e', 's'));
    }

    @Test
    void consumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    void consumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeLetterSequence());
        assertEquals('1', reader.current());
        reader.consume();
        assertEquals("123", reader.consumeLetterSequence());
    }

    @Test
    void consumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeLetterThenDigitSequence());
        assertEquals('1', reader.current());
        reader.consume();
        assertEquals("23", reader.consumeLetterThenDigitSequence());
    }

    @Test
    void consumeHexSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("", reader.consumeHexSequence());
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals("est", reader.consumeHexSequence());
    }

    @Test
    void consumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("", reader.consumeDigitSequence());
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals("est", reader.consumeDigitSequence());
    }

    @Test
    void matches_char() {
        CharacterReader reader = new CharacterReader("test123");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }

    @Test
    void matches_seq() {
        CharacterReader reader = new CharacterReader("test123");
        assertTrue(reader.matches("te"));
        assertFalse(reader.matches("es"));
    }

    @Test
    void matchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test123");
        assertTrue(reader.matchesIgnoreCase("T"));
        assertTrue(reader.matchesIgnoreCase("TE"));
        assertFalse(reader.matchesIgnoreCase("s"));
    }

    @Test
    void matchesAny() {
        CharacterReader reader = new CharacterReader("test123");
        assertTrue(reader.matchesAny('t', 'e'));
        assertFalse(reader.matchesAny('s', '1'));
    }

    @Test
    void matchesLetter() {
        CharacterReader reader = new CharacterReader("test123");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    void matchesDigit() {
        CharacterReader reader = new CharacterReader("test123");
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    void matchConsume_seq() {
        CharacterReader reader = new CharacterReader("test123");
        assertTrue(reader.matchConsume("test"));
        assertFalse(reader.matchConsume("123"));
    }

    @Test
    void matchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test123");
        assertTrue(reader.matchConsumeIgnoreCase("TEST"));
        assertFalse(reader.matchConsumeIgnoreCase("123"));
    }

    @Test
    void containsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test123");
        assertTrue(reader.containsIgnoreCase("TEST"));
        assertFalse(reader.containsIgnoreCase("1234"));
    }

    @Test
    void testToString() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test123", reader.toString());
    }
}