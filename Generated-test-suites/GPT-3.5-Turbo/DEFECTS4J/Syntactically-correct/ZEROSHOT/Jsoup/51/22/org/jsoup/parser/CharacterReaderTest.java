
package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

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
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(1, reader.nextIndexOf("e"));
        assertEquals(-1, reader.nextIndexOf("a"));
    }

    @Test
    public void testConsumeTo() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("t", reader.consumeTo("e"));
        assertEquals("e", reader.consumeTo("a"));
    }

    @Test
    public void testConsumeToAny() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("t", reader.consumeToAny('e'));
        assertEquals("e", reader.consumeToAny('a'));
    }

    @Test
    public void testConsumeToAnySorted() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("t", reader.consumeToAnySorted('e'));
        assertEquals("e", reader.consumeToAnySorted('a'));
    }

    @Test
    public void testConsumeData() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("t", reader.consumeData());
        assertEquals("e", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("t", reader.consumeTagName());
        assertEquals("e", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "test1";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test1", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "test1";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test1", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "123";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("123", reader.consumeDigitSequence());
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
        assertTrue(reader.matches("test"));
        assertFalse(reader.matches("example"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("TEST"));
        assertFalse(reader.matchesIgnoreCase("EXAMPLE"));
    }

    @Test
    public void testMatchesAny() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAny('t', 'e'));
        assertFalse(reader.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesAnySorted() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAnySorted('e', 't'));
        assertFalse(reader.matchesAnySorted('a', 'b'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "test";
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
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsume("test"));
        assertFalse(reader.matchConsume("example"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsumeIgnoreCase("TEST"));
        assertFalse(reader.matchConsumeIgnoreCase("EXAMPLE"));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("TES"));
        assertFalse(reader.containsIgnoreCase("EXAMPLE"));
    }
}
