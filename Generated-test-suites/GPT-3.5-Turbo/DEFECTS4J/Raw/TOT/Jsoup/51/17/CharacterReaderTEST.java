
package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CharacterReaderTest {
    @Mock
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test Input");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        characterReader.consume();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        for (int i = 0; i < characterReader.length; i++) {
            characterReader.consume();
        }
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithSeq() {
        assertEquals(0, characterReader.nextIndexOf("Te"));
        assertEquals(-1, characterReader.nextIndexOf("zt"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("T", characterReader.consumeTo(' '));
        assertEquals("es", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToWithSeq() {
        assertEquals("Test In", characterReader.consumeTo("put"));
        assertEquals(" ", characterReader.consumeTo(" "));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Te", characterReader.consumeToAny(' ', 'I'));
        assertEquals("st", characterReader.consumeToAny(' ', 'F'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("Te", characterReader.consumeToAnySorted(' ', 'I'));
        assertEquals("", characterReader.consumeToAnySorted('F', 'G'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("Test", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Test", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test Input", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", characterReader.consumeLetterSequence());
        assertEquals(" ", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Character", characterReader.consumeLetterThenDigitSequence());
        assertEquals("Reader", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader.advance();
        assertEquals("est", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("T", characterReader.consumeDigitSequence());
        assertEquals("est Inp", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatchesWithSeq() {
        assertTrue(characterReader.matches("Test"));
        assertFalse(characterReader.matches("Input"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("test"));
        assertFalse(characterReader.matchesIgnoreCase("input"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T'));
        assertFalse(characterReader.matchesAny('e', 'I'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted(' ', 'I'));
        assertFalse(characterReader.matchesAnySorted('i', 'j'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Test"));
        assertFalse(characterReader.matchConsume("Input"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("test"));
        assertFalse(characterReader.matchConsumeIgnoreCase("input"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("<style>"));
        assertFalse(characterReader.containsIgnoreCase("</title>"));
    }

    @Test
    public void testToString() {
        assertEquals("Test Input", characterReader.toString());
    }

    @Test
    public void testCacheString() {
        assertEquals("Test", characterReader.cacheString(0, 4));
        assertEquals("Input", characterReader.cacheString(5, 5));
    }

    @Test
    public void testRangeEquals() {
        assertTrue(characterReader.rangeEquals(0, 4, "Test"));
        assertFalse(characterReader.rangeEquals(5, 5, "Input"));
    }
}
