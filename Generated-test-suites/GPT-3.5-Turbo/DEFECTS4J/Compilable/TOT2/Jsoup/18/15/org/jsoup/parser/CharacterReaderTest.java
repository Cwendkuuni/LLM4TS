package org.jsoup.parser;
import org.junit.*;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Testing 1, 2, 3!");
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
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('s', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Testing 1, 2, ", characterReader.consumeTo(','));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Testing 1", characterReader.consumeToAny(' ', ','));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Testing 1, 2, 3!", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Testing", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("1", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("TeStInG"));
        assertFalse(characterReader.matchesIgnoreCase("123"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'e', 's'));
        assertFalse(characterReader.matchesAny('1', '2', '3'));
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Tes"));
        assertFalse(characterReader.matchConsume("ting"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("TEs"));
        assertFalse(characterReader.matchConsumeIgnoreCase("ng"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("Ing"));
        assertFalse(characterReader.containsIgnoreCase("Foo"));
    }

    @Test
    public void testToString() {
        assertEquals("Testing 1, 2, 3!", characterReader.toString());
    }
}