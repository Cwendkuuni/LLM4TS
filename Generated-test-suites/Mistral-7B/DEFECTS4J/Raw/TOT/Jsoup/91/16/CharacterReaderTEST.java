package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("Hello World!");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.advance();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testEmptyNoBufferUp() {
        reader.unconsume();
        reader.unconsume();
        reader.unconsume();
        reader.isEmptyNoBufferUp();
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        reader.advance();
        assertEquals('l', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('l', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testInvalidMark() {
        reader.mark();
        reader.unconsume();
        reader.rewindToMark();
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(5, reader.nextIndexOf(' '));
        assertEquals(-1, reader.nextIndexOf('Z'));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(6, reader.nextIndexOf("ll"));
        assertEquals(-1, reader.nextIndexOf("WW"));
    }

    @Test
    public void testConsumeTo() {
        String result = reader.consumeTo(' ');
        assertEquals("ello", result);
        reader.advance();
        reader.consumeTo('l');
        assertEquals("llo", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToSequence() {
        String result = reader.consumeTo("ll");
        assertEquals("Hello World!llo", reader.consumeToEnd());
        assertEquals("llo", result);
    }

    @Test
    public void testConsumeToAny() {
        String result = reader.consumeToAny(new char[]{'l', 'o'});
        assertEquals("llo", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        String result = reader.consumeToAnySorted(new char[]{'o', 'l'});
        assertEquals("llo", result);
    }

    @Test
    public void testConsumeData() {
        String result = reader.consumeData();
        assertEquals("Hello &lt;", result);
    }

    @Test
    public void testConsumeTagName() {
        String result = reader.consumeTagName();
        assertEquals("HelloWorld", result);
    }

    @Test
    public void testConsumeToEnd() {
        String result = reader.consumeToEnd();
        assertEquals("Hello World!", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        String result = reader.consumeLetterSequence();
        assertEquals("World", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("World1", result);
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('l'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesSequence() {
        assertTrue(reader.matches("ll"));
        assertFalse(reader.matches("WW"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("ll"));
        assertFalse(reader.matchesIgnoreCase("WW"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny(new char[]{'l', 'o'}));
        assertFalse(reader.matchesAny(new char[]{'z', 'l'}));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted(new char[]{'o', 'l'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z', 'l'}));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeSequence() {
        reader.advance();
        reader.consumeMatch("ll");
        assertEquals("llo", reader.consumeToEnd());
    }

    @Test
    public void testMatchConsumeIgnoreCaseSequence() {
        reader.advance();
        reader.consumeMatchIgnoreCase("ll");
        assertEquals("llo", reader.consumeToEnd());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("</title>"));
        assertTrue(reader.containsIgnoreCase("</style>"));
        assertFalse(reader.containsIgnoreCase("</body>"));
    }

}