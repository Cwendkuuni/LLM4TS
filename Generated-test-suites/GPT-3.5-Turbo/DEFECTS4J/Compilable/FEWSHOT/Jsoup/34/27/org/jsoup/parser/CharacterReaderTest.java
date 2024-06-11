package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
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
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        assertEquals(1, reader.pos());
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        reader.mark();
        reader.advance();
        assertEquals(2, reader.pos());
        reader.rewindToMark();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(1, reader.nextIndexOf("es"));
        assertEquals(-1, reader.nextIndexOf("ex"));
    }

    @Test
    public void testConsumeToChar() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("t", reader.consumeTo('e'));
        assertEquals("e", reader.consumeTo('s'));
    }

    @Test
    public void testConsumeToString() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("t", reader.consumeTo("es"));
        assertEquals("e", reader.consumeTo("st"));
    }

    @Test
    public void testConsumeToAny() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("te", reader.consumeToAny('t'));
        assertEquals("t", reader.consumeToAny('e'));
    }

    @Test
    public void testConsumeToEnd() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "test123";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "test123";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "ABC123";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("ABC123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "123test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesString() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches("te"));
        assertFalse(reader.matches("ex"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("TE"));
        assertFalse(reader.matchesIgnoreCase("EX"));
    }

    @Test
    public void testMatchesAny() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAny('t', 'e'));
        assertFalse(reader.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "123test";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matchesLetter());
        reader.consumeToEnd();
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "123test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesDigit());
        reader.consumeToEnd();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String input = "test123";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsume("test"));
        assertFalse(reader.matchConsume("abc"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "test123";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsumeIgnoreCase("TEST"));
        assertFalse(reader.matchConsumeIgnoreCase("ABC"));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "Test123";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("te"));
        assertTrue(reader.containsIgnoreCase("ST"));
        assertTrue(reader.containsIgnoreCase("123"));
        assertFalse(reader.containsIgnoreCase("ab"));
    }

    @Test
    public void testToString() {
        String input = "Test123";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("Test123", reader.toString());
        reader.consume();
        assertEquals("est123", reader.toString());
    }
}