package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    private final String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<null>\t\n\r\f / >";
    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader(input);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testCurrent() {
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals('b', reader.current());

        reader.consume();
        assertEquals('c', reader.current());

        reader.consumeTo('f');
        assertEquals("def", reader.consumeAsString());
        assertEquals('g', reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.pos());

        reader.advance();
        reader.advance();

        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("a", reader.consumeAsString());
        assertEquals("bc", reader.consumeTo('c').trim());
        assertEquals("def", reader.consumeTo('f').trim());
        assertEquals("", reader.consumeToEnd().trim());
        assertEquals("12345", reader.consumeTo("567890").trim());
        assertEquals("", reader.consumeToAny('!', 'z').trim());
        assertEquals("", reader.consumeToAnySorted(new char[]{'z', '!'}).trim());
        assertEquals("&<null>", reader.consumeData().trim());
        assertEquals("title", reader.consumeTagName().trim());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(12, reader.nextIndexOf('5'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("bc", reader.consumeTo('c').trim());
        assertEquals("def", reader.consumeTo('f').trim());
        assertEquals("12345", reader.consumeTo("567890").trim());
        assertEquals("", reader.consumeToAny('!', 'z').trim());
        assertEquals("", reader.consumeToAnySorted(new char[]{'z', '!'}).trim());
        assertEquals("&<null>", reader.consumeData().trim());
        assertEquals("title", reader.consumeTagName().trim());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("abc", reader.consumeToAny(new char[]{'a', 'b', 'c'}).trim());
        assertEquals("ABC", reader.consumeToAnySorted(new char[]{'A', 'B', 'C'}).trim());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<null>\t\n\r\f / >", reader.consumeToEnd().trim());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("abcd", reader.consumeLetterSequence().trim());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("ab12cd", reader.consumeLetterThenDigitSequence().trim());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("abcd12ef", reader.consumeHexSequence().trim());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("12345", reader.consumeDigitSequence().trim());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
        assertFalse(reader.matchesAnySorted(new char[]{'x', 'a'}));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('5'));
    }

    @Test
    public void testMatchConsume() {
        assertEquals("bc", reader.matchConsume("bc").trim());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertEquals("bc", reader.matchConsumeIgnoreCase("BC").trim());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("</title"));
        assertTrue(reader.containsIgnoreCase("/STYLE"));
        assertFalse(reader.containsIgnoreCase("/xyz"));
    }

    @Test
    public void testToString() {
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<null>\t\n\r\f / >", reader.toString().trim());
    }
}