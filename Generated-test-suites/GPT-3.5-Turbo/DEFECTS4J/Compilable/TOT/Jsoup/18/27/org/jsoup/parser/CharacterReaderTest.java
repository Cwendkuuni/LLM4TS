package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        for (int i = 0; i < 52; i++) {
            characterReader.consume();
        }
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('a', characterReader.current());
        characterReader.consume();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('a', characterReader.consume());
        assertEquals('b', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testAdvance() {
        assertEquals('a', characterReader.current());
        characterReader.advance();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        assertEquals('c', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("a", characterReader.consumeAsString());
        assertEquals("b", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("abcdefg", characterReader.consumeTo('h'));
        assertEquals("hijklmnopqrstuvwxyzABCDEFG", characterReader.consumeTo('@'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("abcdefghi", characterReader.consumeToAny('j', 'k', 'l'));
        assertEquals("jklmnopqrstuvwxyzABCDEFG", characterReader.consumeToAny('%', '^', '&'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXY", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQ", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("abcdef", characterReader.consumeHexSequence());
        characterReader.advance();
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        characterReader.advance();
        assertEquals("0123456789", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('a'));
        assertFalse(characterReader.matches('b'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("a"));
        assertTrue(characterReader.matchesIgnoreCase("A"));
        assertFalse(characterReader.matchesIgnoreCase("b"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('a', 'b', 'c'));
        assertFalse(characterReader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("a"));
        assertFalse(characterReader.matchConsume("b"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("a"));
        assertTrue(characterReader.matchConsumeIgnoreCase("A"));
        assertFalse(characterReader.matchConsumeIgnoreCase("b"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("DE"));
        assertFalse(characterReader.containsIgnoreCase("lmno"));
    }

    @Test
    public void testToString() {
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", characterReader.toString());
        characterReader.advance();
        assertEquals("bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", characterReader.toString());
    }
}