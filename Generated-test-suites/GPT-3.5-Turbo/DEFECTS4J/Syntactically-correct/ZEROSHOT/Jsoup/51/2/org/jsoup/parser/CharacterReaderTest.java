import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setup() {
        String input = "This is a test string.";
        characterReader = new CharacterReader(input);
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
        assertEquals('h', characterReader.consume());
        assertEquals('i', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
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
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals("h", characterReader.consumeAsString());
        assertEquals("i", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(2, characterReader.nextIndexOf('i'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(2, characterReader.nextIndexOf("is"));
        assertEquals(-1, characterReader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This ", characterReader.consumeTo(' '));
        assertEquals("is", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This is a", characterReader.consumeToAny(' ', 't', 'r'));
        assertEquals("", characterReader.consumeToAny(' ', 't', 'r'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("This ", characterReader.consumeToAnySorted('i', 's', 'a'));
        assertEquals("", characterReader.consumeToAnySorted('i', 's', 'a'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("This", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("This", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string.", characterReader.consumeToEnd());
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
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('t'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("This "));
        assertFalse(characterReader.matches("this "));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("This "));
        assertTrue(characterReader.matchesIgnoreCase("this "));
        assertFalse(characterReader.matchesIgnoreCase("that "));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'h', 'i'));
        assertFalse(characterReader.matchesAny('t'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('a', 'h', 'i'));
        assertFalse(characterReader.matchesAnySorted('t'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("This "));
        assertFalse(characterReader.matchConsume("this "));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("This "));
        assertTrue(characterReader.matchConsumeIgnoreCase("this "));
        assertFalse(characterReader.matchConsumeIgnoreCase("that "));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("test"));
        assertFalse(characterReader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string.", characterReader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader cr = new CharacterReader("This is a test string.");
        cr.cacheString(0, 4);
        assertEquals("", cr.cacheString(0, 4));
    }

    @Test
    public void testRangeEquals() {
        CharacterReader cr = new CharacterReader("This is a test string.");
        assertTrue(cr.rangeEquals(0, 4, "This"));
        assertFalse(cr.rangeEquals(0, 4, "that"));
    }

}