import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("test input");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('t', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
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
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("t", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf_char() {
        assertEquals(1, characterReader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        assertEquals(1, characterReader.nextIndexOf("es"));
    }

    @Test
    public void testConsumeTo_char() {
        assertEquals("test", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeTo_String() {
        assertEquals("test", characterReader.consumeTo(" "));
    }

    @Test
    public void testConsumeToAny_charArray() {
        assertEquals("test", characterReader.consumeToAny(' ', 't'));
    }

    @Test
    public void testConsumeToAnySorted_charArray() {
        assertEquals(" ", characterReader.consumeToAnySorted(' ', 't'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("test", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("test", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("test input", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("test", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("test", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("test", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        assertTrue(characterReader.matches('t'));
    }

    @Test
    public void testMatches_String() {
        assertTrue(characterReader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase_String() {
        assertTrue(characterReader.matchesIgnoreCase("TEST"));
    }

    @Test
    public void testMatchesAny_charArray() {
        assertTrue(characterReader.matchesAny('t'));
    }

    @Test
    public void testMatchesAnySorted_charArray() {
        assertTrue(characterReader.matchesAnySorted('t'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume_String() {
        assertTrue(characterReader.matchConsume("test"));
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase_String() {
        assertTrue(characterReader.matchConsumeIgnoreCase("TEST"));
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase_String() {
        assertTrue(characterReader.containsIgnoreCase("TEST"));
    }

    @Test
    public void testToString() {
        assertEquals("test input", characterReader.toString());
    }

    @Test
    public void testRangeEquals() {
        assertTrue(characterReader.rangeEquals(0, 4, "test"));
    }
}