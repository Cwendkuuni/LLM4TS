import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;

import java.util.Locale;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("example input");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmptyFalse() {
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testIsEmptyTrue() {
        CharacterReader emptyReader = new CharacterReader("");
        assertTrue(emptyReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('e', characterReader.consume());
        assertEquals('x', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('x', characterReader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('x', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("e", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('x'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(1, characterReader.nextIndexOf("xam"));
        assertEquals(-1, characterReader.nextIndexOf("zzz"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("exam", characterReader.consumeTo('p'));
        assertEquals("le in", characterReader.consumeTo('u'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("example", characterReader.consumeToAny('x', 'z'));
        assertEquals(" in", characterReader.consumeToAny('u', 'y'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("example input", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("example", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("example", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("example", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("examp", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('e'));
        assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("exa"));
        assertFalse(characterReader.matches("zzz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("Exa"));
        assertFalse(characterReader.matchesIgnoreCase("zzz"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('e', 'x', 'z'));
        assertFalse(characterReader.matchesAny('y', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("exa"));
        assertFalse(characterReader.matchConsume("zzz"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("EXA"));
        assertFalse(characterReader.matchConsumeIgnoreCase("ZZZ"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("EX"));
        assertFalse(characterReader.containsIgnoreCase("ZZZ"));
    }

    @Test
    public void testToString() {
        assertEquals("example input", characterReader.toString());
    }
}