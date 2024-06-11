package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        assertTrue(reader1.isEmpty());

        CharacterReader reader2 = new CharacterReader("abc");
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader1.current());

        CharacterReader reader2 = new CharacterReader("abc");
        assertEquals('a', reader2.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('b', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        assertEquals('b', reader.consume());
        reader.rewindToMark();
        assertEquals('a', reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf_char() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('d'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(0, reader.nextIndexOf("a"));
        assertEquals(-1, reader.nextIndexOf("d"));
    }

    @Test
    public void testConsumeTo_char() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeTo('b'));
        assertEquals("b", reader.consumeTo('c'));
        assertEquals("", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeTo_String() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeTo("b"));
        assertEquals("b", reader.consumeTo("c"));
        assertEquals("", reader.consumeTo("d"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeToAny('b', 'c'));
        assertEquals("b", reader.consumeToAny('c'));
        assertEquals("", reader.consumeToAny('d'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
        assertFalse(reader.matches('d'));
    }

    @Test
    public void testMatches_String() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("bc"));
        assertFalse(reader.matches("de"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("Bc"));
        assertFalse(reader.matchesIgnoreCase("De"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('b', 'c'));
        assertFalse(reader.matchesAny('d', 'e'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc0");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("ab"));
        assertFalse(reader.matchConsume("bc"));
        assertFalse(reader.matchConsume("de"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        assertFalse(reader.matchConsumeIgnoreCase("Bc"));
        assertFalse(reader.matchConsumeIgnoreCase("De"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("ab");
        assertTrue(reader.containsIgnoreCase("Ab"));
        assertFalse(reader.containsIgnoreCase("Cd"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());
        reader.consume();
        assertEquals("bc", reader.toString());
    }
}