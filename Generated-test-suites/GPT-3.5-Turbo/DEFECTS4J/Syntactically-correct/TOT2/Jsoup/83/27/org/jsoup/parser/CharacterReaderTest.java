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
        assertEquals('e', characterReader.consume());
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
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(0, characterReader.nextIndexOf("Hello"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello", characterReader.consumeTo(','));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", characterReader.consumeToAny(',','!'));
    }

    @Test
    public void testConsumeToEnd() {
        characterReader.consume();
        assertEquals("ello, World!", characterReader.consumeToEnd());
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
        String hexSequence = "0123456789ABCDEFabcdef";
        characterReader = new CharacterReader(hexSequence);
        assertEquals(hexSequence, characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("12345");
        assertEquals("12345", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('A'));
    }

    @Test
    public void testMatchesSequence() {
        assertTrue(characterReader.matches("Hello"));
        assertFalse(characterReader.matches("World"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("hello"));
        assertFalse(characterReader.matchesIgnoreCase("WORLD"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('H','W','!'));
        assertFalse(characterReader.matchesAny('A','B'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        characterReader = new CharacterReader("12345");
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Hello"));
        assertEquals(6, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("hello"));
        assertEquals(5, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("hello"));
        assertFalse(characterReader.containsIgnoreCase("WORLD"));
    }

}