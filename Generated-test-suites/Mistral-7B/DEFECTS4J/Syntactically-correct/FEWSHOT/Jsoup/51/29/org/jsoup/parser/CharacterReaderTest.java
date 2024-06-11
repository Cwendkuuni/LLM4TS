package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input.toCharArray(), reader.input);
        assertEquals(input.length(), reader.length);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        reader.consume();
        assertEquals('d', reader.current());
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals('d', reader.current());
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals('d', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bcde", reader.consumeAsString());
        reader.consume();
        reader.consume();
        assertEquals("ef", reader.consumeAsString());
        reader.consumeToEnd();
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(5, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('g'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(5, reader.nextIndexOf("ef"));
        assertEquals(-1, reader.nextIndexOf("gh"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertEquals("bcdef", reader.consumeTo('f'));
        assertEquals("abcdefabcde", reader.toString());
    }

    @Test
    public void testConsumeTo_String() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertEquals("bcdef", reader.consumeTo("ef"));
        assertEquals("abcde", reader.toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdeabcdeabcde");
        assertEquals("bcde", reader.consumeToAny('b', 'c', 'd'));
        assertEquals("abcdeabcdeabcde", reader.toString());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcdeabcdeabcde");
        assertEquals("bcde", reader.consumeToAnySorted('c', 'b', 'd'));
        assertEquals("abcdeabcdeabcde", reader.toString());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<null");
        assertEquals("&<null", reader.consumeData());
        assertEquals("", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("\t\n<title>\r<style>");
        assertEquals("\t\n<title>", reader.consumeTagName());
        assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertEquals("opqrstuvwxyz", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456ghi789");
        assertEquals("def456ghi789", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&x21&x22&x23");
        assertEquals("!\"#", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("01234567890123456789");
        assertEquals("01234567890123456789", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ef"));
        assertFalse(reader.matches('g'));
    }

    @Test
    public void testMatches_String() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches("ef"));
        assertFalse(reader.matches("gh"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertFalse(reader.matchesIgnoreCase("gh"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('g'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAnySorted('b', 'a'));
        assertFalse(reader.matchesAnySorted('g'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("01234567890123456789");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("ef", reader.matchConsume("ef"));
        assertEquals("", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("ef", reader.matchConsumeIgnoreCase("ef"));
        assertEquals("", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/H1"));
        assertFalse(reader.containsIgnoreCase("/footer"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.toString());
        reader.consume("ef");
        assertEquals("abcde", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        String str1 = reader.cacheString(0, 6);
        String str2 = reader.cacheString(0, 6);
        assertNotNull(str1);
        assertNotNull(str2);
        assertEquals(str1, str2);

        // cache collision
        String str3 = reader.cacheString(0, 6);
        assertNotSame(str1, str3);
    }
}