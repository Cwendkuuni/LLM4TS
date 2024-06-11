package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_ValidConstruction() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
        assertEquals("abc", reader.input);
        assertEquals(3, reader.length());
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReader_NullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.advance();
        assertEquals('c', reader.current());
        reader.advance(); // EOF
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(EOF, reader.current());
        assertEquals(2, reader.pos());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());
        reader.advance(); // EOF
        assertEquals(EOF, reader.current());
        assertEquals(3, reader.pos());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("ab", reader.consumeAsString());
        assertEquals(2, reader.pos());
        reader.consume(); // c
        assertEquals("bc", reader.consumeAsString());
        assertEquals(3, reader.pos());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("de", reader.consumeTo('f'));
        assertEquals(4, reader.pos());
        assertEquals("ef", reader.consumeToEnd());
        assertEquals("ef", reader.input.substring(reader.pos(), reader.input.length()));
    }

    @Test
    public void testConsumeTo_EmptySequence() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("", reader.consumeToAny());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdeef");
        assertEquals("bc", reader.consumeToAny('c', 'f'));
        assertEquals(4, reader.pos());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
        assertEquals(EOF, reader.current());
        assertEquals(3, reader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123xyz");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("xyz", reader.input.substring(reader.pos()));
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1234abcd");
        assertEquals("1234", reader.consumeHexSequence());
        assertEquals("abcd", reader.input.substring(reader.pos()));
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345abcd");
        assertEquals("1234", reader.consumeDigitSequence());
        assertEquals("5abcd", reader.input.substring(reader.pos()));
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertTrue(reader.matchesIgnoreCase("c"));
        assertFalse(reader.matchesIgnoreCase("d"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('d', 'e'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123abc");
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("ab"));
        assertEquals(1, reader.pos());
        assertTrue(reader.matches('c'));
        reader.matchConsume("c");
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABC");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertEquals(2, reader.pos());
        assertTrue(reader.matches('C'));
        reader.matchConsumeIgnoreCase("c");
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABC");
        assertTrue(reader.containsIgnoreCase("<title"));
        assertTrue(reader.containsIgnoreCase("</title>"));
        assertTrue(reader.containsIgnoreCase("</style>"));
        assertFalse(reader.containsIgnoreCase("<head"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("bc", reader.toString());
    }
}