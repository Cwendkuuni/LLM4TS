
package org.junit.jupiter.api;

import org.junit.Test;
import static org.junit.Assert.*;

class CharacterReaderTest {

    // Test cases for isEmpty() method
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.isEmpty());

        reader.advance();
        assertFalse(reader.isEmpty());

        reader.consume();
        assertFalse(reader.isEmpty());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertTrue(reader.isEmpty());
    }

    // Test cases for consumeTo() method
    @Test
    public void testConsumeTo_WithValidInput() {
        CharacterReader reader = new CharacterReader("Hello World");
        String expected = "World";
        String actual = reader.consumeTo('d');
        assertEquals(expected, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeTo_WithInvalidInput() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consumeTo('z');
    }

    @Test
    public void testConsumeTo_WithEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        String expected = "";
        String actual = reader.consumeTo('a');
        assertEquals(expected, actual);
    }

    // Test cases for other methods
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        String expected = "abc";
        String actual = reader.consumeLetterSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x12345678");
        String expected = "1234";
        String actual = reader.consumeHexSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345678");
        String expected = "1234";
        String actual = reader.consumeDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("def"));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcDEFg");
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertFalse(reader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertTrue(reader.matchesAny('a', 'd', 'g'));
        assertFalse(reader.matchesAny('z', 'x', 'y'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345678");
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdefgabcdefg");
        String expected = "defg";
        String actual = reader.matchConsume("def");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEFGABCDEFG");
        String expected = "DEFG";
        String actual = reader.matchConsumeIgnoreCase("Def");
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello<title>World</title>");
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("world"));
        assertFalse(reader.containsIgnoreCase("Title"));
    }
}
