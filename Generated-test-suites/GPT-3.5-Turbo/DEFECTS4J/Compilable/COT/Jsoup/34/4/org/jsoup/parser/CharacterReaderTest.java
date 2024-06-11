package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Test
    public void testPos() {
        reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        reader = new CharacterReader("test");
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader = new CharacterReader("test");
        reader.consume();
        assertEquals('t', reader.current());
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        reader = new CharacterReader("test");
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        reader = new CharacterReader("test");
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfSequence() {
        reader = new CharacterReader("Hello, world!");
        assertEquals(7, reader.nextIndexOf("world"));
        assertEquals(-1, reader.nextIndexOf("Java"));
    }

    @Test
    public void testConsumeTo() {
        reader = new CharacterReader("Hello, world!");
        assertEquals("Hello, ", reader.consumeTo(","));
        assertEquals("world", reader.consumeTo(" "));
    }

    @Test
    public void testConsumeToAny() {
        reader = new CharacterReader("123abc789");
        assertEquals("123", reader.consumeToAny('a', 'b', 'c'));
        assertEquals("abc", reader.consumeToAny('7', '8', '9'));
    }

    @Test
    public void testConsumeToEnd() {
        reader = new CharacterReader("Hello, world!");
        assertEquals("Hello, world!", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        reader = new CharacterReader("Hello123");
        assertEquals("Hello", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        reader = new CharacterReader("Hello123");
        assertEquals("Hello", reader.consumeLetterThenDigitSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("1F3A9");
        assertEquals("1F3A9", reader.consumeHexSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeDigitSequence() {
        reader = new CharacterReader("123");
        assertEquals("123", reader.consumeDigitSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches() {
        reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesSequence() {
        reader = new CharacterReader("Hello, world!");
        assertTrue(reader.matches("Hello"));
        assertFalse(reader.matches("Java"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        reader = new CharacterReader("TACO");
        assertTrue(reader.matchesIgnoreCase("taco"));
        assertFalse(reader.matchesIgnoreCase("burrito"));
    }

    @Test
    public void testMatchesAny() {
        reader = new CharacterReader("123");
        assertTrue(reader.matchesAny('1', '2', '3'));
        assertFalse(reader.matchesAny('4', '5', '6'));
    }

    @Test
    public void testMatchesLetter() {
        reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("te"));
        assertFalse(reader.matchConsume("es"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        reader = new CharacterReader("tEsT");
        assertTrue(reader.matchConsumeIgnoreCase("TeSt"));
        assertFalse(reader.matchConsumeIgnoreCase("Java"));
    }

    @Test
    public void testContainsIgnoreCase() {
        reader = new CharacterReader("Hello, world!");
        assertTrue(reader.containsIgnoreCase("hello"));
        assertFalse(reader.containsIgnoreCase("java"));
    }

    @Test
    public void testToString() {
        reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}