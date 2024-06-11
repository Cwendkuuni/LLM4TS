package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderCreation() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(3, reader.length);
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("");
        assertEquals(0, reader.length);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testBasicMethods() {
        CharacterReader reader = new CharacterReader("abc");

        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(3, reader.pos());
        assertTrue(reader.isEmpty());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcd");

        assertEquals("a", reader.consumeAsString());
        assertEquals(1, reader.pos());
        assertEquals("b", reader.consumeAsString());
        assertEquals(2, reader.pos());
        assertEquals("cd", reader.consumeToEnd());
        assertEquals(4, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcd");

        assertEquals(-1, reader.nextIndexOf('e'));
        assertEquals(0, reader.nextIndexOf('a'));
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(3, reader.nextIndexOf('d'));
        reader.consume();
        reader.consume();
        assertEquals(2, reader.nextIndexOf('d'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcd");

        assertEquals("a", reader.consumeTo('b').toString());
        assertEquals("ab", reader.consumeTo('c').toString());
        assertEquals("abcd", reader.consumeToEnd().toString());

        reader = new CharacterReader("ab--cd");

        assertEquals("ab", reader.consumeTo('--').toString());
        assertEquals("ab--cd", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd");

        assertEquals("", reader.consumeToAny((char) 0).toString());
        assertEquals("a", reader.consumeToAny('a').toString());
        assertEquals("bc", reader.consumeToAny('c').toString());
        assertEquals("d", reader.consumeToAny('d').toString());
        assertEquals("abcd", reader.consumeToEnd().toString());

        reader = new CharacterReader("--abcd");

        assertEquals("--", reader.consumeToAny('--').toString());
        assertEquals("--abcd", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcd");

        assertEquals("a", reader.consumeToAnySorted('a', 'b', 'c', 'd').toString());
        assertEquals("bc", reader.consumeToAnySorted('b', 'c', 'd', 'a').toString());
        assertEquals("cd", reader.consumeToAnySorted('c', 'd', 'a', 'b').toString());
        assertEquals("abcd", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<abc");

        assertEquals("&", reader.consumeData().toString());
        assertEquals("<", reader.consume().toString());
        assertEquals("abc", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("\t<foo\nbar>");

        assertEquals("\t<", reader.consumeTagName().toString());
        assertEquals("\t<foo\n", reader.consumeToAny('\n').toString());
        assertEquals("\t<foo\nbar>", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");

        assertEquals("abc", reader.consumeToEnd().toString());
        assertEquals("", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd1234efgh");

        assertEquals("abcd", reader.consumeLetterSequence().toString());
        assertEquals("efgh", reader.consumeLetterSequence().toString());
        assertEquals("", reader.consumeLetterSequence().toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("ab12cd34ef");

        assertEquals("ab12", reader.consumeLetterThenDigitSequence().toString());
        assertEquals("cd34", reader.consumeLetterThenDigitSequence().toString());
        assertEquals("ef", reader.consumeLetterThenDigitSequence().toString());
        assertEquals("", reader.consumeLetterThenDigitSequence().toString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");

        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertFalse(reader.matches('b'));
        assertFalse(reader.matches("b"));
        assertFalse(reader.matches('\n'));
        assertFalse(reader.matches("\n"));
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("B"));
        assertFalse(reader.matchesAny('e'));
        assertFalse(reader.matchesAny('f'));
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesdsi());
        assertFalse(reader.matchesIgnoreCase("dsi"));
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());

        reader = new CharacterReader("");

        assertFalse(reader.matches('a'));
        assertFalse(reader.matches("a"));
        assertFalse(reader.matches('b'));
        assertFalse(reader.matches("b"));
        assertFalse(reader.matches('\n'));
        assertFalse(reader.matches("\n"));
        assertFalse(reader.matchesIgnoreCase("a"));
        assertFalse(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesAny('e'));
        assertFalse(reader.matchesAny('f'));
        assertFalse(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());

        reader = new CharacterReader("abcd1234efgh");

        assertFalse(reader.matches('e'));
        assertFalse(reader.matches('f'));
        assertFalse(reader.matches('i'));
        assertFalse(reader.matches('\n'));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertTrue(reader.matches('d'));
        assertTrue(reader.matches('1'));
        assertTrue(reader.matches('2'));
        assertTrue(reader.matches('3'));
        assertTrue(reader.matches('4'));
        assertTrue(reader.matches('5'));
        assertTrue(reader.matches('6'));
        assertTrue(reader.matches('7'));
        assertTrue(reader.matches('8'));
        assertTrue(reader.matches('9'));
        assertTrue(reader.matches('0'));
        assertFalse(reader.matchesAny('x'));
        assertTrue(reader.matchesAnySorted(new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcd");

        assertEquals("abcd", reader.toString());
        reader.consume();
        reader.consume();
        assertEquals("cd", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("abcdef");

        String str = reader.cacheString(0, 3);
        assertEquals("abc", str);
        assertEquals("abc", reader.stringCache[0]);

        str = reader.cacheString(3, 3);
        assertEquals("def", str);
        assertEquals("def", reader.stringCache[1]);

        str = reader.cacheString(6, 1);
        assertEquals("f", str);
        assertEquals("f", reader.stringCache[2]);

        str = reader.cacheString(7, 3);
        assertEquals("ef", str);
        assertEquals("ef", reader.stringCache[3]);

        str = reader.cacheString(10, 1);
        assertNull(str);
        assertNull(reader.stringCache[4]);
    }
}