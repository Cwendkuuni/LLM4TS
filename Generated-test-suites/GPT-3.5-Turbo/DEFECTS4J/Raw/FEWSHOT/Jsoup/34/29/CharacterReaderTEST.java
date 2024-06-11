package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        // Test when the position is at the start
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());

        // Test when the position is in the middle
        reader.consume();
        assertEquals(1, reader.pos());

        // Test when the position is at the end
        while (!reader.isEmpty()) {
            reader.consume();
        }
        assertEquals(4, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test when the input string is empty
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        // Test when the input string is not empty
        reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test when the input string is empty
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.current());

        // Test when the input string is not empty
        reader = new CharacterReader("test");
        assertEquals('t', reader.current());

        // Test when the position is at the end
        while (!reader.isEmpty()) {
            reader.consume();
        }
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        // Test consuming characters until EOF
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        // Test unconsuming characters
        CharacterReader reader = new CharacterReader("test");
        char firstChar = reader.consume();
        reader.unconsume();
        assertEquals(firstChar, reader.consume());
    }

    @Test
    public void testAdvance() {
        // Test advancing to the next character
        CharacterReader reader = new CharacterReader("test");
        assertEquals('e', reader.advance());
        assertEquals('s', reader.advance());
        assertEquals('t', reader.advance());
    }

    @Test
    public void testMarkAndRewindToMark() {
        // Test marking and rewinding to mark
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        // Test consuming a character as a string
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        // Test finding the next index of a character
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        // Test finding the next index of a character sequence
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf("es"));
        assertEquals(-1, reader.nextIndexOf("ex"));
    }

    @Test
    public void testConsumeToChar() {
        // Test consuming characters until a specific character
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo('e'));
        assertEquals("e", reader.consumeTo('s'));
        assertEquals("s", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToCharSequence() {
        // Test consuming characters until a specific character sequence
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo("es"));
        assertEquals("es", reader.consumeTo("t"));
    }

    @Test
    public void testConsumeToAny() {
        // Test consuming characters until any of the specified characters
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeToAny('e', 's'));
        assertEquals("e", reader.consumeToAny('t'));
    }

    @Test
    public void testConsumeToEnd() {
        // Test consuming characters until the end of the input
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        // Test consuming a sequence of letters
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        // Test consuming a sequence of letters followed by digits
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeLetterThenDigitSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeHexSequence() {
        // Test consuming a hexadecimal sequence
        CharacterReader reader = new CharacterReader("a4b7");
        assertEquals("a4b7", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        // Test consuming a sequence of digits
        CharacterReader reader = new CharacterReader("123test");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        // Test matching a single character
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));

        // Test matching a character sequence
        reader = new CharacterReader("test");
        assertTrue(reader.matches("test"));
        assertFalse(reader.matches("tess"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        // Test matching a character sequence ignoring case
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("TEST"));
        assertFalse(reader.matchesIgnoreCase("tess"));
    }

    @Test
    public void testMatchesAny() {
        // Test matching any of the specified characters
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('a', 't', 'c'));
        assertFalse(reader.matchesAny('a', 'b', 'c'));
    }

    @Test
    public void testMatchesLetter() {
        // Test matching a letter
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        // Test matching a digit
        CharacterReader reader = new CharacterReader("123test");
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesConsume() {
        // Test matching a character sequence and consuming it
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("test"));
        assertTrue(reader.isEmpty());

        // Test not matching a character sequence
        reader = new CharacterReader("test");
        assertFalse(reader.matchConsume("tess"));
    }

    @Test
    public void testMatchesConsumeIgnoreCase() {
        // Test matching a character sequence ignoring case and consuming it
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("TEST"));
        assertTrue(reader.isEmpty());

        // Test not matching a character sequence ignoring case
        reader = new CharacterReader("test");
        assertFalse(reader.matchConsumeIgnoreCase("tess"));
    }

    @Test
    public void testContainsIgnoreCase() {
        // Test finding a sequence in the input string ignoring case
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("TES"));
        assertFalse(reader.containsIgnoreCase("tesz"));
    }

    @Override
    public String toString() {
       return new String(input, pos, length - pos);
   }
    }