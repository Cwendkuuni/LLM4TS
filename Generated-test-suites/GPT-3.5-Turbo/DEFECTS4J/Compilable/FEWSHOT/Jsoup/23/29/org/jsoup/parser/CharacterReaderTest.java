package org.jsoup.parser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        // Test empty input
        CharacterReader reader = new CharacterReader("");
        assertEquals(0, reader.pos());

        // Test non-empty input
        reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test empty input
        CharacterReader reader = new CharacterReader("");
        assertEquals(true, reader.isEmpty());

        // Test non-empty input
        reader = new CharacterReader("abc");
        assertEquals(false, reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test empty input
        CharacterReader reader = new CharacterReader("");
        assertEquals((char) -1, reader.current());

        // Test non-empty input
        reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsume() {
        // Test empty input
        CharacterReader reader = new CharacterReader("");
        assertEquals((char) -1, reader.consume());

        // Test non-empty input
        reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals((char) -1, reader.consume());
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
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("ab", reader.consumeTo('c'));
        assertEquals("", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeToAny('b', 'c'));
        assertEquals("", reader.consumeToAny('d'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123abc", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(true, reader.matches('a'));
        assertEquals(false, reader.matches('b'));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(true, reader.matchesAny('a', 'b'));
        assertEquals(false, reader.matchesAny('c'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(true, reader.matchesLetter());
        reader.consume(); // consume 'a'
        assertEquals(false, reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc123");
        reader.consumeToEnd(); // consume 'abc'
        assertEquals(true, reader.matchesDigit());
        reader.consume(); // consume '1'
        assertEquals(false, reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(true, reader.matchConsume("ab"));
        assertEquals(false, reader.matchConsume("bc"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(true, reader.matchConsumeIgnoreCase("AB"));
        assertEquals(false, reader.matchConsumeIgnoreCase("BC"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABC");
        assertEquals(true, reader.containsIgnoreCase("abc"));
        assertEquals(false, reader.containsIgnoreCase("def"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());
        reader.consume();
        assertEquals("bc", reader.toString());
    }
}