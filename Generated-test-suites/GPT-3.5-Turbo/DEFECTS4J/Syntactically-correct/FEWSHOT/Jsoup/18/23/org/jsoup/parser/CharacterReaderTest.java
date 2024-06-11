package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuv");
        assertEquals(0, reader.pos());

        reader.consume();
        assertEquals(1, reader.pos());

        reader.consume();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("foo");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.current());

        reader = new CharacterReader("foo");
        assertEquals('f', reader.current());

        reader.consume();
        assertEquals('o', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("foo");
        assertEquals('f', reader.consume());
        assertTrue(reader.pos() == 1);

        assertEquals('o', reader.consume());
        assertTrue(reader.pos() == 2);

        assertEquals('o', reader.consume());
        assertTrue(reader.pos() == 3);

        assertEquals(CharacterReader.EOF, reader.consume());
        assertFalse(reader.pos() == 4);
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("foo");
        assertEquals('f', reader.consume());
        assertTrue(reader.pos() == 1);

        reader.unconsume();
        assertTrue(reader.pos() == 0);
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("foo");
        reader.advance();
        assertTrue(reader.pos() == 1);

        reader.advance();
        assertTrue(reader.pos() == 2);
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("foo");
        reader.mark();
        assertTrue(reader.pos() == 0);

        reader.consume();
        assertTrue(reader.pos() == 1);

        reader.rewindToMark();
        assertTrue(reader.pos() == 0);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("foo");
        assertEquals("f", reader.consumeAsString());
        assertTrue(reader.pos() == 1);

        assertEquals("o", reader.consumeAsString());
        assertTrue(reader.pos() == 2);

        assertEquals("o", reader.consumeAsString());
        assertTrue(reader.pos() == 3);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("food");
        assertEquals("foo", reader.consumeTo('d'));
        assertTrue(reader.pos() == 3);

        assertEquals("d", reader.consumeTo('o'));
        assertTrue(reader.pos() == 4);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("food");
        assertEquals("food", reader.consumeToAny('p', 'r', 'o', 'x'));
        assertTrue(reader.pos() == 4);

        reader = new CharacterReader("food");
        assertEquals("foo", reader.consumeToAny('d', 'a', 'c', 'z'));
        assertTrue(reader.pos() == 3);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("food");
        assertEquals("food", reader.consumeToEnd());
        assertTrue(reader.pos() == 4);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("foo");
        assertTrue(reader.matches('f'));

        reader.consume();
        assertTrue(reader.matches('o'));

        reader.consume();
        assertTrue(reader.matches('o'));

        reader.consume();
        assertFalse(reader.matches('o'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("foo");
        assertTrue(reader.matchesIgnoreCase("F"));

        reader.consume();
        assertTrue(reader.matchesIgnoreCase("O"));

        reader.consume();
        assertTrue(reader.matchesIgnoreCase("O"));

        reader.consume();
        assertFalse(reader.matchesIgnoreCase("O"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("foo");
        assertTrue(reader.matchesAny('f', 'o'));

        reader.consume();
        assertTrue(reader.matchesAny('f', 'o'));

        reader.consume();
        assertTrue(reader.matchesAny('f', 'o'));

        reader.consume();
        assertFalse(reader.matchesAny('f', 'o'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("foo");
        assertTrue(reader.matchesLetter());

        reader.consume();
        assertTrue(reader.matchesLetter());

        reader.consume();
        assertTrue(reader.matchesLetter());

        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("foo123");
        assertFalse(reader.matchesDigit());

        reader.consume();
        assertFalse(reader.matchesDigit());

        reader.consume();
        assertFalse(reader.matchesDigit());

        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("foo");
        assertTrue(reader.matchConsume("foo"));
        assertTrue(reader.pos() == 3);

        reader = new CharacterReader("foo");
        assertFalse(reader.matchConsume("oof"));
        assertTrue(reader.pos() == 0);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("foo");
        assertTrue(reader.matchConsumeIgnoreCase("FOO"));
        assertTrue(reader.pos() == 3);

        reader = new CharacterReader("foo");
        assertFalse(reader.matchConsumeIgnoreCase("OOF"));
        assertTrue(reader.pos() == 0);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("foo");
        assertTrue(reader.containsIgnoreCase("FOO"));

        reader = new CharacterReader("foo");
        assertFalse(reader.containsIgnoreCase("OOF"));
    }
}