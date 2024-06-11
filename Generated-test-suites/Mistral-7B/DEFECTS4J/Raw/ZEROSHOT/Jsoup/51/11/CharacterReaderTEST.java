package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * CharacterReaderTest class to test CharacterReader methods
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader reader = new CharacterReader("a");
        assertFalse(reader.isEmpty());
        assertEquals("a", reader.toString());
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('', reader.current().toString());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
        assertEquals("abc", reader.toString());
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals('', reader.current().toString());
        assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOfSingleChar() {
        CharacterReader reader = new CharacterReader("abcabc");
        int offset = reader.nextIndexOf('a');
        assertEquals(0, offset);
        offset = reader.nextIndexOf('b');
        assertEquals(1, offset);
        offset = reader.nextIndexOf('c');
        assertEquals(2, offset);
        offset = reader.nextIndexOf('d');
        assertEquals(-1, offset);
    }

    @Test
    public void testCharacterReaderNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("abcabc");
        String seq = "bc";
        int offset = reader.nextIndexOf(seq);
        assertEquals(1, offset);
        seq = "ab";
        offset = reader.nextIndexOf(seq);
        assertEquals(0, offset);
        seq = "abcd";
        offset = reader.nextIndexOf(seq);
        assertEquals(-1, offset);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefg");;
        String result = reader.consumeTo('d');
        assertEquals("abc", result);
        result = reader.consumeTo('f');
        assertEquals("abcdef", result);
        result = reader.consumeTo('g');
        assertEquals("abcdefg", result);
        result = reader.consumeTo('h');
        assertEquals("", result);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        String result = reader.consumeToAny('a', 'b');
        assertEquals("a", result);
        result = reader.consumeToAny('b', 'c');
        assertEquals("b", result);
        result = reader.consumeToAny('c', 'd');
        assertEquals("c", result);
        result = reader.consumeToAny('x', 'z');
        assertEquals("", result);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcdefg");
        char[] chars = {'b', 'c', 'a'};
        String result = reader.consumeToAnySorted(chars);
        assertEquals("b", result);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader("&lt;script&gt;");
        String result = reader.consumeData();
        assertEquals("&lt;script", result);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader("<title>Test</title>");
        String result = reader.consumeTagName();
        assertEquals("title", result);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdefg");
        String result = reader.consumeToEnd();
        assertEquals("abcdefg", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd1234efg");
        String result = reader.consumeLetterSequence();
        assertEquals("abcd", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd1234efg");
        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("abcd1234", result);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&x31;&x32;&x33;");
        String result = reader.consumeHexSequence();
        assertEquals("123", result);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567");
        String result = reader.consumeDigitSequence();
        assertEquals("1234567", result);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertFalse(reader.matches('b'));
        assertFalse(reader.matches("b"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("B"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertTrue(reader.matchesAny("a", "b", "c"));
        assertFalse(reader.matchesAny('d', 'e', 'f'));
        assertFalse(reader.matchesAny("d", "e", "f"));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abc");
        char[] chars = {'a', 'b', 'c'};
        assertTrue(reader.matchesAnySorted(chars));
        assertFalse(reader.matchesAnySorted(Arrays.copyOfRange(chars, 1, chars.length)));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('1'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("a"));
        assertEquals('a', reader.current());
        reader.consume();
        assertFalse(reader.matchConsume("a"));
        assertTrue(reader.matchConsume("b"));
        assertEquals('b', reader.current());
        reader.consume();
        assertFalse(reader.matchConsume("b"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertEquals('a', reader.current());
        reader.consume();
        assertTrue(reader.matchConsumeIgnoreCase("b"));
        assertEquals('b', reader.current());
        reader.consume();
        assertFalse(reader.matchConsumeIgnoreCase("b"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertTrue(reader.containsIgnoreCase("/title/"));
        assertTrue(reader.containsIgnoreCase("/TITLE/"));
        assertFalse(reader.containsIgnoreCase("/style/"));
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());
    }
}