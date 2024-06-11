import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("This-is-a-test.");
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
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        assertEquals(0, characterReader.mark);
    }

    @Test
    public void testRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf_char() {
        assertEquals(3, characterReader.nextIndexOf('-'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        assertEquals(10, characterReader.nextIndexOf("-a"));
    }

    @Test
    public void testConsumeTo_char() {
        assertEquals("This", characterReader.consumeTo('.'));
    }

    @Test
    public void testConsumeTo_String() {
        assertEquals("This", characterReader.consumeTo("-a"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This", characterReader.consumeToAny('-', 'a'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("This", characterReader.consumeToAnySorted('-', 'a', '.'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("Th", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("This", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This-is-a-test.", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("This", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("This", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        assertTrue(characterReader.matches('T'));
    }

    @Test
    public void testMatches_String() {
        assertTrue(characterReader.matches("This"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("this"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 't', 'x'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('T', 't', 'x'));
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
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("This"));
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("this"));
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("This"));
    }

    @Test
    public void testRangeEquals() {
        String cached = "This";
        assertFalse(characterReader.rangeEquals(0, 4, cached));
    }

    @Test
    public void testCacheString() {
        String result = characterReader.cacheString(0, 4);
        assertEquals("This", result);
    }

    @Test
    public void testToString() {
        assertEquals("This-is-a-test.", characterReader.toString());
    }

    @Test
    public void testCacheString_fullCoverage() {
        characterReader.cacheString(0, 4);
        characterReader.cacheString(0, 4);
        characterReader.cacheString(0, 4);
        characterReader.cacheString(0, 4);
        characterReader.cacheString(0, 4);
        assertEquals("This", characterReader.toString());
    }

}