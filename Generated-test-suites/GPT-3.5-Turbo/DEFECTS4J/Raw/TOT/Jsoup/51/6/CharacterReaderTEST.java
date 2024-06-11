import org.jsoup.helper.Validate;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
    }

    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        assertEquals(1, characterReader.nextIndexOf('h'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(1, characterReader.nextIndexOf("his"));
        assertEquals(-1, characterReader.nextIndexOf("test"));
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("This is a ", characterReader.consumeTo('t'));
        assertEquals("t", characterReader.consumeTo('s'));
    }

    @Test
    public void testConsumeToString() {
        assertEquals("This is a ", characterReader.consumeTo("test"));
        assertEquals("t", characterReader.consumeTo("string"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This ", characterReader.consumeToAny('i', 's'));
        assertEquals(" is a te", characterReader.consumeToAny('i', 's'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("This ", characterReader.consumeToAnySorted('i', 's'));
        assertEquals("", characterReader.consumeToAnySorted('i', 's'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("This", characterReader.consumeTagName());
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
        assertEquals("", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
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
        assertFalse(characterReader.matches('t'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("T"));
        assertFalse(characterReader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("t"));
        assertFalse(characterReader.matchesIgnoreCase("TEST"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 't'));
        assertFalse(characterReader.matchesAny('E', 'S'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('T', 't'));
        assertFalse(characterReader.matchesAnySorted('E', 'S'));
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
        assertTrue(characterReader.matchConsume("This"));
        assertFalse(characterReader.matchConsume("is"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("this"));
        assertFalse(characterReader.matchConsumeIgnoreCase("is"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(characterReader.containsIgnoreCase("This"));
        assertFalse(characterReader.containsIgnoreCase("is"));
    }

    @Test
    public void testRangeEquals() {
        assertFalse(characterReader.rangeEquals(0, 4, "This"));
        assertFalse(characterReader.rangeEquals(5, 2, "is"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string.", characterReader.toString());
    }

    @Test
    public void testCacheString() {
        String cachedString = characterReader.cacheString(0, 4);
        assertEquals("This", cachedString);
    }

    @Test
    public void testRangeEqualsWithCachedString() {
        String cachedString = characterReader.cacheString(0, 4);
        assertTrue(characterReader.rangeEquals(0, 4, cachedString));
    }
}