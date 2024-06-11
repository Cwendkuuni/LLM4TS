package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(0, reader.pos());

        reader.consume();
        assertEquals(1, reader.pos());

        reader.advance();
        assertEquals(2, reader.pos());

        reader.mark();
        assertEquals(2, reader.pos());

        reader.rewindToMark();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        assertTrue(reader1.isEmpty());

        CharacterReader reader2 = new CharacterReader("test");
        assertFalse(reader2.isEmpty());

        reader2.consume();
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader1.current());

        CharacterReader reader2 = new CharacterReader("test");
        assertEquals('t', reader2.current());

        reader2.consume();
        assertEquals('e', reader2.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");

        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");

        reader.consume();
        reader.consume();

        reader.unconsume();
        assertEquals('t', reader.current());

        reader.consume();
        assertEquals('s', reader.current());

        reader.unconsume();
        assertEquals('s', reader.current());

        reader.consume();
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");

        assertEquals("t", reader.consumeAsString());

        reader.consume();
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test string");

        assertEquals("test ", reader.consumeTo(' '));
        assertEquals("str", reader.consumeTo('i'));
        assertEquals("ing", reader.consumeTo(' '));
        assertEquals("", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test string");

        assertEquals("test", reader.consumeToAny(' ', 'i', 's'));
        assertEquals(" ", reader.consumeToAny('i', 's'));
        assertEquals("s", reader.consumeToAny('t', 'r'));
        assertEquals("", reader.consumeToAny('x'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test string");

        assertEquals("test string", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("1a2b3c4def");

        assertEquals("", reader.consumeLetterSequence());

        reader.consume();
        assertEquals("a", reader.consumeLetterSequence());

        reader.consume();
        reader.consume();
        assertEquals("b", reader.consumeLetterSequence());

        assertEquals("c", reader.consumeLetterSequence());

        assertEquals("def", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("a12b34c56d");

        assertEquals("", reader.consumeDigitSequence());

        reader.consume();
        assertEquals("12", reader.consumeDigitSequence());

        reader.consume();
        assertEquals("34", reader.consumeDigitSequence());

        assertEquals("56", reader.consumeDigitSequence());

        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");

        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('r'));

        reader.consume();
        assertFalse(reader.matches('t'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('s'));

        reader.consume();
        assertFalse(reader.matches('e'));
        assertTrue(reader.matches('s'));
        assertFalse(reader.matches('t'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");

        assertTrue(reader.matchesIgnoreCase("tEsT"));
        assertFalse(reader.matchesIgnoreCase("r"));

        reader.consume();
        assertFalse(reader.matchesIgnoreCase("TeSt"));
        assertTrue(reader.matchesIgnoreCase("EsT"));
        assertFalse(reader.matchesIgnoreCase("s"));

        reader.consume();
        assertFalse(reader.matchesIgnoreCase("esT"));
        assertTrue(reader.matchesIgnoreCase("ST"));
        assertFalse(reader.matchesIgnoreCase("T"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");

        assertTrue(reader.matchesAny('t', 'r'));
        assertFalse(reader.matchesAny('r', 's'));

        reader.consume();
        assertFalse(reader.matchesAny('t', 'r'));
        assertTrue(reader.matchesAny('e', 's'));
        assertFalse(reader.matchesAny('t'));

        reader.consume();
        assertFalse(reader.matchesAny('e', 'r'));
        assertTrue(reader.matchesAny('s', 't'));
        assertFalse(reader.matchesAny('t'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("t123");

        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader.consume();
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("t123");

        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());

        reader.consume();
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");

        assertFalse(reader.matchConsume("q"));
        assertTrue(reader.matchConsume("te"));

        assertFalse(reader.matchConsume("eSt"));
        assertTrue(reader.matchConsumeIgnoreCase("eSt"));

        assertFalse(reader.matchConsume("t"));
        assertFalse(reader.matchConsume("x"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("This is a Test");

        assertTrue(reader.containsIgnoreCase("thIS"));
        assertFalse(reader.containsIgnoreCase("abc"));
        assertFalse(reader.containsIgnoreCase("xyzTCM"));

        reader.consume();
        assertFalse(reader.containsIgnoreCase("thIS"));
        assertTrue(reader.containsIgnoreCase("TEST"));
        assertTrue(reader.containsIgnoreCase("ABC"));
        assertFalse(reader.containsIgnoreCase("XYZTCM"));
    }
}