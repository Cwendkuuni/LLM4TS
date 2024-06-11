package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderSingleCharacterInput() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals("A", reader.consumeToEnd());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharactersInput() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals("ABCDEF", reader.consumeToEnd());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals('A', reader.consume());
        assertEquals("BCDEF", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals("BCDEF", reader.consumeTo('B').toString());
        assertEquals("CDEF", reader.consumeTo('C').toString());
        assertEquals("DEF", reader.consumeTo('D').toString());
        assertEquals("", reader.consumeTo('X').toString());
        assertEquals("", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals("AB", reader.consumeTo('B').toString());
        assertEquals("CD", reader.consumeTo('D').toString());
        assertEquals("EF", reader.consumeTo('E').toString());
        assertEquals("", reader.consumeToEnd().toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("012345ABCDEFabcdef");
        assertEquals("012345", reader.consumeToAny('0', '5').toString());
        assertEquals("ABCDEF", reader.consumeToAny('A', 'F').toString());
        assertEquals("abcdef", reader.consumeToAny('a', 'f').toString());
        assertEquals("", reader.consumeToAny('G').toString());
        assertEquals("", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("012345ABCDEFabcdef");
        assertEquals("01234", reader.consumeToAnySorted(new char[]{'0', '1', '2', '3', '4'}).toString());
        assertEquals("ABCDEF", reader.consumeToAnySorted(new char[]{'A', 'B', 'C', 'D', 'E', 'F'}).toString());
        assertEquals("abcdef", reader.consumeToAnySorted(new char[]{'a', 'b', 'c', 'd', 'e', 'f'}).toString());
        assertEquals("", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader("&amp; < &lt; <null>");
        assertEquals("&amp;", reader.consumeData());
        assertEquals("< &lt; <null>", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader("<tag name=\"Attribute\">");
        assertEquals("<tag", reader.consumeTagName());
        assertEquals("name=\"Attribute\">", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("<tag name=\"Attribute\">");
        assertEquals("Attribute>", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches("ABC"));
        assertFalse(reader.matches('X'));
        assertFalse(reader.matches("XYZ"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchConsume("AB"));
        assertEquals("CDEF", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertEquals("CDEF", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test Title</title></head><body>Test Content</body></html>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/TITLE"));
        assertTrue(reader.containsIgnoreCase("/Body"));
        assertTrue(reader.containsIgnoreCase("/BODY"));
        assertFalse(reader.containsIgnoreCase("/Footer"));
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderNullInput() {
        new CharacterReader(null);
    }
}