import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Hello, World!");
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
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
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
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("Hello", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToString() {
        assertEquals("Hello, ", characterReader.consumeTo(","));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", characterReader.consumeToAny('.', '!'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello, World!", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("3ACD1");
        assertEquals("3ACD1", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("567");
        assertEquals("567", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('W'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("Hello"));
        assertFalse(characterReader.matches("World"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("hello"));
        assertFalse(characterReader.matchesIgnoreCase("world"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('H', 'W'));
        assertFalse(characterReader.matchesAny('A', 'B'));
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
        assertTrue(characterReader.matchConsume("Hello"));
        assertFalse(characterReader.matchConsume("World"));
        assertEquals(5, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("hello"));
        assertFalse(characterReader.matchConsumeIgnoreCase("world"));
        assertEquals(5, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("WORLD"));
        assertFalse(characterReader.containsIgnoreCase("WORLD!"));
    }

    @Test
    public void testToString() {
        assertEquals("Hello, World!", characterReader.toString());
    }
}