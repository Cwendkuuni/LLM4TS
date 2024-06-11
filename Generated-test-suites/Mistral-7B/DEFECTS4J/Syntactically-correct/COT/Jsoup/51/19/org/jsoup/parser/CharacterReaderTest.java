package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals("TestString", reader.input);
        assertEquals(14, reader.length);
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.pos = 2;
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("TestString");
        assertFalse(reader.isEmpty());

        reader.pos = 14;
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.consume();
        assertEquals('e', reader.current());

        reader.consume();
        assertEquals('s', reader.current());

        reader.consumeToEnd();
        assertEquals(0, reader.length());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.advance();
        reader.advance();
        assertEquals('s', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.pos = 2;
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals("Test", reader.consumeAsString());
        reader.consume();
        assertEquals("Test", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals(3, reader.nextIndexOf('s'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals("es", reader.consumeTo('s'));
        assertEquals("TestString", reader.toString());

        reader.pos = 5;
        assertEquals("ring", reader.consumeTo(5, "ring"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals("es", reader.consumeToAny('s'));
        assertEquals("TestString", reader.toString());

        reader.pos = 3;
        assertEquals("test", reader.consumeToAny("es", "test"));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals("es", reader.consumeToAnySorted('s'));
        assertEquals("TestString", reader.toString());

        reader.pos = 6;
        assertEquals("string", reader.consumeToAnySorted("es", "string"));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("Test&String");
        assertEquals("Test&", reader.consumeData());
        assertEquals("String", reader.toString());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<title>Test</title>");
        assertEquals("title", reader.consumeTagName());
        assertEquals("</title>", reader.toString());
    }

    @Test(expected = AssertionError.class)
    public void testConsumeToEndWithEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        reader.consumeToEnd();
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals("Test", reader.consumeLetterSequence());
        assertEquals("String", reader.toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("Test123String");
        assertEquals("Test123", reader.consumeLetterThenDigitSequence());
        assertEquals("String", reader.toString());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("Test#ff0000String");
        assertEquals("#ff0000", reader.consumeHexSequence());
        assertEquals("TestString", reader.toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("Test123String");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("TestString", reader.toString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matches('T'));
        assertTrue(reader.matches("es"));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matchesIgnoreCase("es"));
        assertFalse(reader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matchesAny('s', 'e'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matchesAnySorted('s', 'e'));
        assertFalse(reader.matchesAnySorted('z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Test123String");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matchConsume("es"));
        assertEquals("String", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matchConsumeIgnoreCase("es"));
        assertEquals("String", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><title>Test</title></html>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/TITLE"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCacheStringHashCollision() {
        CharacterReader reader = new CharacterReader("TestTestTestTest");
        reader.cacheString(0, 4);
        reader.cacheString(0, 4);
    }
}