import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader(new StringReader("Hello, World!"));
    }

    @Test
    public void testPos() {
        assertEquals("Initial position should be 0", 0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse("CharacterReader should not be empty initially", characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals("Current character should be 'H'", 'H', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals("Consume should return 'H'", 'H', characterReader.consume());
        assertEquals("After consuming once, current character should be 'e'", 'e', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals("After unconsuming, current character should be the same as before consuming", 'H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals("After advancing, position should increase by 1", 1, characterReader.pos());
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals("After rewinding, position should be the same as the marked position", 0, characterReader.pos());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals("Next index of 'e' should be 1", 1, characterReader.nextIndexOf('e'));
    }

    @Test
    public void testConsumeTo() {
        String consumed = characterReader.consumeTo(',');
        assertEquals("Consumed string should be 'Hello'", "Hello", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        String consumed = characterReader.consumeToEnd();
        assertEquals("Consumed string should be 'Hello, World!'", "Hello, World!", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        characterReader.advance();
        String consumed = characterReader.consumeLetterSequence();
        assertEquals("Consumed string should be 'ello'", "ello", consumed);
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue("ContainsIgnoreCase should return true for 'world'", characterReader.containsIgnoreCase("world"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testUnconsumeException() {
        characterReader.unconsume();
        characterReader.unconsume();
    }

    @Test
    public void testEmptyStringConsumeTo() {
        CharacterReader emptyReader = new CharacterReader(new StringReader(""));
        String consumed = emptyReader.consumeTo(',');
        assertEquals("ConsumeTo on empty string should return an empty string", "", consumed);
    }

    @Test
    public void testEmptyStringConsumeToEnd() {
        CharacterReader emptyReader = new CharacterReader(new StringReader(""));
        String consumed = emptyReader.consumeToEnd();
        assertEquals("ConsumeToEnd on empty string should return an empty string", "", consumed);
    }

    @Test
    public void testMatches() {
        assertTrue("Current character should match 'H'", characterReader.matches('H'));
    }

    @Test
    public void testMatchConsume() {
        assertTrue("Should match and consume 'Hel'", characterReader.matchConsume("Hel"));
        assertEquals("Position should be moved after consuming 'Hel'", 3, characterReader.pos());
    }

    @Test
    public void testMatchesAny() {
        assertTrue("Current character should match any in 'xHW'", characterReader.matchesAny('x', 'H', 'W'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue("Current character should be a letter", characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse("Current character should not be a digit", characterReader.matchesDigit());
    }
}