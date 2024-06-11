package org.jsoup.parser;
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
        characterReader.consume();
        assertEquals(1, characterReader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        characterReader.consume();
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('e', characterReader.consume());
        characterReader.unconsume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testAdvance() {
        assertEquals('T', characterReader.consume());
        characterReader.advance();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        assertEquals('T', characterReader.consume());
        characterReader.mark();
        characterReader.advance();
        assertEquals('e', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfString() {
        assertEquals(1, characterReader.nextIndexOf("est"));
        assertEquals(-1, characterReader.nextIndexOf("xyz"));
    }
    
    @Test
    public void testConsumeToChar() {
        assertEquals("Test ", characterReader.consumeTo(' '));
        assertEquals("String", characterReader.consumeTo('z'));
    }
    
    @Test
    public void testConsumeToString() {
        assertEquals("Test", characterReader.consumeTo(" "));
        assertEquals("", characterReader.consumeTo("xyz"));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("Test Str", characterReader.consumeToAny(' ', 'i'));
        assertEquals("ng", characterReader.consumeToEnd());
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("Test String", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", characterReader.consumeLetterSequence());
        assertEquals(" ", characterReader.consumeAsString());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Test", characterReader.consumeLetterThenDigitSequence());
        assertEquals(" ", characterReader.consumeAsString());
    }
    
    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
        assertEquals(" ", characterReader.consumeAsString());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        assertEquals(" ", characterReader.consumeAsString());
    }
    
    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('t'));
    }
    
    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("Test"));
        assertFalse(characterReader.matches("test"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("Test"));
        assertTrue(characterReader.matchesIgnoreCase("test"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 't'));
        assertFalse(characterReader.matchesAny('a', 'b'));
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
        assertTrue(characterReader.matchConsume("Test"));
        assertFalse(characterReader.matchConsume("test"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("Test"));
        assertTrue(characterReader.matchConsumeIgnoreCase("test"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("Test"));
        assertFalse(characterReader.containsIgnoreCase("test"));
    }
    
    @Test
    public void testToString() {
        assertEquals("Test String", characterReader.toString());
    }
}