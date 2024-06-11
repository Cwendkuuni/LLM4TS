import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test Input");
    }

    @Test
    public void testPos() {
        int pos = characterReader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testIsEmpty() {
        boolean isEmpty = characterReader.isEmpty();
        assertFalse(isEmpty);
    }

    @Test
    public void testCurrent() {
        char current = characterReader.current();
        assertEquals('T', current);
    }

    @Test
    public void testConsume() {
        char consumed = characterReader.consume();
        assertEquals('T', consumed);
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        char current = characterReader.current();
        assertEquals('T', current);
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        int pos = characterReader.pos();
        assertEquals(1, pos);
    }

    @Test
    public void testMark() {
        characterReader.mark();
        int mark = characterReader.pos();
        assertEquals(0, mark);
    }

    @Test
    public void testRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        int pos = characterReader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testConsumeAsString() {
        String consumedString = characterReader.consumeAsString();
        assertEquals("T", consumedString);
    }

    @Test
    public void testNextIndexOf() {
        int offset = characterReader.nextIndexOf(' ');
        assertEquals(4, offset);
    }

    @Test
    public void testNextIndexOfWithSeq() {
        int offset = characterReader.nextIndexOf("Test");
        assertEquals(0, offset);
    }

    @Test
    public void testConsumeTo() {
        String consumedString = characterReader.consumeTo(" Input");
        assertEquals("Test", consumedString);
    }

    @Test
    public void testConsumeToAny() {
        String consumedString = characterReader.consumeToAny(' ', 'I');
        assertEquals("Test", consumedString);
    }

    @Test
    public void testConsumeToEnd() {
        String consumedString = characterReader.consumeToEnd();
        assertEquals("Test Input", consumedString);
    }

    @Test
    public void testConsumeLetterSequence() {
        String letterSequence = characterReader.consumeLetterSequence();
        assertEquals("Test", letterSequence);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String sequence = characterReader.consumeLetterThenDigitSequence();
        assertEquals("Test", sequence);
    }

    @Test
    public void testConsumeHexSequence() {
        String hexSequence = characterReader.consumeHexSequence();
        assertEquals("Test", hexSequence);
    }

    @Test
    public void testConsumeDigitSequence() {
        String digitSequence = characterReader.consumeDigitSequence();
        assertEquals("Test", digitSequence);
    }

    @Test
    public void testMatches() {
        boolean isMatch = characterReader.matches('T');
        assertTrue(isMatch);
    }

    @Test
    public void testMatchesWithSeq() {
        boolean isMatch = characterReader.matches("Test");
        assertTrue(isMatch);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean isMatch = characterReader.matchesIgnoreCase("test");
        assertTrue(isMatch);
    }

    @Test
    public void testMatchesAny() {
        boolean isMatch = characterReader.matchesAny('T', 'E', 'S', 'T');
        assertTrue(isMatch);
    }

    @Test
    public void testMatchesLetter() {
        boolean isMatch = characterReader.matchesLetter();
        assertTrue(isMatch);
    }

    @Test
    public void testMatchesDigit() {
        boolean isMatch = characterReader.matchesDigit();
        assertTrue(isMatch);
    }

    @Test
    public void testMatchConsume() {
        boolean isMatchAndConsumed = characterReader.matchConsume("Test");
        assertTrue(isMatchAndConsumed);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean isMatchAndConsumed = characterReader.matchConsumeIgnoreCase("test");
        assertTrue(isMatchAndConsumed);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean contains = characterReader.containsIgnoreCase("test");
        assertTrue(contains);
    }

    @Test
    public void testToString() {
        String remainingInput = characterReader.toString();
        assertEquals("Test Input", remainingInput);
    }
}

