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
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(1, characterReader.nextIndexOf("es"));
        assertEquals(-1, characterReader.nextIndexOf("ex"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test ", characterReader.consumeTo('I'));
        assertEquals(" Input", characterReader.consumeTo('t'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Te", characterReader.consumeToAny('s', 'I'));
        assertEquals("st", characterReader.consumeToAny('n', 'G'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("Te", characterReader.consumeToAnySorted('I', 's'));
        assertEquals("st", characterReader.consumeToAnySorted('G', 'n'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("Test ", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Test", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test Input", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", characterReader.consumeLetterSequence());
        assertEquals(" ", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Test", characterReader.consumeLetterThenDigitSequence());
        assertEquals(" ", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("Test", characterReader.consumeHexSequence());
        assertEquals(" ", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        assertEquals(" ", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("Test"));
        assertFalse(characterReader.matches("Testx"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("test"));
        assertFalse(characterReader.matchesIgnoreCase("testx"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'x'));
        assertFalse(characterReader.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('T', 'x'));
        assertFalse(characterReader.matchesAnySorted('x', 'y'));
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
        assertTrue(characterReader.matchConsume("Test"));
        assertFalse(characterReader.matchConsume("Testx"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("test"));
        assertFalse(characterReader.matchConsumeIgnoreCase("testx"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(characterReader.containsIgnoreCase("abc"));
        assertTrue(characterReader.containsIgnoreCase("TeSt"));
    }

    @Test
    public void testToString() {
        assertEquals("Test Input", characterReader.toString());
    }

    @Test
    public void testCacheString() {
        String cached = characterReader.cacheString(0, 4);
        assertEquals("Test", cached);

        String cachedAgain = characterReader.cacheString(0, 4);
        assertSame(cached, cachedAgain);

        String newCached = characterReader.cacheString(2, 2);
        assertNotSame(cached, newCached);
    }

    @Test
    public void testRangeEquals() {
        assertTrue(characterReader.rangeEquals(0, 4, "Test"));
        assertFalse(characterReader.rangeEquals(0, 4, "tesT"));
    }
}