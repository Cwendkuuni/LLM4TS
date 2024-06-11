package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private String input;
    private CharacterReader reader;

    @Before
    public void setUp() {
        input = "abc123def";
        reader = new CharacterReader(input);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testCurrent() {
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals('b', reader.current());

        reader.consumeTo('c');
        assertEquals('c', reader.current());
        assertEquals("bc", reader.consumeAsString());
        assertEquals(4, reader.pos());

        reader.consumeToEnd();
        assertEquals("def", reader.consumeAsString());
        assertEquals(9, reader.pos());

        reader.consumeLetterSequence();
        assertEquals("ab", reader.consumeAsString());
        assertEquals(3, reader.pos());

        reader.consumeHexSequence();
        assertEquals("12", reader.consumeAsString());
        assertEquals(5, reader.pos());

        reader.consumeDigitSequence();
        assertEquals("123", reader.consumeAsString());
        assertEquals(7, reader.pos());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());

        reader.consumeTo('c');
        reader.unconsume();
        assertEquals('b', reader.current());
        assertEquals("bc", reader.consumeAsString());
        assertEquals(4, reader.pos());
    }

    @Test
    public void testMark() {
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('c', reader.current());

        reader.consumeTo('c');
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('d', reader.current());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('x'));
        assertFalse(reader.matches("ab"));

        reader.consume();
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('x'));
        assertFalse(reader.matches("ab"));

        reader.consumeTo('c');
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('x'));
        assertFalse(reader.matches("ab"));

        reader.consumeToEnd();
        assertFalse(reader.matches('x'));
        assertFalse(reader.matches("ab"));
        assertFalse(reader.matches("def"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("B"));
        assertFalse(reader.matchesIgnoreCase("X"));
        assertFalse(reader.matchesIgnoreCase("ab"));

        reader.consume();
        assertTrue(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("X"));
        assertFalse(reader.matchesIgnoreCase("ab"));

        reader.consumeTo('c');
        assertTrue(reader.matchesIgnoreCase("c"));
        assertFalse(reader.matchesIgnoreCase("X"));
        assertFalse(reader.matchesIgnoreCase("ab"));

        reader.consumeToEnd();
        assertFalse(reader.matchesIgnoreCase("X"));
        assertFalse(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("Def"));
    }

    @Test
    public void testMatchesAny() {
        assertFalse(reader.matchesAny(new char[]{'x'}));
        assertFalse(reader.matchesAny(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAny(new char[]{'a', 'b', 'c'}));
        assertTrue(reader.matchesAny(new char[]{'a'}));

        reader.consume();
        assertFalse(reader.matchesAny(new char[]{'x'}));
        assertFalse(reader.matchesAny(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAny(new char[]{'a', 'b', 'c'}));
        assertTrue(reader.matchesAny(new char[]{'b'}));

        reader.consumeTo('c');
        assertFalse(reader.matchesAny(new char[]{'x'}));
        assertFalse(reader.matchesAny(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAny(new char[]{'a', 'b', 'c'}));
        assertTrue(reader.matchesAny(new char[]{'c'}));

        reader.consumeToEnd();
        assertFalse(reader.matchesAny(new char[]{'x'}));
        assertFalse(reader.matchesAny(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAny(new char[]{'a', 'b', 'c'}));
        assertFalse(reader.matchesAny(new char[]{'e', 'f'}));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader.consume();
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader.consumeTo('c');
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader.consumeToEnd();
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());

        reader.consume();
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());

        reader.consumeTo('c');
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());

        reader.consumeToEnd();
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(reader.containsIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("ef"));

        reader.consume();
        assertFalse(reader.containsIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("ef"));

        reader.consumeTo('c');
        assertFalse(reader.containsIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("ef"));

        reader.consumeToEnd();
        assertTrue(reader.containsIgnoreCase("ef"));
        assertTrue(reader.containsIgnoreCase("def"));
        assertFalse(reader.containsIgnoreCase("ab"));
    }

    @Test
    public void testMatchConsume() {
        String seq = "ab";
        assertTrue(reader.matchConsume(seq));
        assertEquals("ab", reader.consumeAsString());

        seq = "ef";
        assertTrue(reader.matchConsume(seq));
        assertEquals("ef", reader.consumeAsString());

        seq = "xyz";
        assertFalse(reader.matchConsume(seq));

        reader.consume();
        assertTrue(reader.matchConsume(seq));
        assertEquals("ab", reader.consumeAsString());

        reader.consumeTo('c');
        assertTrue(reader.matchConsume(seq));
        assertEquals("ab", reader.consumeAsString());

        reader.consumeToEnd();
        assertFalse(reader.matchConsume(seq));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String seq = "Ab";
        assertTrue(reader.matchConsumeIgnoreCase(seq));
        assertEquals("ab", reader.consumeAsString());

        seq = "Ef";
        assertTrue(reader.matchConsumeIgnoreCase(seq));
        assertEquals("ef", reader.consumeAsString());

        seq = "XYZ";
        assertFalse(reader.matchConsumeIgnoreCase(seq));

        reader.consume();
        assertTrue(reader.matchConsumeIgnoreCase(seq));
        assertEquals("ab", reader.consumeAsString());

        reader.consumeTo('c');
        assertTrue(reader.matchConsumeIgnoreCase(seq));
        assertEquals("ab", reader.consumeAsString());

        reader.consumeToEnd();
        assertFalse(reader.matchConsumeIgnoreCase(seq));
    }

    @Test
    public void testToString() {
        assertEquals("", reader.toString());
        reader.consume();
        assertEquals("a", reader.toString());
        reader.consumeTo('c');
        assertEquals("bc", reader.toString());
        reader.consumeToEnd();
        assertEquals("def", reader.toString());
    }

    @After
    public void tearDown() {
        reader = null;
        input = null;
    }
}