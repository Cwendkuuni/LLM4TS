package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(5, reader.length());
        assertEquals('a', reader.current());
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReader_constructor_nullInput() {
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
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        assertEquals('b', reader.current());

        reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals('d', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.advance();
        assertEquals('b', reader.current());

        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeAsString());
        assertEquals("ef", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("def", reader.consumeTo('f'));
        assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeTo_emptyString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("", reader.consumeTo(""));
    }

    @Test
    public void testConsumeTo_cannotFind() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeTo("x"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("123abcdef456");
        assertEquals("123", reader.consumeToAny('1', '2'));
        assertEquals("def", reader.consumeToAny('d', 'f'));
        assertEquals("456", reader.consumeToAny('4', '6'));
        assertEquals("", reader.consumeToAny('x', 'y'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123abcdef456");
        assertEquals("12", reader.consumeHexSequence());
        assertEquals("def", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abcdef456");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("456", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('c'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertTrue(reader.matchesIgnoreCase("c"));
        assertTrue(reader.matchesIgnoreCase("f"));
        assertFalse(reader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAny('b', 'b'));
        assertFalse(reader.matchesAny('c', 'x'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123abcdef456");
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchConsume("bc"));
        assertEquals("ef", reader.current());

        reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchConsume("AB"));
        assertEquals("CDEF", reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertEquals("CDEF", reader.current());

        reader = new CharacterReader("abcDEF");
        assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        assertEquals("cDEF", reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef<title>Hello World</title>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("HELLO"));
        assertFalse(reader.containsIgnoreCase("x"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.toString());
    }
}