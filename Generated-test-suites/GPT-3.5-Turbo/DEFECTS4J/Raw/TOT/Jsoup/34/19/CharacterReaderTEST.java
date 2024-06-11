import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("This is a test string.");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
        characterReader.consume();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        assertEquals('T', characterReader.consume());
        characterReader.unconsume();
        assertEquals('T', characterReader.consume());
    }

    @Test
    public void testAdvance() {
        assertEquals('T', characterReader.consume());
        characterReader.advance();
        assertEquals('i', characterReader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.mark();
        assertEquals('T', characterReader.consume());
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals("h", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(-1, characterReader.nextIndexOf('z'));
        assertEquals(1, characterReader.nextIndexOf('h'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        assertEquals(-1, characterReader.nextIndexOf("zzz"));
        assertEquals(5, characterReader.nextIndexOf("is a"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This ", characterReader.consumeTo(' '));
        assertEquals("", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToWithSequence() {
        assertEquals("This", characterReader.consumeTo(" "));
        assertEquals("is", characterReader.consumeTo("is "));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", characterReader.consumeToAny('.',';',','));
        assertEquals("This is a test ", characterReader.consumeToAny('.',';',',','s'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string.", characterReader.consumeToEnd());
        assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
        assertEquals("is", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", characterReader.consumeLetterThenDigitSequence());
        assertEquals("is", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("This", characterReader.consumeHexSequence());
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertTrue(characterReader.matches("This"));
        assertFalse(characterReader.matches('t'));
        assertFalse(characterReader.matches("this"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("this"));
        assertFalse(characterReader.matchesIgnoreCase("test"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('.',';',','));
        assertFalse(characterReader.matchesAny('z','x','y'));
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
        assertTrue(characterReader.matchConsume("This"));
        assertFalse(characterReader.matchConsume("is"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("THiS"));
        assertFalse(characterReader.matchConsumeIgnoreCase("IS"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("test"));
        assertFalse(characterReader.containsIgnoreCase("java"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string.", characterReader.toString());
        characterReader.consumeToEnd();
        assertEquals("", characterReader.toString());
    }
}