import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Hello World!");
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
        consumeTillEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        assertEquals('H', characterReader.current());
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMarkAndRewind() {
        characterReader.mark();
        characterReader.advance();
        characterReader.advance();
        assertEquals('l', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(2, characterReader.nextIndexOf('l'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfSeq() {
        assertEquals(2, characterReader.nextIndexOf("llo"));
        assertEquals(-1, characterReader.nextIndexOf("abc"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello ", characterReader.consumeTo('W'));
        assertEquals("World!", characterReader.consumeTo('!'));
    }

    @Test
    public void testConsumeToSeq() {
        assertEquals("Hello ", characterReader.consumeTo("Wor"));
        assertEquals("World!", characterReader.consumeTo("!"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello ", characterReader.consumeToAny('W', ' '));
        assertEquals("World!", characterReader.consumeToAny('!'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello World!", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Hello", characterReader.consumeLetterThenDigitSequence());
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
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('h'));
    }

    @Test
    public void testMatchesSeq() {
        assertTrue(characterReader.matches("Hello"));
        assertFalse(characterReader.matches("hello"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("Hello"));
        assertTrue(characterReader.matchesIgnoreCase("hello"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('H', 'e'));
        assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
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
        assertTrue(characterReader.matchConsume("Hello"));
        assertFalse(characterReader.matchConsume("Hello"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("Hello"));
        assertFalse(characterReader.matchConsumeIgnoreCase("hello"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("world"));
        assertFalse(characterReader.containsIgnoreCase("earth"));
    }

    private void consumeTillEnd() {
        while(!characterReader.isEmpty()) {
            characterReader.consume();
        }
    }
}