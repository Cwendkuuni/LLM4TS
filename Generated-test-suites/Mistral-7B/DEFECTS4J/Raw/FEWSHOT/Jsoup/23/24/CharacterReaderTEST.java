package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("foo");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("foo");
        assertEquals(0, reader.pos());

        reader.consume();
        reader.consume();

        assertEquals(2, reader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("foo");

        assertEquals('f', reader.current());
        reader.consume();

        assertEquals('o', reader.current());
        reader.consume();

        assertEquals('o', reader.current());
        reader.consume();

        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("foo");

        reader.consume();
        assertEquals('o', reader.current());

        reader.consume();
        assertEquals('o', reader.current());

        reader.consume();
        assertEquals('\u0000', reader.current());

        reader.consume();
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("foo");

        reader.consume();
        reader.consume();

        assertEquals('o', reader.current());

        reader.unconsume();
        assertEquals('o', reader.current());

        reader.unconsume();
        assertEquals('f', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("foo");

        reader.advance();
        reader.advance();

        assertEquals('o', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("foo");
        reader.mark();

        reader.consume();
        reader.consume();

        assertEquals('o', reader.current());
        reader.rewindToMark();

        assertEquals('f', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("foo");

        String expected = "oo";
        String actual = reader.consumeAsString();

        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("foo--bar");

        String expected = "oo";
        String actual = reader.consumeTo('-');

        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("foo--bar");

        String expected = "bar";
        String actual = reader.consumeToEnd();

        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("foo--1bar--2baz--3");

        String expected = "1b";
        String actual = reader.consumeToAny('1', '2');

        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("foo--bar--baz--qux--quux");

        String expected = "foo";
        String actual = reader.consumeLetterSequence();

        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("foo--0x12--bar--0x34--baz");

        String expected = "12";
        String actual = reader.consumeHexSequence();

        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("foo--123--bar--456--baz");

        String expected = "123";
        String actual = reader.consumeDigitSequence();

        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("foo--bar--baz");

        assertTrue(reader.matches('o'));
        assertFalse(reader.matches('x'));

        reader.consume();

        assertTrue(reader.matches('o'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("foo--BAR--BAZ");

        assertTrue(reader.matchesIgnoreCase("foo"));
        assertFalse(reader.matchesIgnoreCase("BAR"));

        reader.consumeLetterSequence();

        assertTrue(reader.matchesIgnoreCase("BAR"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("foo--1bar--2baz--3qux--quux");

        assertTrue(reader.matchesAny('1', '2'));
        assertFalse(reader.matchesAny('x'));

        reader.consumeToAny(new char[]{'1'});

        assertFalse(reader.matchesAny('1'));
        assertTrue(reader.matchesAny('2'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("foo--bar--baz--qux--quux");

        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('z'));

        reader.consumeLetterSequence();

        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesLetter('z'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("foo--123--bar--456--baz");

        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('x'));

        reader.consumeDigitSequence();

        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('x'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("foo--<tag>--bar--<tag2>--baz");

        String expected = "<tag>";
        String actual = reader.matchConsume("<tag>");

        assertEquals(expected, actual);

        String expected2 = "<tag2>";
        String actual2 = reader.matchConsume("<tag2>");

        assertEquals(expected2, actual2);

        assertFalse(reader.matchConsume("<tag3>"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("foo--<TAG>--BAR--<TAG2>--BAZ");

        String expected = "<TAG>";
        String actual = reader.matchConsumeIgnoreCase("<TAG>");

        assertEquals(expected, actual);

        String expected2 = "<TAG2>";
        String actual2 = reader.matchConsumeIgnoreCase("<TAG2>");

        assertEquals(expected2, actual2);

        assertFalse(reader.matchConsumeIgnoreCase("<TAG3>"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("foo--<TAG>--<TAG2>--baz");

        String expected = "<TAG>";
        assertTrue(reader.containsIgnoreCase(expected));

        String expected2 = "<TAG2>";
        assertTrue(reader.containsIgnoreCase(expected2));

        String expected3 = "<TAG3>";
        assertFalse(reader.containsIgnoreCase(expected3));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("foo--<TAG>--<TAG2>--baz");

        String expected = "<TAG2>";
        reader.matchConsumeIgnoreCase("<TAG>");
        reader.matchConsumeIgnoreCase("<TAG2>");

        assertEquals(expected, reader.toString());
    }
}