package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.emptyString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testNewInstance() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals(5, reader.length());
        assertEquals('1', reader.current());
        assertEquals('1', reader.consume());
        assertEquals(4, reader.length());
    }

    @Test(expected = NullPointerException.class)
    public void testNewInstanceNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("12345");
        assertFalse(reader.isEmpty());

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals(0, reader.pos());

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(5, reader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals('1', reader.current());

        reader.consume();
        assertEquals('2', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals('1', reader.consume());
        assertEquals('2', reader.consume());
        assertEquals('3', reader.consume());
        assertEquals('4', reader.consume());
        assertEquals('5', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("12345");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('2', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("12345");
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertTrue(reader.isEmpty());
        reader.mark();
        reader.rewindToMark();
        assertEquals('1', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("12345");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('1', reader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("12345");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('1', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("foo bar baz");
        assertEquals("foo", reader.consumeAsString());
        assertEquals("bar", reader.consumeAsString());
        assertEquals("baz", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("foo bar baz qux quux");
        assertEquals("bar", reader.consumeTo('a'));
        assertEquals("baz qux quux", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToEmptyString() {
        CharacterReader reader = new CharacterReader("foo bar baz qux quux");
        assertEquals("", reader.consumeTo(emptyString()));
        assertEquals("foo bar baz", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("foo bar baz qux quux");
        assertEquals("qux quux", reader.consumeTo("xu xux"));
        assertEquals("foo bar baz", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("foo bar baz qux quux");
        assertEquals("qux", reader.consumeToAny('x'));
        assertEquals("quux", reader.consumeToAny('u'));
        assertEquals("", reader.consumeToAny('z'));
        assertEquals("quux", reader.consumeToEnd());

        reader.mark();
        reader.consumeToAny('u');
        reader.rewindToMark();
        assertEquals("qux", reader.consumeToAny('x'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("foo bar baz qux quux");
        assertEquals("qux quux", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("foo bar baz qux quux");
        assertEquals("foo", reader.consumeLetterSequence());
        assertEquals("bar", reader.consumeLetterSequence());
        assertEquals("baz", reader.consumeLetterSequence());
        assertEquals("qux", reader.consumeLetterSequence());
        assertEquals("quux", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x12 0x34 0xf0 fu00 bar");
        assertEquals("12", reader.consumeHexSequence());
        assertEquals("34", reader.consumeHexSequence());
        assertEquals("f0", reader.consumeHexSequence());
        assertEquals("fu00", reader.consumeLetterSequence());
        assertEquals("bar", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123 456 789 012");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("456", reader.consumeDigitSequence());
        assertEquals("789", reader.consumeDigitSequence());
        assertEquals("012", reader.consumeDigitSequence());
        assertEquals("", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("foo bar baz qux quux");
        assertTrue(reader.matches('f'));
        assertTrue(reader.matches('o'));
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('x'));
        assertTrue(reader.matches("oo"));
        assertFalse(reader.matches("qu"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("FOO BAR BAZ QUX QUUX");
        assertTrue(reader.matchesIgnoreCase("oo"));
        assertFalse(reader.matchesIgnoreCase("qu"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("foo bar baz qux quux");
        assertTrue(reader.matchesAny('o'));
        assertTrue(reader.matchesAny('a'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("foo bar baz qux quux");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123 456 789 012");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("foo bar baz qux quux");
        assertTrue(reader.matchConsume("oo"));
        assertEquals("bar", reader.consumeToEnd());

        reader = new CharacterReader("FOO BAR BAZ QUX QUUX");
        assertTrue(reader.matchConsumeIgnoreCase("oo"));
        assertEquals("BAR", reader.consumeToEnd());

        reader = new CharacterReader("foo bar baz qux quux");
        assertFalse(reader.matchConsume("qu"));
        assertEquals("quux", reader.consumeToEnd());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Java Parser</title></head><body>...");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/head"));
        assertTrue(reader.containsIgnoreCase("/body"));
        assertFalse(reader.containsIgnoreCase("/footer"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("foo bar baz qux quux");
        assertEquals("quux", reader.toString());
    }
}