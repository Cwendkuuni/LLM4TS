package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdef", reader.input);
        assertEquals(input.length(), reader.length);
        assertEquals(0, reader.pos);
        assertEquals(0, reader.mark);
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReader_constructor_nullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.isEmpty());
        reader.advance();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consumeTo('e');
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('f', reader.current());
        reader.consumeToEnd();
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consumeTo('e');
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('f', reader.current());
        reader.consumeToEnd();
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        reader.unconsume();
        assertEquals('a', reader.current());
        reader.consumeTo('e');
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        assertEquals('b', reader.current());
        reader.advance();
        assertEquals('c', reader.current());
        reader.consumeTo('e');
        reader.advance();
        assertEquals('f', reader.current());
        reader.consumeToEnd();
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consumeTo('e');
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeAsString());
        reader.consumeTo('c');
        assertEquals("c", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("ef", reader.consumeTo('e'));
        reader.consumeTo('c');
        assertEquals("c", reader.consumeAsString());
    }

    @Test
    public void testConsumeToStringSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeTo("ef"));
        reader.consumeTo("cd");
        assertEquals("cd", reader.consumeAsString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeToAny('e', 'f'));
        reader.consumeTo('c');
        assertEquals("c", reader.consumeAsString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeToEnd());
        reader.consumeTo('c');
        assertEquals("c", reader.consumeAsString());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeLetterSequence());
        reader.consumeTo('c');
        assertEquals("c", reader.consumeAsString());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x12345678");
        assertEquals("5678", reader.consumeHexSequence());
        reader.consumeTo('3');
        assertEquals("3", reader.consumeAsString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345678");
        assertEquals("678", reader.consumeDigitSequence());
        reader.consumeTo('3');
        assertEquals("3", reader.consumeAsString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('g'));

        reader.consumeTo('c');
        assertTrue(reader.matches('f'));
        assertFalse(reader.matches('g'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches("ef"));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("g"));

        reader.consumeTo('c');
        assertTrue(reader.matches("def"));
        assertFalse(reader.matches("g"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("g"));

        reader.consumeTo('c');
        assertTrue(reader.matchesIgnoreCase("def"));
        assertFalse(reader.matchesIgnoreCase("g"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a', 'e'));
        assertFalse(reader.matchesAny('g'));

        reader.consumeTo('c');
        assertTrue(reader.matchesAny('d', 'f'));
        assertFalse(reader.matchesAny('g'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesLetter());
        reader.consumeTo('c');
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345678");
        assertTrue(reader.matchesDigit());
        reader.consumeTo('3');
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchConsume("ef"));
        reader.consumeTo('c');
        assertTrue(reader.matchConsume("def"));
        assertFalse(reader.matchConsume("g"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.matchConsumeIgnoreCase("ef"));
        reader.consumeTo('c');
        assertTrue(reader.matchConsumeIgnoreCase("def"));
        assertFalse(reader.matchConsumeIgnoreCase("g"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef<title>My Title</title>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("MY TITLE"));
        assertFalse(reader.containsIgnoreCase("g"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.toString());
        reader.consumeTo('c');
        assertEquals("c", reader.toString());
    }
}