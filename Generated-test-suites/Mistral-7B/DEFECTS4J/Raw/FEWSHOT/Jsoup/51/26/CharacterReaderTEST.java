package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input.toCharArray(), reader.input);
        assertEquals(input.length(), reader.length);
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReader_constructor_nullInput() {
        CharacterReader reader = new CharacterReader(null);
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
        reader.consume();
        reader.consume();
        assertEquals(2, reader.pos());
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        assertEquals('b', reader.current());
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("ab", reader.consumeAsString());
        assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testNextIndexOf_char() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('x'));
        reader.consume();
        reader.consume();
        assertEquals(2, reader.nextIndexOf('b'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(-1, reader.nextIndexOf("cd"));
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(3, reader.nextIndexOf("bc"));
    }

    @Test
    public void testConsumeTo_char() {
        CharacterReader reader = new CharacterReader("abcabc");
        String result = reader.consumeTo('b');
        assertEquals("a", result);
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals("c", reader.consumeTo('c').toString());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeTo_CharSequence() {
        CharacterReader reader = new CharacterReader("abcabc");
        String result = reader.consumeTo("bc");
        assertEquals("ab", result);
        assertEquals("c", reader.current());
        reader.consume();
        assertEquals("d", reader.consumeTo("cd").toString());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeToAny_char() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertEquals("abcd", reader.consumeToAny(new char[] {'a', 'b', 'c', 'd'}));
        assertEquals("abcd", reader.consumeToAnySorted(new char[] {'d', 'c', 'b', 'a'}));
        assertEquals("", reader.consumeToAny(new char[] {'e'}));
        assertEquals("", reader.consumeToAny(new char[] {}));
    }

    @Test
    public void testConsumeToAnySorted_char() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertEquals("abcd", reader.consumeToAny(new char[] {'a', 'b', 'c', 'd'}));
        assertEquals("abcd", reader.consumeToAnySorted(new char[] {'d', 'c', 'b', 'a'}));
        assertEquals("", reader.consumeToAny(new char[] {'e'}));
        assertEquals("", reader.consumeToAny(new char[] {}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<foo");
        assertEquals("&<", reader.consumeData());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<title>Hello World</title>");
        assertEquals("<title>", reader.consumeTagName());
        assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("<title>Hello World</title>");
        assertEquals("Hello World</title>", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123efgh");
        assertEquals("abcd", reader.consumeLetterSequence().toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd123efgh");
        assertEquals("abcd123", reader.consumeLetterThenDigitSequence().toString());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&#x61;&#x62;&#x63;");
        assertEquals("abc", reader.consumeHexSequence().toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123456");
        assertEquals("12345", reader.consumeDigitSequence().toString());
    }

    @Test
    public void testMatches_char() {
        CharacterReader reader = new CharacterReader("abcd123efgh");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('e'));
        reader.consume();
        assertTrue(reader.matches('b'));
    }

    @Test
    public void testMatches_String() {
        CharacterReader reader = new CharacterReader("abcd123efgh");
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("ef"));
        reader.consume();
        reader.consume();
        assertTrue(reader.matches("cd"));
    }

    @Test
    public void testMatchesIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("ABCD123EfGh");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("cd"));
        assertFalse(reader.matchesIgnoreCase("EF"));
    }

    @Test
    public void testMatchesAny_char() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny(new char[] {'a', 'b', 'c', 'd', 'e', 'f'}));
        assertFalse(reader.matchesAny(new char[] {'g'}));
    }

    @Test
    public void testMatchesAnySorted_char() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAnySorted(new char[] {'f', 'e', 'd', 'c', 'b', 'a'}));
        assertFalse(reader.matchesAnySorted(new char[] {'g'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123456");
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume_String() {
        CharacterReader reader = new CharacterReader("abcd123efgh");
        String match = "ab";
        assertTrue(reader.matchConsume(match));
        assertEquals(match.length(), reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("ABCD123EfGh");
        String match = "cd";
        assertTrue(reader.matchConsumeIgnoreCase(match));
        assertEquals(match.length(), reader.pos());
    }

    @Test
    public void testContainsIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("<html><head><title>Hello World</title></head><body><h1>Hello</h1></body></html>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("head"));
        assertFalse(reader.containsIgnoreCase("footer"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
        String result = reader.cacheString(0, 5);
        assertEquals("abcd", result);
        String result2 = reader.cacheString(11, 5);
        assertEquals("abcd", result2);
        assertNotEquals(result, result2);
    }

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
        assertTrue(reader.rangeEquals(0, 5, "abcd"));
        assertFalse(reader.rangeEquals(0, 5, "adef"));
        assertFalse(reader.rangeEquals(11, 5, "efgh"));
    }

}