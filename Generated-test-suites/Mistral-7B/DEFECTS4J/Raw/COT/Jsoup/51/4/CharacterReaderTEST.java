package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("Hello World!", reader.toString());
    }

    @Test
    public void testPos() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.consume();
        int pos = reader.pos();
        assertEquals(2, pos);
    }

    @Test
    public void testIsEmpty() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.isEmpty());

        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('W', reader.current());
        reader.consumeToEnd();
        assertFalse(reader.isEmpty());
        assertNull(reader.current());
    }

    @Test
    public void testUnconsume() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        String str = reader.consumeAsString();
        assertEquals("ab", str);
        reader.consume();
        str = reader.consumeAsString();
        assertEquals("cdef", str);
        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testNextIndexOf() {
        String input = "abcdabcdabcdabcd";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(2, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('x'));
        reader.consume();
        reader.consume();
        assertEquals(3, reader.nextIndexOf('b'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        String input = "ababababababa";
        CharacterReader reader = new CharacterReader(input);
        CharSequence seq = "ab";
        assertEquals(0, reader.nextIndexOf(seq));
        CharSequence seq2 = "cd";
        assertEquals(-1, reader.nextIndexOf(seq2));
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(5, reader.nextIndexOf(seq));
    }

    @Test
    public void testConsumeTo() {
        String input = "abcdefghijklm";
        CharacterReader reader = new CharacterReader(input);
        String str = reader.consumeTo('g');
        assertEquals("abcd", str);
        reader.consume();
        str = reader.consumeTo('m');
        assertEquals("efghijklm", str);
        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testConsumeToWithCharSequence() {
        String input = "ababababababa";
        CharacterReader reader = new CharacterReader(input);
        String str = reader.consumeTo("ab");
        assertEquals("ab", str);
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        str = reader.consumeTo("ba");
        assertEquals("ba", str);
        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testConsumeToAny() {
        String input = "abcdabcdabcdabcd";
        CharacterReader reader = new CharacterReader(input);
        String str = reader.consumeToAny(new char[]{'a', 'b', 'c'});
        assertEquals("abcd", str);
        reader.consume();
        str = reader.consumeToAny(new char[]{'d', 'e'});
        assertEquals("ef", str);
        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testConsumeToAnySorted() {
        String input = "abcdabcdabcdabcd";
        CharacterReader reader = new CharacterReader(input);
        String str = reader.consumeToAnySorted(new char[]{'c', 'd'});
        assertEquals("cd", str);
        reader.consume();
        reader.consume();
        str = reader.consumeToAnySorted(new char[]{'b', 'd'});
        assertEquals("bd", str);
        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testConsumeData() {
        String input = "&<token&token&nullChar";
        CharacterReader reader = new CharacterReader(input);
        String str = reader.consumeData();
        assertEquals("&<token", str);
        reader.consume();
        str = reader.consumeData();
        assertEquals("token&token&nullChar", str);
        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testConsumeTagName() {
        String input = "<tab>\n<tag>\r<title>\f<style>";
        CharacterReader reader = new CharacterReader(input);
        String str = reader.consumeTagName();
        assertEquals("<tab>", str);
        reader.consume();
        str = reader.consumeTagName();
        assertEquals("<tag>", str);
        reader.consume();
        reader.consume();
        str = reader.consumeTagName();
        assertEquals("<title>", str);
        reader.consume();
        str = reader.consumeTagName();
        assertEquals("<style>", str);
        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(input);
        String str = reader.consumeToEnd();
        assertEquals("Hello World!", str);
        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        String str = reader.consumeLetterSequence();
        assertEquals("abcdef", str);
        reader.consume();
        str = reader.consumeLetterSequence();
        assertEquals("ghijklmnopqrstuvwxyz", str);
        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "abc123def456ghi789jkl";
        CharacterReader reader = new CharacterReader(input);
        String str = reader.consumeLetterThenDigitSequence();
        assertEquals("abc123", str);
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        str = reader.consumeLetterThenDigitSequence();
        assertEquals("def456", str);
        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEF";
        CharacterReader reader = new CharacterReader(input);
        String str = reader.consumeHexSequence();
        assertEquals("abcdef", str);
        reader.consume();
        str = reader.consumeHexSequence();
        assertEquals("ghijklmnopqrstuvwxyzABCDEF", str);
        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "012345678901234567890123456789";
        CharacterReader reader = new CharacterReader(input);
        String str = reader.consumeDigitSequence();
        assertEquals("012345678901234567890123456789", str);
        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testMatches() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('g'));
        reader.consume();
        assertFalse(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('g'));
        reader.consumeToEnd();
        assertFalse(reader.matches('a'));
        assertFalse(reader.matches('e'));
        assertFalse(reader.matches('g'));
    }

    @Test
    public void testMatchesWithString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));
        reader.consume();
        reader.consume();
        assertTrue(reader.matches("ef"));
        reader.consumeToEnd();
        assertFalse(reader.matches("ab"));
        assertFalse(reader.matches("cd"));
        assertFalse(reader.matches("ef"));
    }

    @Test
    public void testMatchesIgnoreCaseWithString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertFalse(reader.matchesIgnoreCase("Gh"));
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesIgnoreCase("ef"));
        reader.consumeToEnd();
        assertFalse(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("Gh"));
        assertFalse(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testMatchesAny() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAny('a', 'b', 'c', 'e', 'f'));
        assertFalse(reader.matchesAny('g'));
        reader.consume();
        assertFalse(reader.matchesAny('a', 'b', 'c', 'e', 'f'));
        reader.consume();
        assertFalse(reader.matchesAny('g'));
        reader.consumeToEnd();
        assertFalse(reader.matchesAny('a', 'b', 'c', 'e', 'f'));
        assertFalse(reader.matchesAny('g'));
    }

    @Test
    public void testMatchesAnySorted() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAnySorted('a', 'e', 'f', 'b', 'c'));
        assertFalse(reader.matchesAnySorted('g'));
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesAnySorted('f', 'e', 'a', 'b', 'c'));
        reader.consumeToEnd();
        assertFalse(reader.matchesAnySorted('a', 'e', 'f', 'b', 'c'));
        assertFalse(reader.matchesAnySorted('g'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
        reader.consumeToEnd();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "012345678901234567890123456789";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertFalse(reader.matchesDigit());
        reader.consumeToEnd();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeWithString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsume("ab"));
        assertFalse(reader.matchConsume("cd"));
        reader.consume();
        reader.consume();
        assertTrue(reader.matchConsume("ef"));
        reader.consumeToEnd();
        assertFalse(reader.matchConsume("ab"));
        assertFalse(reader.matchConsume("cd"));
        assertFalse(reader.matchConsume("ef"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseWithString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        assertTrue(reader.matchConsumeIgnoreCase("ef"));
        assertFalse(reader.matchConsumeIgnoreCase("Gh"));
        reader.consume();
        reader.consume();
        assertTrue(reader.matchConsumeIgnoreCase("ef"));
        reader.consumeToEnd();
        assertFalse(reader.matchConsumeIgnoreCase("Ab"));
        assertFalse(reader.matchConsumeIgnoreCase("Gh"));
        assertFalse(reader.matchConsumeIgnoreCase("ef"));
    }

    @Test
    public void testContainsIgnoreCaseWithString() {
        String input = "<html><title>My Title</title></html>";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("TITLE"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStringCacheWithTooLongString() {
        String input = "A".repeat(1024 * 1024);
        CharacterReader reader = new CharacterReader(input);
    }
}