package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());

        reader.advance();
        assertEquals(1, reader.pos());

        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());

        reader.advance();
        assertEquals('b', reader.current());

        reader.consume();
        assertEquals('b', reader.current());

        reader.consume();
        assertEquals('c', reader.current());

        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());

        reader.consume();
        assertEquals('c', reader.current());

        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        assertEquals('b', reader.current());

        reader.unconsume();
        assertEquals('a', reader.current());

        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());

        reader.advance();
        assertEquals('b', reader.current());

        reader.advance();
        assertEquals('c', reader.current());

        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();

        reader.consume();
        reader.advance();
        reader.advance();

        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.mark();

        reader.rewindToMark();
        assertEquals('a', reader.current());

        reader.advance();
        assertEquals('b', reader.current());

        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());

        reader.consume();
        assertEquals("b", reader.consumeAsString());

        reader.consume();
        assertEquals("c", reader.consumeAsString());

        reader.consume();
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('x'));

        reader.consume();
        assertEquals(2, reader.nextIndexOf('c'));

        reader.consumeTo('b');
        assertEquals(-1, reader.nextIndexOf('b'));
    }

    @Test
    public void testNextIndexOfWithSeq() {
        CharacterReader reader = new CharacterReader("abcb");
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(-1, reader.nextIndexOf("ab"));

        reader.consume();
        assertEquals(3, reader.nextIndexOf("bc"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIndexOfWithNullSeq() {
        CharacterReader reader = new CharacterReader("abcb");
        reader.nextIndexOf(null);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcb");
        assertEquals("bc", reader.consumeTo('b'));
        assertEquals("c", reader.consumeTo('c'));
        assertEquals("", reader.consumeTo('d'));

        reader.consumeTo("bc");
        assertEquals("c", reader.current());
    }

    @Test
    public void testConsumeToWithEmptySeq() {
        CharacterReader reader = new CharacterReader("abcb");
        assertEquals("", reader.consumeTo(""));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcb");
        assertEquals("abcb", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("2345", reader.consumeToAny('3', '5'));
        assertEquals("5", reader.consumeToAny('5'));
        assertEquals("", reader.consumeToAny('6'));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("345", reader.consumeToAnySorted(new char[]{'3', '4', '5'}));
        assertEquals("5", reader.consumeToAnySorted(new char[]{'5'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'6'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<null");
        assertEquals("&<null", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<title>");
        assertEquals("title", reader.consumeTagName());

        reader.consume();
        assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("cd", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("ab12cd");
        assertEquals("2cd", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("d"));
        assertFalse(reader.matches('e'));

        reader.consume();
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('a'));
        assertTrue(reader.matches('d'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCd");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("D"));
        assertFalse(reader.matchesIgnoreCase("E"));

        reader.consume();
        assertTrue(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchesIgnoreCase("D"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("ab");
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAny('b', 'a'));
        assertFalse(reader.matchesAny('c'));

        reader.consume();
        assertTrue(reader.matchesAny('b', 'a'));
        assertFalse(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("ab");
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
        assertFalse(reader.matchesAnySorted(new char[]{'c'}));

        reader.consume();
        assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
        assertFalse(reader.matchesAnySorted(new char[]{'a'}));
        assertTrue(reader.matchesAnySorted(new char[]{'b'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());

        reader.consume();
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());

        reader.consume();
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchConsume("cd"));
        assertTrue(reader.matchConsume("d"));
        assertFalse(reader.matchConsume("e"));

        reader.consume();
        assertTrue(reader.matchConsume("b"));
        assertFalse(reader.matchConsume("a"));
        assertTrue(reader.matchConsume("d"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCd");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertTrue(reader.matchConsumeIgnoreCase("D"));
        assertFalse(reader.matchConsumeIgnoreCase("E"));

        reader.consume();
        assertTrue(reader.matchConsumeIgnoreCase("b"));
        assertFalse(reader.matchConsumeIgnoreCase("A"));
        assertTrue(reader.matchConsumeIgnoreCase("D"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><title>My Title</title>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("MY TITLE"));
        assertTrue(reader.containsIgnoreCase("TITLE"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("abcabc");
        String str1 = reader.cacheString(0, 3);
        String str2 = reader.cacheString(3, 3);
        String str3 = reader.cacheString(6, 3);

        assertEquals("abc", str1);
        assertEquals("abc", str2);
        assertEquals("abc", str3);

        reader.consume();
        reader.consume();
        reader.consume();

        String str4 = reader.cacheString(3, 3);
        String str5 = reader.cacheString(6, 3);

        assertEquals("abc", str4);
        assertEquals("cba", str5);
    }
}