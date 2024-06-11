import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
    }

    @After
    public void tearDown() {
        characterReader = null;
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
        assertEquals('L', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('L', characterReader.consume());
        assertEquals('o', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('L', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('o', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.advance();
        characterReader.mark();
        assertEquals('r', characterReader.current());
    }

    @Test
    public void testRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('r', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("L", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(-1, characterReader.nextIndexOf('x'));
        assertEquals(1, characterReader.nextIndexOf('o'));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(-1, characterReader.nextIndexOf("xyz"));
        assertEquals(2, characterReader.nextIndexOf("re"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("L", characterReader.consumeTo('o'));
        assertEquals("orem ipsum d", characterReader.consumeTo("si"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Lorem ", characterReader.consumeToAny('i', 'e'));
        assertEquals("i", characterReader.consumeToAny(',', '.'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Lorem", characterReader.consumeLetterSequence());
        assertEquals(" ", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Lorem", characterReader.consumeLetterThenDigitSequence());
        assertEquals(" ", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("L", characterReader.consumeHexSequence());
        assertEquals("orem", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        characterReader.advance();
        assertEquals("orem", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('L'));
        assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertFalse(characterReader.matchesIgnoreCase("lorem"));
        assertTrue(characterReader.matchesIgnoreCase("LOREM"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('L', 'x', 'o'));
        assertFalse(characterReader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Lo"));
        assertFalse(characterReader.matchConsume("rem"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("lOr"));
        assertFalse(characterReader.matchConsumeIgnoreCase("rem"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("IPSUM"));
        assertFalse(characterReader.containsIgnoreCase("ipsum"));
    }

    @Test
    public void testToString() {
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", characterReader.toString());
    }
}