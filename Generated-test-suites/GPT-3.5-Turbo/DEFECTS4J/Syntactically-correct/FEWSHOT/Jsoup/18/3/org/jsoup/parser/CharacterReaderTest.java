package org.jsoup.parser;

import org.jsoup.helper.Validate;

import static org.junit.Assert.*;

class CharacterReaderTest {

    @Test
    public void testPos() {
        String input = "Lorem ipsum dolor sit amet";
        CharacterReader reader = new CharacterReader(input);

        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.advance();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.isEmpty());
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "Hello, World!";
        CharacterReader reader = new CharacterReader(input);

        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        String input = "Hello, World!";
        CharacterReader reader = new CharacterReader(input);

        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('l', reader.consume());
    }

    @Test
    public void testUnconsume() {
        String input = "Hello";
        CharacterReader reader = new CharacterReader(input);

        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        String input = "Hello";
        CharacterReader reader = new CharacterReader(input);

        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        String input = "Hello, World!";
        CharacterReader reader = new CharacterReader(input);

        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();

        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String input = "Hello";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("H", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        String input = "Hello, World!";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("Hello", reader.consumeTo(','));
        assertEquals(" World!", reader.consumeTo('!'));
    }

    @Test
    public void testConsumeToAny() {
        String input = "Hello, World!";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("Hello", reader.consumeToAny(' ', ','));
        assertEquals(", ", reader.consumeToAny('!', '?'));
    }

    @Test
    public void testConsumeToEnd() {
        String input = "Hello, World!";
        CharacterReader reader = new CharacterReader(input);

        assertEquals(input, reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "abc123";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "abc123";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        String input = "Hello";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "Hello";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesIgnoreCase("hello"));
        assertFalse(reader.matchesIgnoreCase("world"));
    }

    @Test
    public void testMatchesAny() {
        String input = "123";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesAny('1', '2'));
        assertFalse(reader.matchesAny('3', '4'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "123";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String input = "Hello";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchConsume("He"));
        assertFalse(reader.matchConsume("lo"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "Hello";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchConsumeIgnoreCase("He"));
        assertFalse(reader.matchConsumeIgnoreCase("Lo"));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "Hello, World!";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.containsIgnoreCase("hello"));
        assertTrue(reader.containsIgnoreCase("world"));
        assertFalse(reader.containsIgnoreCase("goodbye"));
    }

    @Test
    public void testToString() {
        String input = "Hello, World!";
        CharacterReader reader = new CharacterReader(input);

        assertEquals(input, reader.toString());
    }
}