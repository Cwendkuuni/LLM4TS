package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("Hello, World!");
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
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        char val = reader.consume();
        assertEquals('H', val);
        assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        reader.advance();
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String consumedString = reader.consumeAsString();
        assertEquals("H", consumedString);
    }

    @Test
    public void testConsumeTo() {
        String consumedTo = reader.consumeTo(',');
        assertEquals("Hello", consumedTo);
    }

    @Test
    public void testConsumeToAny() {
        String consumedToAny = reader.consumeToAny('o', ',');
        assertEquals("Hell", consumedToAny);
    }

    @Test
    public void testConsumeToEnd() {
        String consumedToEnd = reader.consumeToEnd();
        assertEquals("Hello, World!", consumedToEnd);
    }

    @Test
    public void testConsumeLetterSequence() {
        String consumedLetterSeq = reader.consumeLetterSequence();
        assertEquals("Hello", consumedLetterSeq);
    }

    @Test
    public void testConsumeHexSequence() {
        String consumedHexSeq = reader.consumeHexSequence();
        assertNotEquals("Hello", consumedHexSeq);
    }

    @Test
    public void testConsumeDigitSequence() {
        String consumedDigitSeq = reader.consumeDigitSequence();
        assertNotEquals("Hello", consumedDigitSeq);
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('H'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("h"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('o', 'e'));
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
        assertTrue(reader.matchConsume("H"));
        assertEquals('e', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("h"));
        assertEquals('e', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("Hello"));
        assertFalse(reader.containsIgnoreCase("World"));
    }

    @Test
    public void testToString() {
        String result = reader.toString();
        assertEquals("Hello, World!", result);
    }
}