import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("abc");
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
        assertEquals('a', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('a', characterReader.consume());
        assertEquals('b', characterReader.consume());
        assertEquals('c', characterReader.consume());
        assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        characterReader.unconsume();
        assertEquals('b', characterReader.consume());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('b', characterReader.current());
        characterReader.advance();
        assertEquals('c', characterReader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.advance();
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("a", characterReader.consumeAsString());
        assertEquals("b", characterReader.consumeAsString());
        assertEquals("c", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("ab", characterReader.consumeTo('c'));
        characterReader.consume();
        assertEquals("c", characterReader.consumeTo('d'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("a", characterReader.consumeToAny('b','c'));
        assertEquals("bc", characterReader.consumeToAny('d','e'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("abc", characterReader.consumeToEnd());
        assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        characterReader = new CharacterReader("abc123");
        assertEquals("abc", characterReader.consumeLetterSequence());
        assertEquals("123", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("abc123");
        assertEquals("abc", characterReader.consumeHexSequence());
        assertEquals("123", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("abc123");
        assertEquals("", characterReader.consumeDigitSequence());
        assertEquals("123", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('a'));
        assertFalse(characterReader.matches('b'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("A"));
        assertFalse(characterReader.matchesIgnoreCase("B"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('a', 'b'));
        assertFalse(characterReader.matchesAny('b', 'c'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("a"));
        assertFalse(characterReader.matchConsume("b"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("A"));
        assertFalse(characterReader.matchConsumeIgnoreCase("B"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("a"));
        assertTrue(characterReader.containsIgnoreCase("A"));
        assertFalse(characterReader.containsIgnoreCase("b"));
    }

    @Test
    public void testToString() {
        assertEquals("abc", characterReader.toString());
        characterReader.consume();
        assertEquals("bc", characterReader.toString());
    }
}