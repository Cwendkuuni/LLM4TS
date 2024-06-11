import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test input");
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
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('s', characterReader.consume());
        assertEquals('t', characterReader.consume());
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
        characterReader.mark();
        characterReader.advance();
        assertEquals('e', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfSeq() {
        assertEquals(2, characterReader.nextIndexOf("st"));
        assertEquals(-1, characterReader.nextIndexOf("xx"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test", characterReader.consumeTo(' '));
        assertEquals("input", characterReader.consumeTo('x'));
    }

    @Test
    public void testConsumeToSeq() {
        assertEquals("Test", characterReader.consumeTo(" "));
        assertEquals("input", characterReader.consumeTo("xx"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Test", characterReader.consumeToAny(' '));
        assertEquals("", characterReader.consumeToAny('x'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test input", characterReader.consumeToEnd());
        assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", characterReader.consumeLetterSequence());
        assertEquals("", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Test", characterReader.consumeLetterThenDigitSequence());
        assertEquals("", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertTrue(characterReader.matches("Test"));
        assertFalse(characterReader.matches('x'));
        assertFalse(characterReader.matches("xxxx"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("test"));
        assertTrue(characterReader.matchesIgnoreCase("TEsT"));
        assertFalse(characterReader.matchesIgnoreCase("xxxx"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T'));
        assertTrue(characterReader.matchesAny('T', 'x'));
        assertFalse(characterReader.matchesAny('x'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Test "));
        assertFalse(characterReader.matchConsume("input"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("test "));
        assertFalse(characterReader.matchConsumeIgnoreCase("input"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("test"));
        assertFalse(characterReader.containsIgnoreCase("xxx"));
    }

    @Test
    public void testToString() {
        assertEquals("Test input", characterReader.toString());
    }
}