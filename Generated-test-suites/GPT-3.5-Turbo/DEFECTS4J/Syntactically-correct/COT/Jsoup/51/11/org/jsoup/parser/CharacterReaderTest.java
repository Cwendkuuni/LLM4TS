
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("test string");
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
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('t', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
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
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("t", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOf_sequence() {
        assertEquals(1, characterReader.nextIndexOf("es"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("t", characterReader.consumeTo('e'));
    }

    @Test
    public void testConsumeTo_sequence() {
        assertEquals("t", characterReader.consumeTo("es"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("t", characterReader.consumeToAny('e', 'a', 'i'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("t", characterReader.consumeToAnySorted('e', 'a', 'i'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("t", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("t", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("test string", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("test", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("test", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("test", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('t'));
    }

    @Test
    public void testMatches_sequence() {
        assertTrue(characterReader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("tEsT"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('t', 'e', 's'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('e', 's', 't'));
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
        assertTrue(characterReader.matchConsume("test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("tEsT"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("TeSt"));
    }

    @Test
    public void testToString() {
        assertEquals("test string", characterReader.toString());
    }
}
