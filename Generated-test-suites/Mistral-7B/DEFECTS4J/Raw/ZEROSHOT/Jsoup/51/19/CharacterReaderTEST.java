package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertFalse(reader.isEmpty());
        assertEquals("a", reader.toString());
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('', reader.current().toString());
        assertEquals("a", reader.toString());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
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
        assertEquals("abc", reader.toString());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcd");
        assertFalse(reader.isEmpty());
        reader.mark();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertFalse(reader.isEmpty());
        assertEquals("abc", reader.consumeAsString());
        assertEquals('d', reader.current());
        assertEquals("def", reader.consumeToEnd().toString());
    }

    @Test
    public void testCharacterReaderNextIndexOfSingleCharacter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(3, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        CharacterReader reader = new CharacterReader("abcdefabc");
        assertEquals(7, reader.nextIndexOf("abc"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefabc");
        assertEquals("cdef", reader.consumeTo('e').toString());
        assertEquals("abc", reader.consumeTo('a').toString());
        assertEquals("", reader.consumeTo('z').toString());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        assertEquals("abcd", reader.consumeToAny('a', 'b', 'c', 'd').toString());
        assertEquals("", reader.consumeToAny('e').toString());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        assertEquals("abcd", reader.consumeToAnySorted('a', 'b', 'c', 'd').toString());
        assertEquals("", reader.consumeToAnySorted('e').toString());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader("&abc&<def>ghij");
        assertEquals("&abc&<def>", reader.consumeData().toString());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader("<h1 class=\"title\">Hello World</h1>");
        assertEquals("<h1 class=\"title\">", reader.consumeTagName().toString());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("<h1 class=\"title\">Hello World</h1>");
        assertEquals("</h1>", reader.consumeToEnd().toString());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        assertEquals("Hello", reader.consumeLetterSequence().toString());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("Hello123World");
        assertEquals("Hello123", reader.consumeLetterThenDigitSequence().toString());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&x0F&x1A");
        assertEquals("&x0F&x1A", reader.consumeHexSequence().toString());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("1234567890", reader.consumeDigitSequence().toString());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        assertTrue(reader.matches("ab"));
        assertTrue(reader.matches("cd"));
        assertFalse(reader.matches("xy"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("cd"));
        assertFalse(reader.matchesIgnoreCase("XY"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        assertTrue(reader.matchesAnySorted('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAnySorted('e'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('1'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdefabc");
        assertTrue(reader.matchConsume("abc"));
        assertTrue(reader.matchConsume("def"));
        assertFalse(reader.matchConsume("xyz"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefabc");
        assertTrue(reader.matchConsumeIgnoreCase("abc"));
        assertTrue(reader.matchConsumeIgnoreCase("def"));
        assertFalse(reader.matchConsumeIgnoreCase("XYZ"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefabc");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/style"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }
}