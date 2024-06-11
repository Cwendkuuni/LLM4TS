package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;

import org.junit.Test;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.input);
    }

    @Test
    public void testPos() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        reader.pos = 5;
        assertEquals(5, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.isEmpty());

        String notEmptyInput = "Hello World";
        reader = new CharacterReader(notEmptyInput);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);

        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());

        reader.consume(); // consume 'e'
        reader.consume(); // consume 'l'

        assertEquals('l', reader.current());
        reader.consume(); // consume 'o'

        assertEquals(' ', reader.current()); // consume space
        assertEquals(CharacterReader.EOF, reader.current()); // consume end of string
    }

    @Test
    public void testUnconsume() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);

        reader.consume(); // consume 'H'
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);

        reader.advance(); // move to next character
        assertEquals('e', reader.current());

        reader.advance(); // move to next character
        assertEquals('l', reader.current());

        reader.advance(); // move to next character
        assertEquals('l', reader.current());

        reader.advance(); // move to next character
        assertEquals('o', reader.current());

        reader.advance(); // move to next character
        assertEquals(' ', reader.current());

        reader.advance(); // move to next character
        assertEquals('W', reader.current());
    }

    @Test
    public void testMark() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);

        reader.mark();
        reader.advance();
        reader.consume();

        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("ll", reader.consumeAsString());
        reader.advance();
        reader.advance();
        assertEquals("ld", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("ll", reader.consumeTo('l'));
        reader.advance();
        reader.advance();
        assertEquals("ld", reader.consumeTo('d'));

        reader.advance();
        reader.advance();
        assertEquals("Wr", reader.consumeTo('r'));
    }

    @Test
    public void testConsumeToEnd() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("ldldWrld", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("llowrld", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "Hello #xFFFFWorld";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("xFFFF", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "Hello 123World";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matches('o'));
        assertTrue(reader.matches("ll"));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesIgnoreCase("ll"));
        assertTrue(reader.matchesIgnoreCase("world"));
        assertFalse(reader.matchesIgnoreCase("X"));
    }

    @Test
    public void testMatchesAny() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesAny('l', 'o'));
        assertTrue(reader.matchesAny('l', 'd'));
        assertFalse(reader.matchesAny('x', 'o'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "Hello 123World";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String input = "Hello <title>World</title>";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchConsume("<title>"));
        assertEquals("World</title", reader.input.substring(reader.pos));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "Hello <TITLE>World</title>";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchConsumeIgnoreCase("<title>"));
        assertEquals("World</title", reader.input.substring(reader.pos));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "Hello <title>World</title>";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.containsIgnoreCase("<title>"));
        assertFalse(reader.containsIgnoreCase("<TITLE>"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        CharacterReader reader = new CharacterReader(null);
        Validate.notNull(reader.input);
    }
}