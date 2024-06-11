package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("Hello, World!");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.consumeTo('o');
        assertEquals(5, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('l', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume(); // 'H'
        reader.consume(); // 'e'
        reader.unconsume();
        assertEquals('e', reader.current());
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        assertEquals('H', reader.consume());
        reader.advance();
        assertEquals('l', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("l", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(7, reader.nextIndexOf('W'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOf_Sequence() {
        assertEquals(7, reader.nextIndexOf("World"));
        assertEquals(-1, reader.nextIndexOf("Country"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("", reader.consumeTo('H'));
        assertEquals("Hello, ", reader.consumeTo('W'));
        assertEquals("World", reader.consumeTo('!'));
    }

    @Test
    public void testConsumeTo_Sequence() {
        assertEquals("", reader.consumeTo("Hello"));
        assertEquals("Hello, ", reader.consumeTo("World"));
        assertEquals("Hello, World", reader.consumeTo("!"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", reader.consumeToAny(','));
        assertEquals("Hello", reader.consumeToAny('o'));
        assertEquals("World!", reader.consumeToAny('z'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("", reader.consumeToAnySorted(','));
        assertEquals("Hello", reader.consumeToAnySorted('l', 'o'));
        assertEquals("World!", reader.consumeToAnySorted('z'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("", reader.consumeData());
        reader.consumeToAny(',', '!');
        assertEquals(" World", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("", reader.consumeTagName());
        reader.consumeToAnySorted('<', '/');
        assertEquals("World", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello, World!", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Hello", reader.consumeLetterThenDigitSequence());
        assertEquals(",", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        reader.consume();
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('H'));
        reader.consumeTo('H');
        assertFalse(reader.matches('H'));
    }

    @Test
    public void testMatches_Sequence() {
        assertTrue(reader.matches("Hello"));
        reader.consumeTo("Hello");
        assertFalse(reader.matches("Hello"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("hello"));
        reader.consumeTo("He");
        assertTrue(reader.matchesIgnoreCase("llo"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny(','));
        reader.consumeToAny(',');
        assertFalse(reader.matchesAny(','));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted(','));
        reader.consumeToAny(',', '!');
        assertFalse(reader.matchesAnySorted(','));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("Hello"));
        assertFalse(reader.matchConsume("Hello"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("hello"));
        assertFalse(reader.matchConsumeIgnoreCase("hello"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("worLd"));
        assertFalse(reader.containsIgnoreCase("EnGland"));
    }

    @Test
    public void testToString() {
        assertEquals("Hello, World!", reader.toString());
        reader.consumeTo(',');
        assertEquals(", World!", reader.toString());
    }

    @Test
    public void testRangeEquals() {
        assertTrue(reader.rangeEquals(0, 5, "Hello"));
        assertFalse(reader.rangeEquals(0, 5, "hello"));
    }
}