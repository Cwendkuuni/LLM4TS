package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderWithSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals('C', reader.current());
        assertEquals('C', reader.consume());
        assertEquals(4, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderWithMarkAndRewind() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        reader.mark();
        reader.consume();
        reader.consume();
        int markPos = reader.pos();
        reader.rewindToMark();
        assertEquals(markPos, reader.pos());
        assertEquals('A', reader.current());
        reader.consume();
        assertEquals('B', reader.current());
    }

    @Test
    public void testReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals("AB", reader.consumeAsString());
        assertEquals('C', reader.current());
        assertEquals(3, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("ABCDEFG");
        assertEquals("BC", reader.consumeTo('C').toString());
        assertEquals('D', reader.current());
        assertEquals(6, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeToWithNullCharacter() {
        CharacterReader reader = new CharacterReader("ABC");
        assertNull(reader.consumeTo((char) 0));
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeToWithString() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals("DEF", reader.consumeTo("EF").toString());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("012345");
        assertEquals("012", reader.consumeToAny('2', '4').toString());
        assertEquals('3', reader.current());
        assertEquals(5, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeToAnyWithEmptyArray() {
        CharacterReader reader = new CharacterReader("012345");
        assertEquals("", reader.consumeToAny().toString());
        assertEquals('5', reader.current());
        assertEquals(5, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("012345");
        assertEquals("234", reader.consumeToAnySorted('2', '3', '4').toString());
        assertEquals('5', reader.current());
        assertEquals(5, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeData() {
        CharacterReader reader = new CharacterReader("&<null");
        assertEquals("&<", reader.consumeData().toString());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader("<title></title>");
        assertEquals("<title", reader.consumeTagName().toString());
        assertEquals(10, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals("ABCDEF", reader.consumeToEnd().toString());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.consumeLetterSequence().toString());
        assertEquals(42, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("AB12CD3EFG4HIJ5");
        assertEquals("AB12CD3EFG4HIJ5", reader.consumeLetterThenDigitSequence().toString());
        assertEquals(27, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABC123DEF456GHI789JKL");
        assertEquals("123DEF456", reader.consumeHexSequence().toString());
        assertEquals(19, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345678901234567890123456");
        assertEquals("12345678901234567890123456", reader.consumeDigitSequence().toString());
        assertEquals(31, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderMatches() {
        CharacterReader reader = new CharacterReader("ABCDEFG");
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches("A"));
        assertFalse(reader.matches('B'));
        assertFalse(reader.matches("B"));
    }

    @Test
    public void testReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEFG");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("B"));
    }

    @Test
    public void testReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("ABCDEFG");
        assertTrue(reader.matchesAny('A', 'B', 'C'));
        assertFalse(reader.matchesAny('D', 'E', 'F'));
    }

    @Test
    public void testReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("ABCDEFG");
        assertTrue(reader.matchesAnySorted('A', 'B', 'C'));
        assertFalse(reader.matchesAnySorted('D', 'E', 'F'));
    }

    @Test
    public void testReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("ABCDEFG");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('A'));
    }

    @Test
    public void testReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("ABCDEFG");
        assertTrue(reader.matchConsume("A"));
        assertEquals('A', reader.current());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEFG");
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertEquals('A', reader.current());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>My Title</title></head><body>...");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }

    @Test
    public void testReaderToString() {
        CharacterReader reader = new CharacterReader("ABCDEFG");
        assertEquals("ABCDEFG", reader.toString());
    }
}