package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());

        reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());

        reader = new CharacterReader("");
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('x'));

        reader = new CharacterReader("abc");
        assertEquals(-1, reader.nextIndexOf('b'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("abcabca");
        assertEquals(2, reader.nextIndexOf("bc"));
        assertEquals(-1, reader.nextIndexOf("xyz"));

        reader = new CharacterReader("abc");
        assertEquals(-1, reader.nextIndexOf("bc"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals("bc", reader.consumeTo('b'));
        assertEquals("cab", reader.consumeTo("ab"));
        assertEquals("", reader.consumeTo('x'));

        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeTo('b'));
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals("bc", reader.consumeToString("b"));
        assertEquals("cab", reader.consumeToString("ab"));
        assertEquals("", reader.consumeToString("x"));

        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeToString("b"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals("bc", reader.consumeToAny('b', 'c'));
        assertEquals("cab", reader.consumeToAny("ab", 'a'));
        assertEquals("", reader.consumeToAny(new char[]{'x'}));

        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeToAny('b'));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals("bc", reader.consumeToAnySorted(new char[]{'b', 'c'}));
        assertEquals("cab", reader.consumeToAnySorted(new char[]{'a', 'b'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'x'}));

        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeToAnySorted(new char[]{'b'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<abc");
        assertEquals("&<", reader.consumeData());

        reader = new CharacterReader("");
        assertEquals("", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<title>abc");
        assertEquals("<title>", reader.consumeTagName());

        reader = new CharacterReader("");
        assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals("abc", reader.consumeToEnd());

        reader = new CharacterReader("");
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("d", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());

        reader = new CharacterReader("ab");
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("3", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());

        reader = new CharacterReader("ab");
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertFalse(reader.matches('b'));
        assertFalse(reader.matches("b"));

        reader = new CharacterReader("");
        assertFalse(reader.matches('a'));
        assertFalse(reader.matches("a"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("B"));

        reader = new CharacterReader("AbC");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertTrue(reader.matchesIgnoreCase("B"));

        reader = new CharacterReader("AbCdEf");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertTrue(reader.matchesIgnoreCase("B"));
        assertTrue(reader.matchesIgnoreCase("c"));
        assertTrue(reader.matchesIgnoreCase("C"));
        assertTrue(reader.matchesIgnoreCase("d"));
        assertTrue(reader.matchesIgnoreCase("E"));
        assertTrue(reader.matchesIgnoreCase("f"));
        assertTrue(reader.matchesIgnoreCase("F"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.matchesAny(new char[]{'a'}));
        assertFalse(reader.matchesAny(new char[]{'b'}));
        assertFalse(reader.matchesAny(new char[]{'c'}));
        assertFalse(reader.matchesAny(new char[]{'d'}));

        reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny(new char[]{'a', 'b', 'c', 'd'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));

        reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c', 'd'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader = new CharacterReader("ab");
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());

        reader = new CharacterReader("abc");
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("a"));
        assertEquals('b', reader.current());
        assertTrue(reader.matchConsume("b"));
        assertEquals('c', reader.current());
        assertFalse(reader.matchConsume("x"));
        assertEquals(CharacterReader.EOF, reader.current());

        reader = new CharacterReader("abc");
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertEquals('b', reader.current());
        assertTrue(reader.matchConsumeIgnoreCase("b"));
        assertEquals('c', reader.current());
        assertFalse(reader.matchConsumeIgnoreCase("x"));
        assertEquals(CharacterReader.EOF, reader.current());

        reader = new CharacterReader("abCdEf");
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertEquals('b', reader.current());
        assertTrue(reader.matchConsumeIgnoreCase("b"));
        assertEquals('c', reader.current());
        assertTrue(reader.matchConsumeIgnoreCase("c"));
        assertEquals('d', reader.current());
        assertTrue(reader.matchConsumeIgnoreCase("d"));
        assertTrue(reader.matchConsumeIgnoreCase("e"));
        assertTrue(reader.matchConsumeIgnoreCase("f"));
        assertTrue(reader.matchConsumeIgnoreCase("F"));
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>My Page</title></head><body>...</body></html>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/TITLE"));
        assertTrue(reader.containsIgnoreCase("/HEAD"));
        assertTrue(reader.containsIgnoreCase("/BODY"));
        assertFalse(reader.containsIgnoreCase("/FOO"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());

        reader = new CharacterReader("");
        assertEquals("", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("abcabc");
        String str = reader.cacheString(1, 3);
        assertEquals("bc", str);
        str = reader.cacheString(1, 6);
        assertEquals("cab", str);

        reader = new CharacterReader("abc");
        str = reader.cacheString(1, 1);
        assertEquals("a", str);
    }

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader("bcabc");
        assertTrue(reader.rangeEquals(1, 2, "bc"));
        assertFalse(reader.rangeEquals(1, 2, "ab"));

        reader = new CharacterReader("abc");
        assertFalse(reader.rangeEquals(1, 1, "a"));
    }
}