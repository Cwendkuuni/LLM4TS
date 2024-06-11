import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test String");
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
    public void testAdvance() {
        characterReader.consume();
        characterReader.advance();
        assertEquals('s', characterReader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(2, characterReader.nextIndexOf('s'));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Test", characterReader.consumeToAny(' ', 'S'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test String", characterReader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('e', 'T'));
    }

    @Test
    public void testMatchesLetter() {
        characterReader.consume();
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        characterReader.consume();
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("es"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("Es"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("TeSt"));
    }
}