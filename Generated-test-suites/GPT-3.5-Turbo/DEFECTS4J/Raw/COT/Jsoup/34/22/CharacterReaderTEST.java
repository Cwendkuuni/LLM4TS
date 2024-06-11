
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("test string");
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
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('t', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('t', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        assertEquals('t', characterReader.consume());
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testAdvance() {
        assertEquals('t', characterReader.current());
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testRewindToMark() {
        characterReader.advance();
        assertEquals(1, characterReader.pos());
        characterReader.mark();
        characterReader.advance();
        assertEquals(2, characterReader.pos());
        characterReader.rewindToMark();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("t", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(0, characterReader.nextIndexOf('t'));
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        assertEquals(0, characterReader.nextIndexOf("t"));
        assertEquals(3, characterReader.nextIndexOf("t "));
        assertEquals(-1, characterReader.nextIndexOf("hello"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("", characterReader.consumeTo('t'));
        assertEquals("t ", characterReader.consumeTo('s'));
        assertEquals("string", characterReader.consumeTo('x'));
    }

    @Test
    public void testConsumeToWithSequence() {
        assertEquals("", characterReader.consumeTo("t"));
        assertEquals("t", characterReader.consumeTo(" s"));
        assertEquals("string", characterReader.consumeTo("x"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("tes", characterReader.consumeToAny('t', 's'));
        assertEquals("t ", characterReader.consumeToAny('x', 's'));
        assertEquals("", characterReader.consumeToAny('x', 'y', 'z'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("test string", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("test", characterReader.consumeLetterSequence());
        assertEquals(" ", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("test", characterReader.consumeLetterThenDigitSequence());
        assertEquals("", characterReader.consumeLetterThenDigitSequence());
        assertEquals("", characterReader.consumeLetterThenDigitSequence());
        assertEquals("", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
        assertEquals("", characterReader.consumeHexSequence());
        assertEquals("", characterReader.consumeHexSequence());
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        assertEquals("", characterReader.consumeDigitSequence());
        assertEquals("", characterReader.consumeDigitSequence());
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('t'));
        assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatchesWithSequence() {
        assertTrue(characterReader.matches("test"));
        assertFalse(characterReader.matches("hello"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("TeSt"));
        assertFalse(characterReader.matchesIgnoreCase("hElLo"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('t', 'e'));
        assertFalse(characterReader.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("test"));
        assertEquals(4, characterReader.pos());
        assertFalse(characterReader.matchConsume("hello"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("teST"));
        assertEquals(4, characterReader.pos());
        assertFalse(characterReader.matchConsumeIgnoreCase("hElLo"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("TEst"));
        assertFalse(characterReader.containsIgnoreCase("Hello"));
    }

    @Test
    public void testToString() {
        assertEquals("test string", characterReader.toString());
        characterReader.consume();
        assertEquals("est string", characterReader.toString());
    }
}
