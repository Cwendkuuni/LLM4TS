package org.jsoup.parser;

import org.junit.*;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Test");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf_char() {
        assertEquals(1, reader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        assertEquals(1, reader.nextIndexOf("es"));
    }

    @Test
    public void testConsumeTo_char() {
        assertEquals("T", reader.consumeTo('e'));
    }

    @Test
    public void testConsumeTo_CharSequence() {
        assertEquals("T", reader.consumeTo("es"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("T", reader.consumeToAny('e'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("T", reader.consumeToAnySorted('e'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("Test", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Test", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Test", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("Test", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("Test", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        assertTrue(reader.matches('T'));
    }

    @Test
    public void testMatches_String() {
        assertTrue(reader.matches("Test"));
    }

    @Test
    public void testMatchesIgnoreCase_String() {
        assertTrue(reader.matchesIgnoreCase("TEST"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted('T'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("Test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("TEST"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(reader.containsIgnoreCase("test"));
    }

    @Test
    public void testToString() {
        assertEquals("Test", reader.toString());
    }
}